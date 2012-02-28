package com.earth2me.essentials.components.settings.users;

import com.earth2me.essentials.storage.*;
import java.util.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Material;


@Data
@EqualsAndHashCode(callSuper = false)
public class UserSurrogate implements IStorageObject
{
	private String nickname;
	private Double money;
	@MapValueType(LocationData.class)
	private Map<String, LocationData> homes = new HashMap<String, LocationData>();
	@ListType(Material.class)
	// TODO Replace with EnumSet.
	@SuppressWarnings("SetReplaceableByEnumSet")
	private Set<Material> unlimited = new HashSet<Material>();
	@MapValueType(List.class)
	@MapKeyType(Material.class)
	// TODO Replace with EnumMap.
	@SuppressWarnings("MapReplaceableByEnumMap")
	private Map<Material, List<String>> powerTools = new HashMap<Material, List<String>>();
	private LocationData lastLocation;
	@MapKeyType(TimestampType.class)
	@MapValueType(Long.class)
	// TODO Replace with EnumMap.
	@SuppressWarnings("MapReplaceableByEnumMap")
	private Map<TimestampType, Long> timestamps = new HashMap<TimestampType, Long>();
	private String jail;
	@ListType
	private List<String> mails;
	private Inventory lastInventory;
	private boolean teleportEnabled;
	@ListType
	private Set<String> ignore;
	private boolean godmode;
	private boolean muted;
	private boolean jailed;
	private Ban ban;
	private String ipAddress;
	private boolean afk;
	private boolean newplayer = true;
	private String geolocation;
	private boolean socialspy;
	private boolean npc;
	private boolean powerToolsEnabled;

	public UserSurrogate()
	{
		unlimited.add(Material.AIR);
		unlimited.add(Material.ARROW);
		unlimited.add(Material.APPLE);
		powerTools.put(Material.DEAD_BUSH, Collections.singletonList("test"));
		timestamps.put(TimestampType.JAIL, Long.MIN_VALUE);
	}

	public boolean hasUnlimited(Material mat)
	{
		return unlimited != null && unlimited.contains(mat);
	}

	public void setUnlimited(Material mat, boolean state)
	{
		if (unlimited.contains(mat))
		{
			unlimited.remove(mat);
		}
		if (state)
		{
			unlimited.add(mat);
		}
	}

	public List<String> getPowertool(Material mat)
	{
		return powerTools == null ? Collections.<String>emptyList() : powerTools.get(mat);
	}


	public boolean hasPowerTools()
	{
		return powerTools != null && !powerTools.isEmpty();
	}

	@SuppressWarnings("MapReplaceableByEnumMap")
	public void setPowertool(Material mat, List<String> commands)
	{
		if (powerTools == null)
		{
			// TODO Replace with EnumMap
			powerTools = new HashMap<Material, List<String>>();
		}
		powerTools.put(mat, commands);
	}

	public void clearAllPowertools()
	{
		powerTools = null;
	}

	public void removeHome(String home)
	{
		if (homes == null)
		{
			return;
		}
		homes.remove(home);
	}
}
