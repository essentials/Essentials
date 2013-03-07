package com.earth2me.essentials;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.textreader.*;
import java.util.*;
import java.util.regex.Pattern;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;


public class MetaItemStack
{
	private final transient Pattern splitPattern = Pattern.compile("[:+',;.]");
	private final ItemStack stack;
	private final static Map<String, DyeColor> colorMap = new HashMap<String, DyeColor>();
	private final static Map<String, FireworkEffect.Type> fireworkShape = new HashMap<String, FireworkEffect.Type>();
	private FireworkEffect.Builder builder = FireworkEffect.builder();
	private PotionEffectType pEffectType;
	private PotionEffect pEffect;
	private boolean validFirework = false;
	private boolean validPotionEffect = false;
	private boolean validPotionDuration = false;
	private boolean validPotionPower = false;
	private boolean canceledEffect = false;
	private boolean completePotion = false;
	private int power = 1;
	private int duration = 120;

	static
	{
		for (DyeColor color : DyeColor.values())
		{
			colorMap.put(color.name(), color);
		}
		for (FireworkEffect.Type type : FireworkEffect.Type.values())
		{
			fireworkShape.put(type.name(), type);
		}
	}

	public MetaItemStack(final ItemStack stack)
	{
		this.stack = stack.clone();
	}

	public ItemStack getItemStack()
	{
		return stack;
	}

	public boolean isValidFirework()
	{
		return validFirework;
	}

	public boolean isValidPotion()
	{
		return validPotionEffect && validPotionDuration && validPotionPower;
	}

	public FireworkEffect.Builder getFireworkBuilder()
	{
		return builder;
	}

	public PotionEffect getPotionEffect()
	{
		return pEffect;
	}

	public boolean completePotion()
	{
		return completePotion;
	}

	private void resetPotionMeta()
	{
		pEffect = null;
		pEffectType = null;
		validPotionEffect = false;
		validPotionDuration = false;
		validPotionPower = false;
		completePotion = true;
	}

	public void parseStringMeta(final CommandSender sender, final boolean allowUnsafe, String[] string, int fromArg, final IEssentials ess) throws Exception
	{

		for (int i = fromArg; i < string.length; i++)
		{
			addStringMeta(sender, allowUnsafe, string[i], ess);
		}
		if (validFirework)
		{
			if (!hasMetaPermission(sender, "firework", true, false, ess))
			{
				throw new Exception(_("noMetaFirework"));
			}
			FireworkEffect effect = builder.build();
			FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
			fmeta.addEffect(effect);
			if (fmeta.getEffects().size() > 1 && !hasMetaPermission(sender, "firework-multiple", true, false, ess))
			{
				throw new Exception(_("multipleCharges"));
			}
			stack.setItemMeta(fmeta);
		}
	}

