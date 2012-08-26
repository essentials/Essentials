package net.ess3.user;

import java.util.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.*;
import org.bukkit.Material;



@Data
@EqualsAndHashCode(callSuper = false)
public class UserData implements StorageObject
{
	public enum TimestampType
	{
		JAIL, MUTE, LASTHEAL, LASTTELEPORT, LOGIN, LOGOUT, KIT
	}
	private String nickname;
	private Double money;
	@MapValueType(StoredLocation.class)
	private Map<String, StoredLocation> homes = new HashMap<String, StoredLocation>();
	@ListType(Material.class)
	private Set<Material> unlimited = new HashSet<Material>();
	@MapValueType(List.class)
	@MapKeyType(Material.class)
	private Map<Material, List<String>> powerTools = new HashMap<Material, List<String>>();
	private StoredLocation lastLocation;
	@MapKeyType(TimestampType.class)
	@MapValueType(Long.class)
	private Map<TimestampType, Long> timestamps = new HashMap<TimestampType, Long>();
	private String jail;
	@ListType
	private List<String> mails;
	private Inventory inventory;
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

	public UserData()
	{
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
