package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;


public class Commandfirework extends EssentialsCommand
{
	private final transient Pattern splitPattern = Pattern.compile("[:+',;.]");
	private final static Map<String, DyeColor> colorMap = new HashMap<String, DyeColor>();
	private final static Map<String, FireworkEffect.Type> fireworkShape = new HashMap<String, FireworkEffect.Type>();

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

	public Commandfirework()
	{
		super("firework");
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0)
		{
			final ItemStack stack = user.getItemInHand();
			if (stack.getType() == Material.FIREWORK)
			{
				if (args.length > 0)
				{
					if (args[0].equalsIgnoreCase("clear"))
					{
						FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
						fmeta.clearEffects();
						stack.setItemMeta(fmeta);
					}
					else
					{
						final MetaItemStack mStack = new MetaItemStack(stack);
						boolean fire = false;
						int amount = 1;
						for (String arg : args)
						{
							final String[] split = splitPattern.split(arg, 2);
							mStack.addFireworkMeta(user, true, arg, ess);

							if (split.length > 1 && split[0].equalsIgnoreCase("fire") && user.isAuthorized("essentials.firework.fire"))
							{
								fire = true;
								try
								{
									amount = Integer.parseInt(split[1]);
									int serverLimit = ess.getSettings().getSpawnMobLimit();
									if (amount > serverLimit)
									{
										amount = serverLimit;
										user.sendMessage(_("mobSpawnLimit"));
									}
								}
								catch (NumberFormatException e)
								{
									amount = 1;
								}
							}
						}

						if (fire)
						{
							for (int i = 0; i < amount; i++)
							{
								Firework firework = (Firework)user.getWorld().spawnEntity(user.getLocation(), EntityType.FIREWORK);
								FireworkMeta fmeta = (FireworkMeta)mStack.getItemStack().getItemMeta();
								if (mStack.isValidFirework())
								{
									FireworkEffect effect = mStack.getFireworkBuilder().build();
									fmeta.addEffect(effect);
								}
								firework.setFireworkMeta(fmeta);
							}
						}
						else if (!mStack.isValidFirework())
						{
							user.sendMessage(_("fireworkColor"));
							user.sendMessage(_("fireworkSyntax"));
						}
						else
						{
							FireworkMeta fmeta = (FireworkMeta)mStack.getItemStack().getItemMeta();
							FireworkEffect effect = mStack.getFireworkBuilder().build();
							fmeta.addEffect(effect);
							stack.setItemMeta(fmeta);
						}
					}
				}
			}
			else
			{
				user.sendMessage(_("holdFirework"));
			}
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}
	}
}