	private void addStringMeta(final CommandSender sender, final boolean allowUnsafe, final String string, final IEssentials ess) throws Exception
	{
		final String[] split = splitPattern.split(string, 2);
		if (split.length < 1)
		{
			return;
		}

		if (split.length > 1 && split[0].equalsIgnoreCase("name") && hasMetaPermission(sender, "name", true, true, ess))
		{
			final String displayName = Util.replaceFormat(split[1].replace('_', ' '));
			final ItemMeta meta = stack.getItemMeta();
			meta.setDisplayName(displayName);
			stack.setItemMeta(meta);
		}
		else if (split.length > 1 && (split[0].equalsIgnoreCase("lore") || split[0].equalsIgnoreCase("desc")) && hasMetaPermission(sender, "lore", true, true, ess))
		{
			final List<String> lore = new ArrayList<String>();
			for (String line : split[1].split("\\|"))
			{
				lore.add(Util.replaceFormat(line.replace('_', ' ')));
			}
			final ItemMeta meta = stack.getItemMeta();
			meta.setLore(lore);
			stack.setItemMeta(meta);
		}
		else if (split.length > 1 && (split[0].equalsIgnoreCase("player") || split[0].equalsIgnoreCase("owner")) && stack.getType() == Material.SKULL_ITEM && hasMetaPermission(sender, "head", true, true, ess))
		{
			if (stack.getDurability() == 3)
			{
				final String owner = split[1];
				final SkullMeta meta = (SkullMeta)stack.getItemMeta();
				boolean result = meta.setOwner(owner);
				stack.setItemMeta(meta);
			}
			else
			{
				throw new Exception(_("onlyPlayerSkulls"));
			}
		}
		else if (split.length > 1 && split[0].equalsIgnoreCase("book") && stack.getType() == Material.WRITTEN_BOOK && hasMetaPermission(sender, "book", true, true, ess))
		{
			final BookMeta meta = (BookMeta)stack.getItemMeta();
			final IText input = new BookInput("book", true, ess);
			final BookPager pager = new BookPager(input);

			if (hasMetaPermission(sender, "chapter-" + split[1].toLowerCase(), true, true, ess))
			{
				List<String> pages = pager.getPages(split[1]);
				meta.setPages(pages);

				stack.setItemMeta(meta);
			}
			else
			{
				sender.sendMessage(_("noChapterMeta"));
			}

		}
		else if (split.length > 1 && split[0].equalsIgnoreCase("author") && stack.getType() == Material.WRITTEN_BOOK && hasMetaPermission(sender, "author", true, true, ess))
		{
			final String author = split[1];
			final BookMeta meta = (BookMeta)stack.getItemMeta();
			meta.setAuthor(author);
			stack.setItemMeta(meta);
		}
		else if (split.length > 1 && split[0].equalsIgnoreCase("title") && stack.getType() == Material.WRITTEN_BOOK && hasMetaPermission(sender, "title", true, true, ess))
		{
			final String title = Util.replaceFormat(split[1].replace('_', ' '));
			final BookMeta meta = (BookMeta)stack.getItemMeta();
			meta.setTitle(title);
			stack.setItemMeta(meta);
		}
		else if (split.length > 1 && split[0].equalsIgnoreCase("power") && stack.getType() == Material.FIREWORK && hasMetaPermission(sender, "firework-power", true, true, ess))
		{
			final int power = Util.isInt(split[1]) ? Integer.parseInt(split[1]) : 0;
			final FireworkMeta meta = (FireworkMeta)stack.getItemMeta();
			meta.setPower(power > 3 ? 4 : power);
			stack.setItemMeta(meta);
		}
		else if (stack.getType() == Material.FIREWORK) //WARNING - Meta for fireworks will be ignored after this point.
		{
			addFireworkMeta(sender, false, string, ess);
		}
		else if (stack.getType() == Material.POTION) //WARNING - Meta for potions will be ignored after this point.
		{
			addPotionMeta(sender, false, string, ess);
		}
		else if (split.length > 1 && (split[0].equalsIgnoreCase("color") || split[0].equalsIgnoreCase("colour"))
				 && (stack.getType() == Material.LEATHER_BOOTS
					 || stack.getType() == Material.LEATHER_CHESTPLATE
					 || stack.getType() == Material.LEATHER_HELMET
					 || stack.getType() == Material.LEATHER_LEGGINGS))
		{
			final String[] color = split[1].split("(\\||,)");
			if (color.length == 3)
			{
				final int red = Util.isInt(color[0]) ? Integer.parseInt(color[0]) : 0;
				final int green = Util.isInt(color[1]) ? Integer.parseInt(color[1]) : 0;
				final int blue = Util.isInt(color[2]) ? Integer.parseInt(color[2]) : 0;
				final LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();
				meta.setColor(Color.fromRGB(red, green, blue));
				stack.setItemMeta(meta);
			}
			else
			{
				throw new Exception(_("leatherSyntax"));
			}
		}
		else
		{
			parseEnchantmentStrings(sender, allowUnsafe, split);
		}
	}

