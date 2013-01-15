package net.ess3.economy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.*;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Money implements StorageObject
{
	@MapValueType(Double.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<String, Double> balances;

	public Map<String, Double> getBalances()
	{
		return balances == null ? Collections.<String, Double>emptyMap() : Collections.unmodifiableMap(balances);
	}

	public void setBalance(String name, Double value)
	{
		Map<String, Double> balanceMap = new HashMap<String, Double>(getBalances());
		balanceMap.put(name, value);
		balances = balanceMap;
	}

	void removeBalance(String name)
	{
		Map<String, Double> balanceMap = new HashMap<String, Double>(getBalances());
		balanceMap.remove(name);
		balances = balanceMap;
	}


}
