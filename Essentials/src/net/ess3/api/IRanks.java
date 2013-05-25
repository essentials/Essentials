package net.ess3.api;

import java.text.MessageFormat;
import org.bukkit.command.CommandSender;


/**
 * CommandSender object can be either IUser or Player
 */
public interface IRanks
{
	/**
	 *
	 * @param player
	 * @return
	 */
	String getMainGroup(CommandSender player);

	/**
	 *
	 * @param player
	 * @param groupname
	 * @return
	 */
	boolean inGroup(CommandSender player, String groupname);

	/**
	 *
	 * @param player
	 * @return
	 */
	double getHealCooldown(CommandSender player);

	/**
	 *
	 * @param player
	 * @return
	 */
	double getTeleportCooldown(CommandSender player);

	/**
	 *
	 * @param player
	 * @return
	 */
	double getTeleportDelay(CommandSender player);

	/**
	 *
	 * @param player
	 * @return
	 */
	String getPrefix(CommandSender player);

	/**
	 *
	 * @param player
	 * @return
	 */
	String getSuffix(CommandSender player);

	/**
	 *
	 * @param player
	 * @return
	 */
	int getHomeLimit(CommandSender player);

	/**
	 *
	 * @param player
	 * @return
	 */
	MessageFormat getChatFormat(CommandSender player);
}
