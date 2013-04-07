package net.ess3.api;

import org.bukkit.inventory.ItemStack;


public interface IWorth extends IReload
{
	/**
	 * Get the price of an ItemStack
	 *
	 * @param itemStack - ItemStack to check
	 * @return - double marking price
	 */
	double getPrice(ItemStack itemStack);

	/**
	 * Set the price of an ItemStack
	 *
	 * @param itemStack - ItemStack to set price for
	 * @param price - Price to set on item
	 */
	void setPrice(ItemStack itemStack, double price);
}
