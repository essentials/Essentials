package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.storage.IStorageObject;
import com.earth2me.essentials.storage.ListType;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;


@Data
@EqualsAndHashCode(callSuper = false)
public class Kit implements IStorageObject
{
	@ListType(ItemStack.class)
	private List<ItemStack> items = new ArrayList<ItemStack>();
	private Double delay;
}
