package net.ess3.settings;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.api.server.ItemStack;
import net.ess3.storage.ListType;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Kit implements StorageObject
{
	@ListType(ItemStack.class)
	private List<ItemStack> items = new ArrayList<ItemStack>();
	private Double delay;
	
}
