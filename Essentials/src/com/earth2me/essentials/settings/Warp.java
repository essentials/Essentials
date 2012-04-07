package com.earth2me.essentials.settings;

import com.earth2me.essentials.storage.StoredLocation;
import com.earth2me.essentials.storage.StorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Warp implements StorageObject
{
	private String name;
	private StoredLocation location;
}
