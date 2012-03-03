package com.earth2me.essentials.components.users;

import com.earth2me.essentials.storage.*;
import java.util.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Material;


@Data
@EqualsAndHashCode(callSuper = false)
public class UserSurrogate implements IStorageObject
{
	private String nickName;
	private Double money;
	@MapValueType(LocationData.class)
	private Map<String, LocationData> homes = new HashMap<String, LocationData>();
	@ListType(Material.class)
	@SuppressWarnings("SetReplaceableByEnumSet")
	private Set<Material> unlimited = new HashSet<Material>();
	@MapValueType(List.class)
	@MapKeyType(Material.class)
	@SuppressWarnings("MapReplaceableByEnumMap")
	private Map<Material, List<String>> powerTools = new HashMap<Material, List<String>>();
	private LocationData lastLocation;
	@MapKeyType(TimeStampType.class)
	@MapValueType(Long.class)
	@SuppressWarnings("MapReplaceableByEnumMap")
	private Map<TimeStampType, Long> timeStamps = new HashMap<TimeStampType, Long>();
	private String jail;
	@ListType
	private List<String> mails;
	private Inventory lastInventory;
	private boolean teleportEnabled;
	@ListType
	private Set<String> ignore;
	private boolean godModeEnabled;
	private boolean muted;
	private boolean jailed;
	private Ban ban;
	private String ipAddress;
	private boolean afk;
	private boolean newPlayer = true;
	private String geoLocation;
	private boolean socialSpy;
	private boolean npc;
	private boolean powerToolsEnabled;
	private String xmppAddress;
	private boolean xmppSpy = false;

	public UserSurrogate()
	{
		unlimited.add(Material.AIR);
		unlimited.add(Material.ARROW);
		unlimited.add(Material.APPLE);
		powerTools.put(Material.DEAD_BUSH, Collections.singletonList("test"));
		timeStamps.put(TimeStampType.JAIL, Long.MIN_VALUE);
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

	public List<String> getPowerTool(Material mat)
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