	public void addFireworkMeta(final CommandSender sender, final boolean allowShortName, final String string, final IEssentials ess) throws Exception
	{
		if (stack.getType() == Material.FIREWORK)
		{
			final String[] split = splitPattern.split(string, 2);

			if (split.length < 2)
			{
				return;
			}

			if (split[0].equalsIgnoreCase("color") || split[0].equalsIgnoreCase("colour") || (allowShortName && split[0].equalsIgnoreCase("c")))
			{
				if (validFirework)
				{
					if (!hasMetaPermission(sender, "firework", true, false, ess))
					{
						throw new Exception(_("noMetaFirework"));
					}
					FireworkEffect effect = builder.build();
					FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
					fmeta.addEffect(effect);
					if (fmeta.getEffects().size() > 1 && !hasMetaPermission(sender, "firework-multiple", false, false, ess))
					{
						throw new Exception(_("multipleCharges"));
					}
					stack.setItemMeta(fmeta);
					builder = FireworkEffect.builder();
				}

				List<Color> primaryColors = new ArrayList<Color>();
				String[] colors = split[1].split(",");
				for (String color : colors)
				{
					if (colorMap.containsKey(color.toUpperCase()))
					{
						validFirework = true;
						primaryColors.add(colorMap.get(color.toUpperCase()).getFireworkColor());
					}
					else
					{
						sender.sendMessage(_("fireworkSyntax"));
						throw new Exception(_("invalidFireworkFormat", split[1], split[0]));
					}
				}
				builder.withColor(primaryColors);
			}
			else if (split[0].equalsIgnoreCase("shape") || split[0].equalsIgnoreCase("type") || (allowShortName && (split[0].equalsIgnoreCase("s") || split[0].equalsIgnoreCase("t"))))
			{
				FireworkEffect.Type finalEffect = null;
				split[1] = (split[1].equalsIgnoreCase("large") ? "BALL_LARGE" : split[1]);
				if (fireworkShape.containsKey(split[1].toUpperCase()))
				{
					finalEffect = fireworkShape.get(split[1].toUpperCase());
				}
				else
				{
					sender.sendMessage(_("fireworkSyntax"));
					throw new Exception(_("invalidFireworkFormat", split[1], split[0]));
				}
				if (finalEffect != null)
				{
					builder.with(finalEffect);
				}
			}
			else if (split[0].equalsIgnoreCase("fade") || (allowShortName && split[0].equalsIgnoreCase("f")))
			{
				List<Color> fadeColors = new ArrayList<Color>();
				String[] colors = split[1].split(",");
				for (String color : colors)
				{
					if (colorMap.containsKey(color.toUpperCase()))
					{
						fadeColors.add(colorMap.get(color.toUpperCase()).getFireworkColor());
					}
					else
					{
						sender.sendMessage(_("fireworkSyntax"));
						throw new Exception(_("invalidFireworkFormat", split[1], split[0]));
					}
				}
				if (!fadeColors.isEmpty())
				{
					builder.withFade(fadeColors);
				}
			}
			else if (split[0].equalsIgnoreCase("effect") || (allowShortName && split[0].equalsIgnoreCase("e")))
			{
				String[] effects = split[1].split(",");
				for (String effect : effects)
				{
					if (effect.equalsIgnoreCase("twinkle"))
					{
						builder.flicker(true);
					}
					else if (effect.equalsIgnoreCase("trail"))
					{
						builder.trail(true);
					}
					else
					{
						sender.sendMessage(_("fireworkSyntax"));
						throw new Exception(_("invalidFireworkFormat", split[1], split[0]));
					}
				}
			}
		}
	}

