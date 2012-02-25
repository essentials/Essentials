package com.earth2me.essentials.components.economy;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.settings.Worth;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import com.earth2me.essentials.storage.EnchantmentLevel;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;


public class WorthsComponent extends AsyncStorageObjectHolder<Worth> implements IWorthsComponent
{
	public WorthsComponent(final IContext context)
	{
		super(context, Worth.class);
	}

	@Override
	public String getTypeId()
	{
		return "WorthsComponent";
	}

	@Override
	public void initialize()
	{
	}

	@Override
	public void onEnable()
	{
		reload(false);
	}

	@Override
	public double getPrice(final ItemStack itemStack)
	{
		this.acquireReadLock();
		try
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
			if (itemStack.getType().getMaxDurability() > 0) {
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
		finally
		{
			this.unlock();
		}
	}

	@Override
	public void setPrice(final ItemStack itemStack, final double price)
	{
		acquireWriteLock();
		try {
			if (getData().getSell() == null) {
				getData().setSell(new HashMap<MaterialData, Double>());
			}
			getData().getSell().put(itemStack.getData(), price);
		} finally {
			unlock();
		}
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(getContext().getDataFolder(), "worth.yml");
	}
}
