package net.ess3.economy;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Money implements StorageObject
{
	@MapValueType(Double.class)
	private Map<String, Double> balances = new HashMap<String, Double>();
}
