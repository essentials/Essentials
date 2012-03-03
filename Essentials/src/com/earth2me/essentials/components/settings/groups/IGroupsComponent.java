package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.users.IUserComponent;
import java.text.MessageFormat;


public interface IGroupsComponent extends IComponent
{
	String getMainGroup(IUserComponent player);

	boolean isInGroup(IUserComponent player, String groupname);

	double getHealCooldown(IUserComponent player);

	double getTeleportCooldown(IUserComponent player);

	double getTeleportDelay(IUserComponent player);

	String getPrefix(IUserComponent player);

	String getSuffix(IUserComponent player);

	int getHomeLimit(IUserComponent player);

	MessageFormat getChatFormat(IUserComponent player);
}
