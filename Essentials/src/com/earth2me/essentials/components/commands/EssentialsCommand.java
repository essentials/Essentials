package com.earth2me.essentials.components.commands;

import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.IComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.AbstractSuperpermsPermission;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public abstract class EssentialsCommand extends AbstractSuperpermsPermission implements IEssentialsCommand
{
	private transient String commandName;
	private transient IContext context;
	private transient IComponent component;
	private transient Server server;
	@SuppressWarnings("NonConstantLogger")
	private transient Logger logger;
	private transient String permission;

	/**
	 * @return the commandName
	 */
	protected String getCommandName()
	{
		return commandName;
	}

	/**
	 * @param commandName the commandName to set
	 */
	protected void setCommandName(String commandName)
	{
		this.commandName = commandName;
	}

	/**
	 * @return the context
	 */
	protected IContext getContext()
	{
		return context;
	}

	/**
	 * @param context the context to set
	 */
	protected void setContext(IContext context)
	{
		this.context = context;
	}

	/**
	 * @return the component
	 */
	protected IComponent getComponent()
	{
		return component;
	}

	/**
	 * @param component the component to set
	 */
	@Override
	public void setComponent(IComponent component)
	{
		this.component = component;
	}

	/**
	 * @return the server
	 */
	protected Server getServer()
	{
		return server;
	}

	/**
	 * @param server the server to set
	 */
	protected void setServer(Server server)
	{
		this.server = server;
	}

	/**
	 * @return the logger
	 */
	protected Logger getLogger()
	{
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	protected void setLogger(Logger logger)
	{
		this.logger = logger;
	}

	@Override
	public void init(final IContext parent, final String commandName)
	{
		this.setContext(parent);
		this.setLogger(getContext().getLogger());
		this.setServer(getContext().getServer());
		this.setCommandName(commandName);
		this.permission = "essentials." + commandName;
	}

	protected IUser getPlayer(final String[] args, final int pos) throws NoSuchFieldException, NotEnoughArgumentsException
	{
		return getPlayer(args, pos, false);
	}

	protected IUser getPlayer(final String[] args, final int pos, final boolean getOffline) throws NoSuchFieldException, NotEnoughArgumentsException
	{
		if (args.length <= pos)
		{
			throw new NotEnoughArgumentsException();
		}
		if (args[pos].isEmpty())
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
		final IUser user = getContext().getUser(args[pos]);
		if (user != null)
		{
			if (!getOffline && (!user.isOnline() || user.isHidden()))
			{
				throw new NoSuchFieldException(_("playerNotFound"));
			}
			return user;
		}
		final List<Player> matches = getServer().matchPlayer(args[pos]);

		if (!matches.isEmpty())
		{
			for (Player player : matches)
			{
				final IUser userMatch = getContext().getUser(player);
				if (userMatch.getDisplayName().startsWith(args[pos]) && (getOffline || !userMatch.isHidden()))
				{
					return userMatch;
				}
			}
			final IUser userMatch = getContext().getUser(matches.get(0));
			if (getOffline || !userMatch.isHidden())
			{
				return userMatch;
			}
		}
		throw new NoSuchFieldException(_("playerNotFound"));
	}

	@Override
	public final void run(final IUser user, final Command cmd, final String commandLabel, final String[] args) throws Exception
	{
		final Trade charge = new Trade(getCommandName(), getContext());
		charge.isAffordableFor(user);
		run(user, commandLabel, args);
		charge.charge(user);
	}

	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		run((CommandSender)user.getBase(), commandLabel, args);
	}

	@Override
	public final void run(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) throws Exception
	{
		run(sender, commandLabel, args);
	}

	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		throw new Exception(_("onlyPlayers", getCommandName()));
	}

	public static String getFinalArg(final String[] args, final int start)
	{
		final StringBuilder bldr = new StringBuilder();
		for (int i = start; i < args.length; i++)
		{
			if (i != start)
			{
				bldr.append(" ");
			}
			bldr.append(args[i]);
		}
		return bldr.toString();
	}

	@Override
	public String getPermission()
	{
		return permission;
	}
}
