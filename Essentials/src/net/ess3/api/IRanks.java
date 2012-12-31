package net.ess3.api;

import java.text.MessageFormat;
import org.bukkit.command.CommandSender;


/**
 * CommandSender object can be either IUser or Player
 */
public interface IRanks
{
	String getMainGroup(CommandSender player);

	boolean inGroup(CommandSender player, String groupname);

	double getHealCooldown(CommandSender player);

	double getTeleportCooldown(CommandSender player);

	double getTeleportDelay(CommandSender player);

	String getPrefix(CommandSender player);

	String getSuffix(CommandSender player);

	int getHomeLimit(CommandSender player);

	MessageFormat getChatFormat(CommandSender player);
}
