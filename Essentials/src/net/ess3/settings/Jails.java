package net.ess3.settings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.*;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;
import net.ess3.storage.StoredLocation;
import org.bukkit.Location;


@Data
@EqualsAndHashCode(callSuper = false)
public class Jails implements StorageObject
{
	@MapValueType(StoredLocation.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<String, StoredLocation> jails;

	public Map<String, StoredLocation> getJails()
	{
		return jails == null ? Collections.<String, StoredLocation>emptyMap() : Collections.unmodifiableMap(jails);
	}

	public void addJail(String name, Location loc)
	{
		Map<String, StoredLocation> jailMap = new HashMap<String, StoredLocation>(getJails());
		jailMap.put(name, new StoredLocation(loc));
		jails = jailMap;
	}

	public void removeJail(String name)
	{
		Map<String, StoredLocation> jailMap = new HashMap<String, StoredLocation>(getJails());
		jailMap.remove(name);
		jails = jailMap;
	}
}
