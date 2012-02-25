package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.settings.groups.Groups;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.storage.IStorageObjectHolder;
import java.text.MessageFormat;


public interface IGroupsComponent extends IComponent, IStorageObjectHolder<Groups>
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
