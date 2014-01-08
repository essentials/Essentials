package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import static com.earth2me.essentials.commands.EssentialsCommand.getFinalArg;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;


public class Commandstaffchat extends EssentialsCommand
{
	public Commandstaffchat()
	{
		super("staffchat");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		sendBroadcast(sender.getSender().getName(), server, sender, args);
	}

	private void sendBroadcast(final String name, final Server server, final CommandSource sender, final String[] args) throws NotEnoughArgumentsException
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			if (onlinePlayer.hasPermission("essentials.staffchat.read") && onlinePlayer != sender.getPlayer())
			{
				onlinePlayer.sendMessage("§b(Staff)" + name +"§b: " + FormatUtil.replaceFormat(getFinalArg(args, 0)).replace("\\n", "\n"));
			}
		}
		sender.sendMessage("§b(Staff)" + name +"§b: " + FormatUtil.replaceFormat(getFinalArg(args, 0)).replace("\\n", "\n"));
		Bukkit.getLogger().log(Level.INFO, ("(Staff)" + name +": " + FormatUtil.replaceFormat(getFinalArg(args, 0)).replace("\\n", "\n")));
	}
}
