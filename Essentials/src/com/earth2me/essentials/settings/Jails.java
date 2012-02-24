package com.earth2me.essentials.settings;

import com.earth2me.essentials.storage.Location;
import com.earth2me.essentials.storage.MapValueType;
import com.earth2me.essentials.storage.IStorageObject;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Jails implements IStorageObject
{
	@MapValueType(Location.class)
	private Map<String, Location> jails = new HashMap<String, Location>();
}
