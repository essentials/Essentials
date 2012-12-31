package net.ess3.user;

import java.util.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.ess3.storage.*;
import org.bukkit.Location;
import org.bukkit.Material;


@Data
@EqualsAndHashCode(callSuper = false)
public class UserData implements StorageObject
{
	public enum TimestampType
	{
		JAIL, MUTE, LASTHEAL, LASTTELEPORT, LOGIN, LOGOUT, KIT, COMMAND
	}
	private String nickname;
	private Double money;
	@MapValueType(StoredLocation.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<String, StoredLocation> homes;

	public Map<String, StoredLocation> getHomes()
	{
		return homes == null
			   ? Collections.<String, StoredLocation>emptyMap()
			   : Collections.unmodifiableMap(homes);
	}
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> unlimited;

	public Set<Material> getUnlimited()
	{
		return unlimited == null
			   ? Collections.<Material>emptySet()
			   : Collections.unmodifiableSet(unlimited);
	}
	@MapValueType(List.class)
	@MapKeyType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<Material, List<String>> powerTools;

	public Map<Material, List<String>> getPowerTools()
	{
		return powerTools == null
			   ? Collections.<Material, List<String>>emptyMap()
			   : Collections.unmodifiableMap(powerTools);
	}
	private StoredLocation lastLocation;
	@MapKeyType(String.class)
	@MapValueType(Long.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<String, Long> timestamps = new HashMap<String, Long>();

	public Map<String, Long> getTimestamps()
	{
		return timestamps == null
			   ? Collections.<String, Long>emptyMap()
			   : Collections.unmodifiableMap(timestamps);
	}
	private String jail;
	@ListType
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private List<String> mails;

	public List<String> getMails()
	{
		return mails == null
			   ? Collections.<String>emptyList()
			   : Collections.unmodifiableList(mails);
	}
	private Inventory inventory;
	private boolean teleportEnabled;
	@ListType
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<String> ignore;

	public Set<String> getIgnore()
	{
		return ignore == null
			   ? Collections.<String>emptySet()
			   : Collections.unmodifiableSet(ignore);
	}
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
	private boolean balancetopHide = false;

	public UserData()
	{
		timestamps.put(TimestampType.JAIL.name(), Long.MIN_VALUE);
	}

	public long getTimestamp(TimestampType type)
	{
		final Long val = getTimestamps().get(type.name());
		return val == null ? 0 : val;
	}

	public long getTimestamp(TimestampType type, String subType)
	{
		final Long val = getTimestamps().get(type.name() + "-" + subType.toUpperCase(Locale.ENGLISH));
		return val == null ? 0 : val;
	}

	public void setTimestamp(TimestampType type, Long value)
	{
		final Map<String, Long> ts = new HashMap<String, Long>(getTimestamps());
		ts.put(type.name(), value);
		timestamps = ts;
	}

	public void setTimestamp(TimestampType type, String subType, Long value)
	{
		final Map<String, Long> ts = new HashMap<String, Long>(getTimestamps());
		ts.put(type.name() + "-" + subType.toUpperCase(Locale.ENGLISH), value);
		timestamps = ts;
	}

	public boolean hasUnlimited(Material mat)
	{
		return unlimited != null && unlimited.contains(mat);
	}

	public void setUnlimited(Material mat, boolean state)
	{
		if (!state && unlimited.contains(mat))
		{
			final Set<Material> unlimitedSet = new HashSet<Material>(getUnlimited());
			unlimitedSet.remove(mat);
			unlimited = unlimitedSet;
		}
		if (state && !unlimited.contains(mat))
		{
			final Set<Material> unlimitedSet = new HashSet<Material>(getUnlimited());
			unlimitedSet.add(mat);
			unlimited = unlimitedSet;
		}
	}

	public List<String> getPowertool(Material mat)
	{
		return powerTools == null ? Collections.<String>emptyList() : Collections.unmodifiableList(powerTools.get(mat));
	}

	public boolean hasPowerTools()
	{
		return powerTools != null && !powerTools.isEmpty();
	}

	public void setPowertool(Material mat, List<String> commands)
	{
		final Map<Material, List<String>> powerToolMap = new HashMap<Material, List<String>>(getPowerTools());
		powerToolMap.put(mat, commands);
		powerTools = powerToolMap;
	}

	public void clearAllPowertools()
	{
		powerTools = null;
	}

	public void addHome(String name, Location location)
	{
		final Map<String, StoredLocation> homeMap = new HashMap<String, StoredLocation>(getHomes());
		homeMap.put(name, new StoredLocation(location));
		homes = homeMap;
	}

	public void addHome(String name, StoredLocation location)
	{
		final Map<String, StoredLocation> homeMap = new HashMap<String, StoredLocation>(getHomes());
		homeMap.put(name, location);
		homes = homeMap;
	}

	public void removeHome(String home)
	{
		if (homes == null || !homes.containsKey(home))
		{
			return;
		}
		final Map<String, StoredLocation> homeMap = new HashMap<String, StoredLocation>(getHomes());
		homeMap.remove(home);
		homes = homeMap;
	}

	public void addMail(String mail)
	{
		final List<String> mailList = new ArrayList<String>(getMails());
		mailList.add(mail);
		mails = mailList;
	}

	public void clearMails()
	{
		mails = null;
	}

	public void setIgnore(String name, boolean state)
	{
		if (state && !ignore.contains(name))
		{
			final Set<String> ignoreSet = new HashSet<String>(getIgnore());
			ignoreSet.add(name);
			ignore = ignoreSet;
		}
		if (!state && ignore.contains(name))
		{
			final Set<String> ignoreSet = new HashSet<String>(getIgnore());
			ignoreSet.remove(name);
			ignore = ignoreSet;
		}
	}
}
