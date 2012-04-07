package com.earth2me.essentials.api.server;

import java.util.Map;

public interface IInventory {

	public boolean containsItem(boolean b, boolean b0, ItemStack itemStack);

	public Map<Integer, ItemStack> addItem(ItemStack itemStack);
	
	public Map<Integer, ItemStack> addItem(boolean b, ItemStack itemStack);
	
	public Map<Integer, ItemStack> addItem(boolean b, int oversizedStacksize, ItemStack itemStack);

	public boolean addAllItems(boolean b, ItemStack itemStack);

	public void removeItem(boolean b, boolean b0, ItemStack itemStack);

}
