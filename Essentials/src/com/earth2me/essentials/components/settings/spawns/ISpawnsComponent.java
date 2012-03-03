package com.earth2me.essentials.components.settings.spawns;

import com.earth2me.essentials.components.settings.Spawns;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.storage.IStorageComponent;
import org.bukkit.Location;
import org.bukkit.event.EventPriority;


public interface ISpawnsComponent extends IStorageComponent<Spawns>
{
	String getAnnounceNewPlayerFormat(IUserComponent user);

	boolean getAnnounceNewPlayers();

	Location getNewbieSpawn();

	EventPriority getRespawnPriority();

	Location getSpawn(final String group);

	void setSpawn(final Location loc, final String group);

}
