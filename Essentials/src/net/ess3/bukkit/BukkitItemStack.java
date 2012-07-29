package net.ess3.bukkit;

import net.ess3.api.server.Enchantment;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.Material;
import lombok.Delegate;
import lombok.Getter;


public class BukkitItemStack extends net.ess3.api.server.ItemStack
{
	public static class BukkitItemStackFactory implements ItemStackFactory
	{
		@Override
		public ItemStack create(int id, int amount, int data)
		{
			return new BukkitItemStack(new org.bukkit.inventory.ItemStack(id, amount, (short)data));
		}

		@Override
		public ItemStack clone(final ItemStack stack)
		{
			return new BukkitItemStack(((BukkitItemStack)stack).getItemStack().clone());
		}
	}


	private interface Excludes
	{
		org.bukkit.Material getType();
	}
	@Delegate(excludes =
	{
		Excludes.class
	})
	@Getter
	private final org.bukkit.inventory.ItemStack itemStack;

	public BukkitItemStack(final org.bukkit.inventory.ItemStack itemStack)
	{
		super();
		this.itemStack = itemStack;
	}
	
	@Override
	public void addEnchantment(Enchantment enchantment, int level)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Material getType()
	{
		return Material.get(itemStack.getTypeId());
	}
	
	@Override
	public void setType(Material type)
	{
		itemStack.setTypeId(type.getId());
	}
	
	@Override
	public void setType(int id)
	{
		itemStack.setTypeId(id);
	}

	@Override
	public boolean isAir()
	{
		return itemStack.getTypeId() == 0;
	}
}
