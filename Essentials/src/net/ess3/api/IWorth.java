package net.ess3.api;

import net.ess3.api.server.ItemStack;



public interface IWorth extends IReload
{
	double getPrice(ItemStack itemStack);

	void setPrice(ItemStack itemStack, double price);
}
