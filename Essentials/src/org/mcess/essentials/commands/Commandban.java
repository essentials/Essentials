package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.Console;
import org.mcess.essentials.OfflinePlayer;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.FormatUtil;
import java.util.logging.Level;
import org.bukkit.BanList;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


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
			user = ess.getUser(new OfflinePlayer(args[0], ess.getServer()));
		}
		if (!user.getBase().isOnline())
		{
			if (sender.isPlayer() && !ess.getUser(sender.getPlayer()).isAuthorized("essentials.ban.offline"))
			{
				throw new Exception(I18n.tl("banExemptOffline"));
			}
		}
		else
		{
			if (user.isAuthorized("essentials.ban.exempt") && sender.isPlayer())
			{
				throw new Exception(I18n.tl("banExempt"));
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
			banReason = I18n.tl("defaultBanReason");
		}
		
		ess.getServer().getBanList(BanList.Type.NAME).addBan(user.getName(), banReason, null, senderName);

		String banDisplay = I18n.tl("banFormat", banReason, senderName);
		
		user.getBase().kickPlayer(banDisplay);
		server.getLogger().log(Level.INFO, I18n.tl("playerBanned", senderName, user.getName(), banDisplay));

		if (nomatch)
		{
			sender.sendMessage(I18n.tl("userUnknown", user.getName()));
		}

		ess.broadcastMessage("essentials.ban.notify", I18n.tl("playerBanned", senderName, user.getName(), banReason));
	}
}
