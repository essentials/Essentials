package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.users.IUser;


/**
 * Provides an interface for the transportation of messages.
 */
public interface IMessagerComponent extends IComponent
{
	int broadcastMessage(IUser sender, String message);
}
