package net.ess3.xmpp;

import java.util.List;
import net.ess3.api.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;


public interface IEssentialsXMPP extends Plugin
{
	/**
	 *
	 * @param user
	 * @return
	 */
	String getAddress(final CommandSender user);

	/**
	 *
	 * @param name
	 * @return
	 */
	String getAddress(final String name);

	/**
	 *
	 * @return
	 */
	List<String> getSpyUsers();

	/**
	 *
	 * @param address
	 * @return
	 */
	IUser getUserByAddress(final String address);

	/**
	 *
	 * @param user
	 * @param message
	 * @return
	 */
	boolean sendMessage(final CommandSender user, final String message);

	/**
	 *
	 * @param address
	 * @param message
	 * @return
	 */
	boolean sendMessage(final String address, final String message);

	/**
	 *
	 * @param user
	 * @param address
	 */
	void setAddress(final CommandSender user, final String address);

	/**
	 *
	 * @param user
	 * @return
	 */
	boolean toggleSpy(final CommandSender user);

	/**
	 *
	 * @param sender
	 * @param message
	 * @param xmppAddress
	 */
	void broadcastMessage(final IUser sender, final String message, final String xmppAddress);
}
