package com.earth2me.essentials.anticheat.data;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.NoCheatPlayerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.entity.Player;


/**
 * Provide secure access to player-specific data objects for various checks or check groups.
 */
public class PlayerManager
{
	// Store data between Events
	private final Map<String, NoCheatPlayerImpl> players;
	private final NoCheat plugin;

	public PlayerManager(NoCheat plugin)
	{
		this.players = new HashMap<String, NoCheatPlayerImpl>();
		this.plugin = plugin;
	}

	/**
	 * Get a data object of the specified class. If none is stored yet, create one.
	 */
	public NoCheatPlayer getPlayer(Player player)
	{

		NoCheatPlayerImpl p = this.players.get(player.getName().toLowerCase());

		if (p == null)
		{
			p = new NoCheatPlayerImpl(player, plugin);
			this.players.put(player.getName().toLowerCase(), p);
		}

		p.setLastUsedTime(System.currentTimeMillis());
		p.refresh(player);

		return p;
	}

	public void cleanDataMap()
	{
		long time = System.currentTimeMillis();
		List<String> removals = new ArrayList<String>(5);

		for (Entry<String, NoCheatPlayerImpl> e : this.players.entrySet())
		{
			if (e.getValue().shouldBeRemoved(time))
			{
				removals.add(e.getKey());
			}
		}

		for (String key : removals)
		{
			this.players.remove(key);
		}
	}

	public Map<String, Object> getPlayerData(String playerName)
	{

		NoCheatPlayer player = this.players.get(playerName.toLowerCase());

		if (player != null)
		{
			return player.getDataStore().collectData();
		}

		return new HashMap<String, Object>();
	}
}
