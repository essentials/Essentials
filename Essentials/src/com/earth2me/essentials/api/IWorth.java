package com.earth2me.essentials.api;

import com.earth2me.essentials.api.server.ItemStack;



public interface IWorth extends IReload
{
	double getPrice(ItemStack itemStack);

	void setPrice(ItemStack itemStack, double price);
}
