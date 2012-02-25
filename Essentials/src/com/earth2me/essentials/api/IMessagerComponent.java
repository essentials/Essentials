package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.users.IUser;
import org.bukkit.entity.Player;


/**
 * Provides an interface for the transportation of messages.
 */
public interface IMessagerComponent extends IComponent
{
	int broadcastMessage(IUser sender, String message);

	void alert(final Player user, final String item, final String type, IPermissions permission);
}
