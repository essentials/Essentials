package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import static org.mcess.essentials.I18n.tl;
import org.mcess.essentials.utils.FormatUtil;
import org.bukkit.Server;
import org.bukkit.entity.Player;


public class Commandkickall extends EssentialsCommand
{
	public Commandkickall()
	{
		super("kickall");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		String kickReason = args.length > 0 ? getFinalArg(args, 0) : tl("kickDefault");
		kickReason = FormatUtil.replaceFormat(kickReason.replace("\\n", "\n").replace("|", "\n"));

		for (Player onlinePlayer : ess.getOnlinePlayers())
		{
			if (!sender.isPlayer() || !onlinePlayer.getName().equalsIgnoreCase(sender.getPlayer().getName()))
			{
				onlinePlayer.kickPlayer(kickReason);
			}
		}
		sender.sendMessage(tl("kickedAll"));
	}
}
