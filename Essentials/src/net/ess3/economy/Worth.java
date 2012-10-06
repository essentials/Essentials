package net.ess3.economy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.ess3.storage.EnchantmentLevel;
import net.ess3.storage.MapKeyType;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;


@Data
@EqualsAndHashCode(callSuper = false)
public class Worth implements StorageObject
{
	@MapKeyType(MaterialData.class)
	@MapValueType(Double.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<MaterialData, Double> sell;

	public Map<MaterialData, Double> getSell()
	{
		return sell == null
			   ? Collections.<MaterialData, Double>emptyMap()
			   : Collections.unmodifiableMap(sell);
	}
	@MapKeyType(MaterialData.class)
	@MapValueType(Double.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<MaterialData, Double> buy;

	public Map<MaterialData, Double> getBuy()
	{
		return buy == null
			   ? Collections.<MaterialData, Double>emptyMap()
			   : Collections.unmodifiableMap(buy);
	}
	@MapKeyType(EnchantmentLevel.class)
	@MapValueType(Double.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<EnchantmentLevel, Double> enchantmentMultiplier;

	public Map<EnchantmentLevel, Double> getEnchantmentMultiplier()
	{
		return enchantmentMultiplier == null
			   ? Collections.<EnchantmentLevel, Double>emptyMap()
			   : Collections.unmodifiableMap(enchantmentMultiplier);
	}

	public Worth()
	{
		sell = new HashMap<MaterialData, Double>();
		sell.put(new MaterialData(Material.APPLE, (byte)0), 1.0);
	}

	void setSellPrice(MaterialData data, double price)
	{
		Map<MaterialData, Double> sellMap = new HashMap<MaterialData, Double>(sell);
		sellMap.put(data, price);
		sell = sellMap;
	}
}
