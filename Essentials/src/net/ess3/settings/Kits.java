package net.ess3.settings;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.*;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


@Data
@EqualsAndHashCode(callSuper = false)
public class Kits implements StorageObject
{
	public Kits()
	{
		final Kit kit = new Kit();
		kit.setDelay(10.0);
		kit.setItems(
				Arrays.<ItemStack>asList(
				new ItemStack(Material.DIAMOND_SPADE, 1), new ItemStack(Material.DIAMOND_PICKAXE, 1), new ItemStack(Material.DIAMOND_AXE, 1)));
		kits = new HashMap<String, Kit>();
		kits.put("tools", kit);
	}
	@MapValueType(Kit.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<String, Kit> kits;

	public Map<String, Kit> getKits()
	{
		return kits == null ? Collections.<String, Kit>emptyMap() : Collections.unmodifiableMap(kits);
	}
}
