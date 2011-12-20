package com.earth2me.essentials.craftbukkit;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class FakeInventory implements Inventory
{
	ItemStack[] items;

	public FakeInventory(ItemStack[] items)
	{
		this.items = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++)
		{
			if (items[i] == null)
			{
				continue;
			}
			this.items[i] = items[i].clone();
		}
	}

	@Override
	public int getSize()
	{
		return items.length;
	}

	@Override
	public String getName()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ItemStack getItem(int i)
	{
		return items[i];
	}

	@Override
	public void setItem(int i, ItemStack is)
	{
		items[i] = is;
	}

	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... iss)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... iss)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ItemStack[] getContents()
	{
		return items;
	}

	@Override
	public void setContents(ItemStack[] iss)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean contains(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean contains(Material mtrl)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean contains(ItemStack is)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean contains(int i, int i1)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean contains(Material mtrl, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean contains(ItemStack is, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material mtrl)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack is)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int first(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int first(Material mtrl)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int first(ItemStack is)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int firstEmpty()
	{
		for (int i = 0; i < items.length; i++)
		{
			if (items[i] == null || items[i].getTypeId() == 0) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void remove(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void remove(Material mtrl)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void remove(ItemStack is)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void clear(int i)
	{
		items[i] = null;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < items.length; i++)
		{
			items[i] = null;
		}
	}
}
