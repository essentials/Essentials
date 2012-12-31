package net.ess3.economy;

import java.io.File;
import java.util.Map;
import net.ess3.api.IEssentials;
import net.ess3.api.IWorth;
import net.ess3.storage.AsyncStorageObjectHolder;
import net.ess3.storage.EnchantmentLevel;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;


public class WorthHolder extends AsyncStorageObjectHolder<net.ess3.economy.Worth> implements IWorth
{
	public WorthHolder(final IEssentials ess)
	{
		super(ess, net.ess3.economy.Worth.class, new File(ess.getPlugin().getDataFolder(), "worth.yml"));
		onReload(false);
	}

	@Override
	public double getPrice(final ItemStack itemStack)
	{
		final Map<MaterialData, Double> prices = this.getData().getSell();
		if (prices == null || itemStack == null)
		{
			return Double.NaN;
		}
		final Double basePrice = prices.get(itemStack.getData());
		if (basePrice == null || Double.isNaN(basePrice))
		{
			return Double.NaN;
		}
		double multiplier = 1.0;
		if (itemStack.getType().getMaxDurability() > 0)
		{
			multiplier *= (double)itemStack.getDurability() / (double)itemStack.getType().getMaxDurability();
		}
		if (itemStack.getEnchantments() != null && !itemStack.getEnchantments().isEmpty())
		{
			final Map<EnchantmentLevel, Double> enchantmentMultipliers = this.getData().getEnchantmentMultiplier();
			if (enchantmentMultipliers != null)
			{
				for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet())
				{
					final Double enchMult = enchantmentMultipliers.get(new EnchantmentLevel(entry.getKey(), entry.getValue()));
					if (enchMult != null)
					{
						multiplier *= enchMult;
					}
				}
			}
		}
		return basePrice * multiplier;
	}

	@Override
	public void setPrice(final ItemStack itemStack, final double price)
	{
		getData().setSellPrice(itemStack.getData(), price);
		queueSave();
	}
}
