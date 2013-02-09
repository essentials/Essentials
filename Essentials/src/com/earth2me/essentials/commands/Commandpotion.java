package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.Potions;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Commandpotion extends EssentialsCommand
{
	private final transient Pattern splitPattern = Pattern.compile("[:+',;.]");

	public Commandpotion()
	{
		super("potion");
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		final ItemStack stack = user.getItemInHand();

		if (args.length == 0)
		{
			final Set<String> potionslist = new TreeSet<String>();
			for (Map.Entry<String, PotionEffectType> entry : Potions.entrySet())
			{
				final String potionName = entry.getValue().getName().toLowerCase(Locale.ENGLISH);
				if (potionslist.contains(potionName) || (user.isAuthorized("essentials.potion." + potionName)))
				{
					potionslist.add(entry.getKey());
				}
			}
			throw new NotEnoughArgumentsException(_("potions", Util.joinList(potionslist.toArray())));
		}

		if (stack.getType() == Material.POTION)
		{
			PotionMeta pmeta = (PotionMeta)stack.getItemMeta();
			if (args.length > 0)
			{
				if (args[0].equalsIgnoreCase("clear"))
				{
					pmeta.clearCustomEffects();
					stack.setItemMeta(pmeta);
				}
				else if (args[0].equalsIgnoreCase("apply") && user.isAuthorized("essentials.potion.apply"))
				{
					for(PotionEffect effect : pmeta.getCustomEffects())
					{
						effect.apply(user);
					}
				}
				else
				{
					final MetaItemStack mStack = new MetaItemStack(stack);
					for (String arg : args)
					{
						mStack.addPotionMeta(user, true, arg, ess);
					}
					if (mStack.completePotion())
					{
						pmeta = (PotionMeta)mStack.getItemStack().getItemMeta();
						stack.setItemMeta(pmeta);
					}
					else
					{
						user.sendMessage(_("invalidPotion"));
						throw new NotEnoughArgumentsException();
					}
				}
			}

		}
		else
		{
			throw new Exception(_("holdPotion"));
		}
	}
}
