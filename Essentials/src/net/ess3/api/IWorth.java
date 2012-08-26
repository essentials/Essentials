package net.ess3.api;

import org.bukkit.inventory.ItemStack;


public interface IWorth extends IReload
{
	double getPrice(ItemStack itemStack);

	void setPrice(ItemStack itemStack, double price);
}
