package net.ess3.settings;

import net.ess3.storage.Location;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Jails implements StorageObject
{
	@MapValueType(Location.class)
	private Map<String, Location> jails = new HashMap<String, Location>();
}
