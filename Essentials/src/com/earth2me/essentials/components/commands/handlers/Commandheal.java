package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.Permissions;
import com.earth2me.essentials.components.users.UserData.TimestampType;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandheal extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{

		if (args.length > 0 && Permissions.HEAL_OTHERS.isAuthorized(user))
		{
			user.checkCooldown(TimestampType.LASTHEAL, getContext().getGroups().getHealCooldown(user), true, Permissions.HEAL_COOLDOWN_BYPASS);

			healOtherPlayers(user, args[0]);
			return;
		}

		user.checkCooldown(TimestampType.LASTHEAL, getContext().getGroups().getHealCooldown(user), true, Permissions.HEAL_COOLDOWN_BYPASS);

		user.setHealth(20);
		user.setFoodLevel(20);
		user.sendMessage(_("heal"));
	}

	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		healOtherPlayers(sender, args[0]);
	}

	private void healOtherPlayers(final CommandSender sender, final String name)
	{
		final List<Player> players = getServer().matchPlayer(name);
		if (players.isEmpty())
		{
			sender.sendMessage(_("playerNotFound"));
			return;
		}
		for (Player p : players)
		{
			if (getContext().getUser(p).isHidden())
			{
				continue;
			}
			p.setHealth(20);
			p.setFoodLevel(20);
			p.sendMessage(_("heal"));
			sender.sendMessage(_("healOther", p.getDisplayName()));
		}
	}
}