	public void addPotionMeta(final CommandSender sender, final boolean allowShortName, final String string, final IEssentials ess) throws Exception
	{
		if (stack.getType() == Material.POTION)
		{
			final User user = ess.getUser(sender);
			final String[] split = splitPattern.split(string, 2);

			if (split.length < 2)
			{
				return;
			}

			if (split[0].equalsIgnoreCase("effect") || (allowShortName && split[0].equalsIgnoreCase("e")))
			{
				pEffectType = Potions.getByName(split[1]);
				if (pEffectType != null)
				{
					if (user != null && user.isAuthorized("essentials.potion." + pEffectType.getName().toLowerCase()))
					{
						validPotionEffect = true;
						canceledEffect = false;
					}
					else
					{
						canceledEffect = true;
						sender.sendMessage(_("invalidPotionEffect", pEffectType.getName().toLowerCase()));
					}
				}
				else
				{
					sender.sendMessage(_("invalidPotionEffect", split[1]));
					canceledEffect = true;
				}
			}
			else if (split[0].equalsIgnoreCase("power") || (allowShortName && split[0].equalsIgnoreCase("p")))
			{
				if (Util.isInt(split[1]))
				{
					validPotionPower = true;
					power = Integer.parseInt(split[1]);
				}
			}
			else if (split[0].equalsIgnoreCase("duration") || (allowShortName && split[0].equalsIgnoreCase("d")))
			{
				if (Util.isInt(split[1]))
				{
					validPotionDuration = true;
					duration = Integer.parseInt(split[1]) * 20; //Duration is in ticks by default, converted to seconds
				}
			}

			if (isValidPotion() && !canceledEffect)
			{
				PotionMeta pmeta = (PotionMeta)stack.getItemMeta();
				pEffect = pEffectType.createEffect(duration, power);
				if (pmeta.getCustomEffects().size() > 1 && !hasMetaPermission(sender, "potion-multiple", true, false, ess))
				{
					throw new Exception(_("multiplePotionEffects"));
				}
				pmeta.addCustomEffect(pEffect, true);
				stack.setItemMeta(pmeta);
				resetPotionMeta();
			}


		}
	}

	private void parseEnchantmentStrings(final CommandSender sender, final boolean allowUnsafe, final String[] split) throws Exception
	{
		Enchantment enchantment = getEnchantment(null, split[0]);

		if (enchantment == null)
		{
			return;
		}

		int level = -1;
		if (split.length > 1)
		{
			try
			{
				level = Integer.parseInt(split[1]);
			}
			catch (NumberFormatException ex)
			{
				level = -1;
			}
		}

		if (level < 0 || (!allowUnsafe && level > enchantment.getMaxLevel()))
		{
			level = enchantment.getMaxLevel();
		}
		addEnchantment(sender, allowUnsafe, enchantment, level);
	}

	public void addEnchantment(final CommandSender sender, final boolean allowUnsafe, final Enchantment enchantment, final int level) throws Exception
	{
		try
		{
			if (stack.getType().equals(Material.ENCHANTED_BOOK))
			{
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta)stack.getItemMeta();
				if (level == 0)
				{
					meta.removeStoredEnchant(enchantment);
				}
				else
				{
					meta.addStoredEnchant(enchantment, level, allowUnsafe);
				}
				stack.setItemMeta(meta);
			}
			else // all other material types besides ENCHANTED_BOOK
			{
				if (level == 0)
				{
					stack.removeEnchantment(enchantment);
				}
				else
				{
					if (allowUnsafe)
					{
						stack.addUnsafeEnchantment(enchantment, level);
					}
					else
					{
						stack.addEnchantment(enchantment, level);
					}
				}
			}
		}
		catch (Exception ex)
		{
			throw new Exception("Enchantment " + enchantment.getName() + ": " + ex.getMessage(), ex);
		}
	}

	public Enchantment getEnchantment(final User user, final String name) throws Exception
	{
		final Enchantment enchantment = Enchantments.getByName(name);
		if (enchantment == null)
		{
			return null;
		}
		final String enchantmentName = enchantment.getName().toLowerCase(Locale.ENGLISH);
		if (user != null && !user.isAuthorized("essentials.enchant." + enchantmentName))
		{
			throw new Exception(_("enchantmentPerm", enchantmentName));
		}
		return enchantment;
	}

	private boolean hasMetaPermission(final CommandSender sender, final String metaPerm, final boolean graceful, final boolean message, final IEssentials ess) throws Exception
	{
		final User user = ess.getUser(sender);
		if (user == null)
		{
			return true;
		}

		if (user.isAuthorized("essentials.itemspawn.meta-" + metaPerm))
		{
			return true;
		}

		if (graceful)
		{
			if (message)
			{
				user.sendMessage(_("noMetaPerm", metaPerm));
			}
			return false;
		}
		else
		{
			throw new Exception(_("noMetaPerm", metaPerm));
		}
	}
}
