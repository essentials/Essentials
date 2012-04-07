package com.earth2me.essentials.bukkit;

import com.earth2me.essentials.api.server.IInventory;
import com.earth2me.essentials.api.server.ItemStack;
import java.util.Map;
import lombok.Delegate;
import org.bukkit.inventory.PlayerInventory;

public class Inventory implements IInventory {
	@Delegate
	private org.bukkit.inventory.PlayerInventory inventory;

	Inventory(PlayerInventory inventory)
	{
		this.inventory = inventory;
	}

	@Override
	public boolean containsItem(boolean b, boolean b0, ItemStack itemStack)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map<Integer, ItemStack> addItem(boolean b, ItemStack itemStack)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean addAllItems(boolean b, ItemStack itemStack)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removeItem(boolean b, boolean b0, ItemStack itemStack)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map<Integer, ItemStack> addItem(boolean b, int oversizedStacksize, ItemStack itemStack)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
