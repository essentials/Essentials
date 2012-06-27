package net.ess3.settings;

import net.ess3.storage.ListType;
import net.ess3.storage.StorageObject;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;


@Data
@EqualsAndHashCode(callSuper = false)
public class Kit implements StorageObject
{
	@ListType(ItemStack.class)
	private List<ItemStack> items = new ArrayList<ItemStack>();
	private Double delay;
	
}
