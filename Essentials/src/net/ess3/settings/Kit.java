package net.ess3.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import lombok.*;
import net.ess3.storage.ListType;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Kit implements StorageObject
{
	@ListType(ItemStack.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private List<ItemStack> items;
	private Double delay;

	public List<ItemStack> getItems()
	{
		return items == null ? Collections.<ItemStack>emptyList() : Collections.unmodifiableList(items);
	}

	public void setItems(Collection<ItemStack> items)
	{
		this.items = new ArrayList<ItemStack>(items);
	}
}
