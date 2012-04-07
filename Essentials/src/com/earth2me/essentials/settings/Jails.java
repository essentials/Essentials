package com.earth2me.essentials.settings;

import com.earth2me.essentials.storage.StoredLocation;
import com.earth2me.essentials.storage.MapValueType;
import com.earth2me.essentials.storage.StorageObject;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Jails implements StorageObject
{
	@MapValueType(StoredLocation.class)
	private Map<String, StoredLocation> jails = new HashMap<String, StoredLocation>();
}
