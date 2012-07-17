package net.ess3.settings;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;
import net.ess3.storage.StoredLocation;


@Data
@EqualsAndHashCode(callSuper = false)
public class Jails implements StorageObject
{
	@MapValueType(StoredLocation.class)
	private Map<String, StoredLocation> jails = new HashMap<String, StoredLocation>();
}
