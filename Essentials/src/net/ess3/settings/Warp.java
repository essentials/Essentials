package net.ess3.settings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.StoredLocation;
import net.ess3.storage.StorageObject;

@Data
@EqualsAndHashCode(callSuper = false)
public class Warp implements StorageObject
{
	private String name;
	private StoredLocation location;
}
