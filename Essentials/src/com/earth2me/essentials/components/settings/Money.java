package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.storage.IStorageObject;
import com.earth2me.essentials.storage.MapValueType;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Money implements IStorageObject
{
	@MapValueType(Double.class)
	private Map<String, Double> balances = new HashMap<String, Double>();
}
