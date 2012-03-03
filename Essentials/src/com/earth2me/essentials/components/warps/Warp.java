package com.earth2me.essentials.components.warps;

import com.earth2me.essentials.storage.IStorageObject;
import com.earth2me.essentials.storage.LocationData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Warp implements IStorageObject
{
	private String name;
	private LocationData location;
}
