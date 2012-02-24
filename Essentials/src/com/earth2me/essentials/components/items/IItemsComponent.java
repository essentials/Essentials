package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.users.IUser;
import org.bukkit.inventory.ItemStack;


public interface IItemsComponent extends IComponent
{
	ItemStack get(final String name, final IUser user) throws Exception;

	ItemStack get(final String name, final int quantity) throws Exception;

	ItemStack get(final String name) throws Exception;
}
