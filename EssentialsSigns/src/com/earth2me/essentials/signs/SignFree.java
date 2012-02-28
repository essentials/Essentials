package com.earth2me.essentials.signs;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.settings.users.IUserComponent;
import com.earth2me.essentials.craftbukkit.ShowInventory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class SignFree extends EssentialsSign
{
	public SignFree()
	{
		super("Free");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUserComponent player, final String username, final IContext ess) throws SignException
	{
		getItemStack(sign.getLine(1), 1, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUserComponent player, final String username, final IContext ess) throws SignException
	{
		final ItemStack item = getItemStack(sign.getLine(1), 1, ess);
		if (item.getType() == Material.AIR)
		{
			throw new SignException(_("cantSpawnItem", "Air"));
		}

		item.setAmount(item.getType().getMaxStackSize() * 9 * 4);
		ShowInventory.showFilledInventory(player.getBase(), item);
		Trade.log("Sign", "Free", "Interact", username, null, username, new Trade(item, ess), sign.getBlock().getLocation(), ess);
		return true;
	}
}
