package com.earth2me.essentials.api;

import com.earth2me.essentials.components.users.IUser;
import java.text.MessageFormat;


public interface IGroupsComponent
{
	String getMainGroup(IUser player);

	boolean isInGroup(IUser player, String groupname);

	double getHealCooldown(IUser player);

	double getTeleportCooldown(IUser player);

	double getTeleportDelay(IUser player);

	String getPrefix(IUser player);

	String getSuffix(IUser player);

	int getHomeLimit(IUser player);

	MessageFormat getChatFormat(IUser player);
}
