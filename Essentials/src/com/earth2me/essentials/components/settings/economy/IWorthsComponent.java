package com.earth2me.essentials.components.settings.economy;

import com.earth2me.essentials.components.IComponent;
import org.bukkit.inventory.ItemStack;


public interface IWorthsComponent extends IComponent
{
	double getPrice(ItemStack itemStack);

	void setPrice(ItemStack itemStack, double price);
}
