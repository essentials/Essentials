package com.earth2me.essentials.components.items;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.users.IUserComponent;
import org.bukkit.inventory.ItemStack;


public interface IItemsComponent extends IComponent
{
	ItemStack get(final String name, final IUserComponent user) throws Exception;

	ItemStack get(final String name, final int quantity) throws Exception;

	ItemStack get(final String name) throws Exception;
}
