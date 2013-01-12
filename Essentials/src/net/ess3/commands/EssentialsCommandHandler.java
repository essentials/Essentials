package net.ess3.commands;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.ess3.I18n._;
import net.ess3.api.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class EssentialsCommandHandler implements ICommandHandler, TabExecutor
{
	private final transient ClassLoader classLoader;
	private final transient String commandPath;
	private final transient String permissionPrefix;// TODO: Needed?
	private final transient IEssentialsModule module;
	private static final transient Logger LOGGER = Bukkit.getLogger();
	private final transient Map<String, List<PluginCommand>> altcommands = new HashMap<String, List<PluginCommand>>();
	private final transient Map<String, String> disabledList = new HashMap<String, String>();
	private final transient Map<String, IEssentialsCommand> commands = new HashMap<String, IEssentialsCommand>();
	private final transient IEssentials ess;

	public EssentialsCommandHandler(ClassLoader classLoader, String commandPath, String permissionPrefix, IEssentials ess)
	{
		this(classLoader, commandPath, permissionPrefix, null, ess);
	}

	public EssentialsCommandHandler(ClassLoader classLoader, String commandPath, String permissionPrefix, IEssentialsModule module, IEssentials ess)
	{
		this.classLoader = classLoader;
		this.commandPath = commandPath;
		this.permissionPrefix = permissionPrefix;
		this.module = module;
		this.ess = ess;
		for (Plugin plugin : ess.getServer().getPluginManager().getPlugins())
		{
			if (plugin.isEnabled())
			{
				addPlugin(plugin);
			}
		}
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args)
	{
		ISettings settings = ess.getSettings();

		boolean disabled = settings.getData().getCommands().isDisabled(command.getName());
		boolean overridden = !disabled || settings.getData().getCommands().isOverridden(command.getName());

		// TODO: Move this stuff to bukkit workarounds
		// Allow plugins to override the command via onCommand
		if (!overridden && (!commandLabel.startsWith("e") || commandLabel.equalsIgnoreCase(command.getName())))
		{
			final PluginCommand pc = getAlternative(commandLabel);
			if (pc != null)
			{

				executed(commandLabel, pc);
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
						if (e.getClassName().equals("net.ess3.Essentials"))
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
			// Check for disabled commands
			if (disabled)
			{
				return true;
			}

			final String commandName = command.getName().toLowerCase(Locale.ENGLISH); // TODO: Isn't this just the commandLable
			IEssentialsCommand cmd = commands.get(commandName);
			if (cmd == null)
			{
				try
				{
					cmd = (IEssentialsCommand)classLoader.loadClass(commandPath + commandName).newInstance();
					cmd.init(ess, commandName);
					cmd.setEssentialsModule(module);
					commands.put(commandName, cmd);
					if (command instanceof PluginCommand)
					{
						((PluginCommand)command).setExecutor(this);
					}
				}
				catch (Exception ex)
				{
					sender.sendMessage(_("commandNotLoaded", commandName));
					LOGGER.log(Level.SEVERE, _("commandNotLoaded", commandName), ex);
					return true;
				}
			}

			// Check authorization
			if (sender != null && !cmd.isAuthorized(sender))
			{
				LOGGER.log(Level.WARNING, _("deniedAccessCommand", sender.getName()));
				sender.sendMessage(_("noAccessCommand"));
				return true;
			}

			IUser user = (sender instanceof Player) ? ess.getUserMap().getUser((Player)sender) : null;
			// Run the command
			try
			{
				if (user == null)
				{
					cmd.run(sender, command, commandLabel, args);
				}
				else
				{
					user.setPlayerCache((Player)sender);
					try
					{
						cmd.run(user, command, commandLabel, args);
					}
					finally
					{
						user.setPlayerCache(null);
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
			LOGGER.log(Level.SEVERE, _("commandFailed", commandLabel), ex);
			return true;
		}
	}

	//TODO: Clean this up, since both methods have a lot in common.
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		ISettings settings = ess.getSettings();

		boolean disabled = settings.getData().getCommands().isDisabled(command.getName());
		boolean overridden = !disabled || settings.getData().getCommands().isOverridden(command.getName());

		// TODO: Move this stuff to bukkit workarounds
		// Allow plugins to override the command via onCommand
		if (!overridden && (!commandLabel.startsWith("e") || commandLabel.equalsIgnoreCase(command.getName())))
		{
			final PluginCommand pc = getAlternative(commandLabel);
			if (pc != null)
			{

				executed(commandLabel, pc);
				try
				{
					return pc.tabComplete(sender, commandLabel, args);
				}
				catch (final Exception ex)
				{
					final ArrayList<StackTraceElement> elements = new ArrayList<StackTraceElement>(Arrays.asList(ex.getStackTrace()));
					elements.remove(0);
					final ArrayList<StackTraceElement> toRemove = new ArrayList<StackTraceElement>();
					for (final StackTraceElement e : elements)
					{
						if (e.getClassName().equals("net.ess3.Essentials"))
						{
							toRemove.add(e);
						}
					}
					elements.removeAll(toRemove);
					final StackTraceElement[] trace = elements.toArray(new StackTraceElement[elements.size()]);
					ex.setStackTrace(trace);
					ex.printStackTrace();
					sender.sendMessage(ChatColor.RED + "An internal error occurred while attempting to perform this command");
					return null;
				}
			}
		}

		try
		{
			// Check for disabled commands
			if (disabled)
			{
				return null;
			}

			final String commandName = command.getName().toLowerCase(Locale.ENGLISH);
			IEssentialsCommand cmd = commands.get(commandName);
			if (cmd == null)
			{
				try
				{
					cmd = (IEssentialsCommand)classLoader.loadClass(commandPath + commandName).newInstance();
					cmd.init(ess, commandName);
					cmd.setEssentialsModule(module);
					commands.put(commandName, cmd);
					if (command instanceof PluginCommand)
					{
						((PluginCommand)command).setExecutor(this);
					}
				}
				catch (Exception ex)
				{
					sender.sendMessage(_("commandNotLoaded", commandName));
					LOGGER.log(Level.SEVERE, _("commandNotLoaded", commandName), ex);
					return null;
				}
			}

			// Check authorization
			if (sender != null && !cmd.isAuthorized(sender))
			{
				LOGGER.log(Level.WARNING, _("deniedAccessCommand", sender.getName()));
				sender.sendMessage(_("noAccessCommand"));
				return null;
			}

			IUser user = (sender instanceof Player) ? ess.getUserMap().getUser((Player)sender) : null;
			// Run the command
			try
			{
				if (user == null)
				{
					return cmd.tabComplete(sender, command, commandLabel, args);
				}
				else
				{
					user.setPlayerCache((Player)sender);
					try
					{
						return cmd.tabComplete(user, command, commandLabel, args);
					}
					finally
					{
						user.setPlayerCache(null);
					}
				}
			}
			catch (Throwable ex)
			{
				showCommandError(sender, commandLabel, ex);
				return null;
			}
		}
		catch (Throwable ex)
		{
			LOGGER.log(Level.SEVERE, _("commandFailed", commandLabel), ex);
			return null;
		}
	}

	@Override
	public void showCommandError(final CommandSender sender, final String commandLabel, final Throwable exception)
	{
		sender.sendMessage(_("errorWithMessage", exception.getMessage()));
		if (ess.getSettings().isDebug())
		{
			LOGGER.log(Level.WARNING, _("errorCallingCommand", commandLabel), exception);
		}
	}

	@Override
	public void onReload()
	{
	}

	//TODO: Move this stuff to bukkit workarounds
	@Override
	public final void addPlugin(final Plugin plugin)
	{
		if (plugin.getDescription().getMain().contains("net.ess3.essentials"))
		{
			return;
		}
		final List<Command> pcommands = PluginCommandYamlParser.parse(plugin);
		final String pluginName = plugin.getDescription().getName().toLowerCase(Locale.ENGLISH);

		for (Command command : pcommands)
		{
			final PluginCommand pc = (PluginCommand)command;
			final List<String> labels = new ArrayList<String>(pc.getAliases());
			labels.add(pc.getName());

			PluginCommand reg = ess.getServer().getPluginCommand(pluginName + ":" + pc.getName().toLowerCase(Locale.ENGLISH));
			if (reg == null)
			{
				reg = ess.getServer().getPluginCommand(pc.getName().toLowerCase(Locale.ENGLISH));
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
		final List<PluginCommand> pcommands = altcommands.get(label);
		if (pcommands == null || pcommands.isEmpty())
		{
			return null;
		}
		if (pcommands.size() == 1)
		{
			return pcommands.get(0);
		}
		// return the first command that is not an alias
		for (PluginCommand command : pcommands)
		{
			if (command.getName().equalsIgnoreCase(label))
			{
				return command;
			}
		}
		// return the first alias
		return pcommands.get(0);
	}

	public void executed(final String label, final PluginCommand pc)
	{
		final String altString = pc.getPlugin().getName() + ":" + pc.getLabel();
		if (ess.getSettings().isDebug())
		{
			LOGGER.log(Level.INFO, "Essentials: Alternative command " + label + " found, using " + altString);
		}
		disabledList.put(label, altString);
	}

	@Override
	public Map<String, String> disabledCommands()
	{
		return disabledList;
	}
}
