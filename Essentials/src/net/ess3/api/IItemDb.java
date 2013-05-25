package net.ess3.api;

import org.bukkit.inventory.ItemStack;


public interface IItemDb extends IReload
{
	/**
	 *
	 * @param name
	 * @param user
	 * @return
	 * @throws Exception
	 */
	ItemStack get(final String name, final IUser user) throws Exception;

	/**
	 *
	 * @param name
	 * @param quantity
	 * @return
	 * @throws Exception
	 */
	ItemStack get(final String name, final int quantity) throws Exception;

	/**
	 *
	 * @param name
	 * @return
	 * @throws Exception
	 */
	ItemStack get(final String name) throws Exception;
}
