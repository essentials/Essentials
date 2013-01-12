package net.ess3.commands;

import java.util.List;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandclearinventory extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && Permissions.CLEARINVENTORY_OTHERS.isAuthorized(user))
		{
			IUser p = ess.getUserMap().matchUserExcludingHidden(args[0], getPlayerOrNull(user));
			if (p != null)
			{
				final Player player = p.getPlayer();
				player.getInventory().clear();
				user.sendMessage(_("inventoryClearedOthers", player.getDisplayName()));
			}
			else
			{
				throw new Exception(_("playerNotFound"));
			}
		}
		else
		{
			user.getPlayer().getInventory().clear();
			user.sendMessage(_("inventoryCleared"));
		}
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if (args[0].length() >= 3)
		{
			List<Player> online = server.matchPlayer(args[0]);

			if (!online.isEmpty())
			{
				for (Player p : online)
				{
					p.getInventory().clear();
					sender.sendMessage(_("inventoryClearedOthers", p.getDisplayName()));
				}
				return;
			}
			throw new Exception(_("playerNotFound"));
		}
		else
		{
			final Player u = server.getPlayer(args[0]);
			if (u != null)
			{
				u.getInventory().clear();
				sender.sendMessage(_("inventoryClearedOthers", u.getDisplayName()));
			}
			else
			{
				throw new Exception(_("playerNotFound"));
			}
		}
	}
}
