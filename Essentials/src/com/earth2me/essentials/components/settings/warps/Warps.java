package com.earth2me.essentials.components.settings.warps;

import com.earth2me.essentials.storage.IStorageObject;
import com.earth2me.essentials.storage.MapValueType;
import java.util.LinkedHashMap;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Warps implements IStorageObject
{
	@MapValueType(Warp.class)
	private LinkedHashMap<String, Warp> warps = new LinkedHashMap<String, Warp>();
}
