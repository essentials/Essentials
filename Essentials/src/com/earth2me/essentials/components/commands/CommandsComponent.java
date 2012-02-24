package com.earth2me.essentials.components.commands;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.api.ICommandsComponent;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentialsModule;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.Component;
import com.earth2me.essentials.components.users.IUser;
import java.util.*;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class CommandsComponent extends Component implements ICommandsComponent
{
	private final transient ClassLoader classLoader;
	private final transient String commandPath;
	private final transient String permissionPrefix;
	private final transient IEssentialsModule module;	private final transient Map<String, List<PluginCommand>> altcommands = new HashMap<String, List<PluginCommand>>();
	private final transient Map<String, String> disabledList = new HashMap<String, String>();
	private final transient Map<String, IEssentialsCommand> commands = new HashMap<String, IEssentialsCommand>();

	public CommandsComponent(ClassLoader classLoader, String commandPath, String permissionPrefix, IContext ess)
	{
		this(classLoader, commandPath, permissionPrefix, null, ess);
	}

	public CommandsComponent(ClassLoader classLoader, String commandPath, String permissionPrefix, IEssentialsModule module, IContext context)
	{
		super(context);

		this.classLoader = classLoader;
		this.commandPath = commandPath;
		this.permissionPrefix = permissionPrefix;
		this.module = module;

		for (Plugin plugin : context.getServer().getPluginManager().getPlugins())
		{
			if (plugin.isEnabled())
			{
				addPlugin(plugin);
			}
		}
	}

	@Override
	@SuppressWarnings("CallToThreadDumpStack")
	public boolean handleCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args)
	{
		boolean disabled = false;
		boolean overridden = false;
		ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		try
		{
			disabled = settings.getData().getCommands().isDisabled(command.getName());
			overridden = !disabled || settings.getData().getCommands().isOverridden(command.getName());
		}
		finally
		{
			settings.unlock();
		}
		// Allow plugins to override the command via onCommand
		if (!overridden && (!commandLabel.startsWith("e") || commandLabel.equalsIgnoreCase(command.getName())))
		{
			final PluginCommand pc = getAlternative(commandLabel);
			if (pc != null)
			{

				executed(commandLabel, pc.getLabel());
				try
				{
					return pc.execute(sender, commandLabel, args);
				}
				catch (final Exception ex)
				{
					final ArrayList<StackTraceElement> elements = new ArrayList<StackTraceElement>(Arrays.asList(ex.getStackTrace()));
					elements.remove(0);
					final ArrayList<StackTraceElement> toRemove = new ArrayList<StackTraceElement>();
					for (final StackTraceElement e : elements)
					{
						if (e.getClassName().equals("com.earth2me.essentials.Essentials"))
						{
							toRemove.add(e);
						}
					}
					elements.removeAll(toRemove);
					final StackTraceElement[] trace = elements.toArray(new StackTraceElement[elements.size()]);
					ex.setStackTrace(trace);
					ex.printStackTrace();
					sender.sendMessage(ChatColor.RED + "An internal error occurred while attempting to perform this command");
					return true;
				}
			}
		}

		try
		{
			IUser user = null;
			if (sender instanceof Player)
			{
				user = getContext().getUser((Player)sender);
				getContext().getLogger().log(Level.INFO, String.format("[PLAYER_COMMAND] %s: /%s %s ", ((Player)sender).getName(), commandLabel, EssentialsCommand.getFinalArg(args, 0)));
			}

			// Check for disabled commands
			if (disabled)
			{
				return true;
			}

			final String commandName = command.getName().toLowerCase(Locale.ENGLISH);
			IEssentialsCommand cmd = commands.get(commandName);
			if (cmd == null)
			{
				try
				{
					cmd = (IEssentialsCommand)classLoader.loadClass(commandPath + commandName).newInstance();
					cmd.init(getContext(), commandName);
					cmd.setEssentialsModule(module);
					commands.put(commandName, cmd);
				}
				catch (Exception ex)
				{
					sender.sendMessage(_("commandNotLoaded", commandName));
					getContext().getLogger().log(Level.SEVERE, _("commandNotLoaded", commandName), ex);
					return true;
				}
			}

			// Check authorization
			if (sender != null && cmd.isAuthorized(sender))
			{
				getContext().getLogger().log(Level.WARNING, _("deniedAccessCommand", user.getName()));
				user.sendMessage(_("noAccessCommand"));
				return true;
			}

			// Run the command
			try
			{
				if (user == null)
				{
					cmd.run(sender, command, commandLabel, args);
				}
				else
				{
					user.acquireReadLock();
					try
					{
						cmd.run(user, command, commandLabel, args);
					}
					finally
					{
						user.unlock();
					}
				}
				return true;
			}
			catch (NoChargeException ex)
			{
				return true;
			}
			catch (NotEnoughArgumentsException ex)
			{
				sender.sendMessage(command.getDescription());
				sender.sendMessage(command.getUsage().replaceAll("<command>", commandLabel));
				if (!ex.getMessage().isEmpty())
				{
					sender.sendMessage(ex.getMessage());
				}
				return true;
			}
			catch (Throwable ex)
			{
				showCommandError(sender, commandLabel, ex);
				return true;
			}
		}
		catch (Throwable ex)
		{
			getContext().getLogger().log(Level.SEVERE, _("commandFailed", commandLabel), ex);
			return true;
		}
	}

	@Override
	public void showCommandError(final CommandSender sender, final String commandLabel, final Throwable exception)
	{
		sender.sendMessage(_("errorWithMessage", exception.getMessage()));
		if (getContext().getSettings().isDebug())
		{
			getContext().getLogger().log(Level.WARNING, _("errorCallingCommand", commandLabel), exception);
		}
	}

	@Override
	public void reload()
	{
	}

	@Override
	public final void addPlugin(final Plugin plugin)
	{
		if (plugin.getDescription().getMain().contains("com.earth2me.essentials"))
		{
			return;
		}
		final List<Command> commandList = PluginCommandYamlParser.parse(plugin);
		final String pluginName = plugin.getDescription().getName().toLowerCase(Locale.ENGLISH);

		for (Command command : commandList)
		{
			final PluginCommand pc = (PluginCommand)command;
			final List<String> labels = new ArrayList<String>(pc.getAliases());
			labels.add(pc.getName());

			PluginCommand reg = getContext().getServer().getPluginCommand(pluginName + ":" + pc.getName().toLowerCase(Locale.ENGLISH));
			if (reg == null)
			{
				reg = getContext().getServer().getPluginCommand(pc.getName().toLowerCase(Locale.ENGLISH));
			}
			if (reg == null || !reg.getPlugin().equals(plugin))
			{
				continue;
			}
			for (String label : labels)
			{
				List<PluginCommand> plugincommands = altcommands.get(label.toLowerCase(Locale.ENGLISH));
				if (plugincommands == null)
				{
					plugincommands = new ArrayList<PluginCommand>();
					altcommands.put(label.toLowerCase(Locale.ENGLISH), plugincommands);
				}
				boolean found = false;
				for (PluginCommand pc2 : plugincommands)
				{
					if (pc2.getPlugin().equals(plugin))
					{
						found = true;
					}
				}
				if (!found)
				{
					plugincommands.add(reg);
				}
			}
		}
	}

	@Override
	public void removePlugin(final Plugin plugin)
	{
		final Iterator<Map.Entry<String, List<PluginCommand>>> iterator = altcommands.entrySet().iterator();
		while (iterator.hasNext())
		{
			final Map.Entry<String, List<PluginCommand>> entry = iterator.next();
			final Iterator<PluginCommand> pcIterator = entry.getValue().iterator();
			while (pcIterator.hasNext())
			{
				final PluginCommand pc = pcIterator.next();
				if (pc.getPlugin() == null || pc.getPlugin().equals(plugin))
				{
					pcIterator.remove();
				}
			}
			if (entry.getValue().isEmpty())
			{
				iterator.remove();
			}
		}
	}

	public PluginCommand getAlternative(final String label)
	{
		final List<PluginCommand> alternativeCommands = altcommands.get(label);
		if (alternativeCommands == null || alternativeCommands.isEmpty())
		{
			return null;
		}
		if (alternativeCommands.size() == 1)
		{
			return alternativeCommands.get(0);
		}
		// return the first command that is not an alias
		for (PluginCommand command : alternativeCommands)
		{
			if (command.getName().equalsIgnoreCase(label))
			{
				return command;
			}
		}
		// return the first alias
		return alternativeCommands.get(0);
	}

	public void executed(final String label, final String otherLabel)
	{
		if (getContext().getSettings().isDebug())
		{
			getContext().getLogger().log(Level.INFO, "Essentials: Alternative command {0} found, using {1}", new Object[]
					{
						label, otherLabel
					});
		}
		disabledList.put(label, otherLabel);
	}

	@Override
	public Map<String, String> disabledCommands()
	{
		return disabledList;
	}
}
