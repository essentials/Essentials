package net.ess3.commands;

import java.util.Set;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData.TimestampType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandheal extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{

		if (args.length > 0 && Permissions.HEAL_OTHERS.isAuthorized(user))
		{
			user.checkCooldown(TimestampType.LASTHEAL, ess.getRanks().getHealCooldown(user), true, Permissions.HEAL_COOLDOWN_BYPASS);

			healOtherPlayers(user, args[0]);
			return;
		}

		user.checkCooldown(TimestampType.LASTHEAL, ess.getRanks().getHealCooldown(user), true, Permissions.HEAL_COOLDOWN_BYPASS);
		
		final Player player = user.getPlayer();
		player.setHealth(20);
		player.setFireTicks(0);
		player.setFoodLevel(20);
		user.sendMessage(_("heal"));
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		healOtherPlayers(sender, args[0]);
	}

	private void healOtherPlayers(final CommandSender sender, final String name)
	{
		final Set<IUser> users = ess.getUserMap().matchUsersExcludingHidden(name, getPlayerOrNull(sender));
		if (users.isEmpty())
		{
			sender.sendMessage(_("playerNotFound"));
			return;
		}
		for (IUser p : users)
		{	
			final Player player = p.getPlayer();
			player.setHealth(20);
			player.setFoodLevel(20);
			player.sendMessage(_("heal"));
			sender.sendMessage(_("healOther", player.getDisplayName()));
		}
	}
}
