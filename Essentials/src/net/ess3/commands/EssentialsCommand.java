package net.ess3.commands;

import java.util.List;
import java.util.logging.Logger;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IEssentialsModule;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.AbstractSuperpermsPermission;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public abstract class EssentialsCommand extends AbstractSuperpermsPermission implements IEssentialsCommand
{
	protected transient String commandName;
	protected transient IEssentials ess;
	protected transient IEssentialsModule module;
	protected transient Server server;
	protected transient Logger logger;
	private transient String permission;

	@Override
	public void init(final IEssentials ess, final String commandName)
	{
		this.ess = ess;
		this.logger = ess.getLogger();
		this.server = ess.getServer();
		this.commandName = commandName;
		this.permission = "essentials." + commandName;
	}

	@Override
	public void setEssentialsModule(final IEssentialsModule module)
	{
		this.module = module;
	}

	@Override
	public final void run(final IUser user, final Command cmd, final String commandLabel, final String[] args) throws Exception
	{
		final Trade charge = new Trade(commandName, ess);
		charge.isAffordableFor(user);
		run(user, commandLabel, args);
		charge.charge(user);
	}

	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		run((CommandSender)user, commandLabel, args);
	}

	@Override
	public final void run(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) throws Exception
	{
		run(sender, commandLabel, args);
	}

	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		throw new Exception(_("onlyPlayers", commandName));
	}

	@Override
	public final List<String> tabComplete(final IUser user, final Command cmd, final String commandLabel, final String[] args)
	{
		return tabComplete(user, commandLabel, args);
	}

	protected List<String> tabComplete(final IUser user, final String commandLabel, final String[] args)
	{
		return tabComplete((CommandSender)user, commandLabel, args);
	}

	@Override
	public final List<String> tabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args)
	{
		return tabComplete(sender, commandLabel, args);
	}

	protected List<String> tabComplete(final CommandSender sender, final String commandLabel, final String[] args)
	{
		return null;
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
	public String getPermissionName()
	{
		return permission;
	}

	protected boolean isUser(CommandSender sender)
	{
		return sender instanceof IUser;
	}

	/**
	 * Converts a CommandSender object to an User.
	 *
	 * @param sender
	 * @return
	 * @throws IllegalArgumentException if the object is neither a superclass of IUser or Player
	 */
	protected IUser getUser(CommandSender sender)
	{
		if (sender instanceof IUser)
		{
			return (IUser)sender;
		}
		if (sender instanceof Player)
		{
			return ess.getUserMap().getUser((Player)sender);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Converts a CommandSender object to an User.
	 *
	 * @param sender
	 * @return
	 * @throws IllegalArgumentException if the object is not a superclass of IUser
	 */
	protected Player getPlayer(CommandSender sender)
	{
		if (sender instanceof IUser)
		{
			return ((IUser)sender).getPlayer();
		}
		throw new IllegalArgumentException();
	}

	protected Player getPlayerOrNull(CommandSender sender)
	{
		if (sender instanceof IUser)
		{
			return ((IUser)sender).getPlayer();
		}
		return null;
	}
}
