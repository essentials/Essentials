package net.ess3.signs.signs;

import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.craftbukkit.InventoryWorkaround;
import net.ess3.economy.Trade;
import net.ess3.signs.EssentialsSign;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class SignFree extends EssentialsSign
{
	public SignFree()
	{
		super("Free");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		getItemStack(sign.getLine(1), 1, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		final ItemStack item = getItemStack(sign.getLine(1), 1, ess);
		if (item.getType() == Material.AIR)
		{
			throw new SignException(_("cantSpawnItem", "Air"));
		}

		item.setAmount(item.getType().getMaxStackSize());
		InventoryWorkaround.addItems(player.getPlayer().getInventory(), item); //TODO: Netbeans doesn't seem to like the addItems call, even though it appears valid.
		player.sendMessage("Item added to your inventory.");
		player.getPlayer().updateInventory();
		//TODO: wait for a fix in bukkit
		//Problem: Items can be duplicated
		//Inventory i = ess.getServer().createInventory(player, InventoryType.CHEST);
		//i.addItem(item);
		//player.openInventory(i);
		Trade.log("Sign", "Free", "Interact", username, null, username, new Trade(item, ess), sign.getBlock().getLocation(), ess);
		return true;
	}
}
