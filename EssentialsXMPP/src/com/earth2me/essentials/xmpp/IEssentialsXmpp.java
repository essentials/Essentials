package com.earth2me.essentials.xmpp;

import com.earth2me.essentials.api.IEssentialsPlugin;
import com.earth2me.essentials.components.settings.users.IUserComponent;
import java.util.List;
import org.bukkit.entity.Player;


public interface IEssentialsXmpp extends IEssentialsPlugin
{
	String getAddress(final Player user);

	String getAddress(final String name);

	List<String> getSpyUsers();

	IUserComponent getUserByAddress(final String address);

	boolean sendMessage(final Player user, final String message);

	boolean sendMessage(final String address, final String message);

	void setAddress(final Player user, final String address);

	boolean toggleSpy(final Player user);

	void broadcastMessage(final IUserComponent sender, final String message, final String xmppAddress);
}
