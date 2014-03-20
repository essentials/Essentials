package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n.tl_;
import com.earth2me.essentials.OfflinePlayer;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.logging.Level;
import org.bukkit.Server;


public class Commandban extends EssentialsCommand
{
	public Commandban()
	{
		super("ban");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		boolean nomatch = false;
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		User user;
		try
		{
			user = getPlayer(server, args, 0, true, true);
		}
		catch (PlayerNotFoundException e)
		{
			nomatch = true;
			user = ess.getUser(new OfflinePlayer(args[0], ess));
		}
		if (!user.isOnline())
		{
			if (sender.isPlayer() && !ess.getUser(sender.getPlayer()).isAuthorized("essentials.ban.offline"))
			{
				throw new Exception(tl_("banExemptOffline"));
			}
		}
		else
		{
			if (user.isAuthorized("essentials.ban.exempt") && sender.isPlayer())
			{
				throw new Exception(tl_("banExempt"));
			}
		}

		final String senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : Console.NAME;
		String banReason;
		if (args.length > 1)
		{
			banReason = FormatUtil.replaceFormat(getFinalArg(args, 1).replace("\\n", "\n").replace("|", "\n"));
		}
		else
		{
			banReason = tl_("defaultBanReason");
		}

		user.setBanReason(tl_("banFormat", banReason, senderName));
		user.setBanned(true);
		user.setBanTimeout(0);
		user.kickPlayer(tl_("banFormat", banReason, senderName));

		server.getLogger().log(Level.INFO, tl_("playerBanned", senderName, user.getName(), banReason));

		if (nomatch)
		{
			sender.sendMessage(tl_("userUnknown", user.getName()));
		}

		ess.broadcastMessage("essentials.ban.notify", tl_("playerBanned", senderName, user.getName(), banReason));
	}
}
