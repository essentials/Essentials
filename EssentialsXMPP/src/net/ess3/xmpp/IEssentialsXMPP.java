package net.ess3.xmpp;

import java.util.List;
import net.ess3.api.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;


public interface IEssentialsXMPP extends Plugin
{
	String getAddress(final CommandSender user);

	String getAddress(final String name);

	List<String> getSpyUsers();

	IUser getUserByAddress(final String address);

	boolean sendMessage(final CommandSender user, final String message);

	boolean sendMessage(final String address, final String message);

	void setAddress(final CommandSender user, final String address);

	boolean toggleSpy(final CommandSender user);

	void broadcastMessage(final IUser sender, final String message, final String xmppAddress);
}
