package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.Permissions;
import java.util.logging.Level;
import org.bukkit.entity.Player;


public class Commandhelpop extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final String message = _("helpOp", user.getDisplayName(), Util.stripColor(getFinalArg(args, 0)));
		getLogger().log(Level.INFO, message);
		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			final IUser player = getContext().getUser(onlinePlayer);
			if (!Permissions.HELPOP_RECEIVE.isAuthorized(player))
			{
				continue;
			}
			player.sendMessage(message);
		}
	}
}
