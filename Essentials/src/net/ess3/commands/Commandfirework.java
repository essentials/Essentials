package net.ess3.commands;

import java.util.regex.Pattern;
import static net.ess3.I18n._;
import net.ess3.MetaItemStack;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.utils.Util;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;


//This command has quite a complicated syntax, in theory it has 4 separate syntaxes which are all variable:
//
//1: /firework clear             - This clears all of the effects on a firework stack
//
//2: /firework power <int>       - This changes the base power of a firework
//
//3: /firework fire              - This 'fires' a copy of the firework held.
//3: /firework fire <int>        - This 'fires' a number of copies of the firework held.
//3: /firework fire <other>      - This 'fires' a copy of the firework held, in the direction you are looking, #easteregg
//
//4: /firework [meta]            - This will add an effect to the firework stack held
//4: /firework color:<color>     - The minimum you need to set an effect is 'color'
//4: Full Syntax:                  color:<color[,color,..]> [fade:<color[,color,..]>] [shape:<shape>] [effect:<effect[,effect]>]
//4: Possible Shapes:              star, ball, large, creeper, burst
//4: Possible Effects              trail, twinkle
public class Commandfirework extends EssentialsCommand
{
	private final transient Pattern splitPattern = Pattern.compile("[:+',;.]");

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final Player player = user.getPlayer();
		final ItemStack stack = user.getPlayer().getItemInHand();
		if (stack.getType() == Material.FIREWORK)
		{
			if (args.length > 0)
			{
				if (args[0].equalsIgnoreCase("clear"))
				{
					FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
					fmeta.clearEffects();
					stack.setItemMeta(fmeta);
					user.sendMessage(_("§6Removed all effects from held stack."));
				}
				else if (args.length > 1 && (args[0].equalsIgnoreCase("power") || (args[0].equalsIgnoreCase("p"))))
				{
					FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
					try
					{
						int power = Integer.parseInt(args[1]);
						fmeta.setPower(power > 3 ? 4 : power);
					}
					catch (NumberFormatException e)
					{
						throw new Exception(_("§6The option §4{0} §6is not a valid value for §4{1}§6.", args[1], args[0]));
					}
					stack.setItemMeta(fmeta);
				}
				else if ((args[0].equalsIgnoreCase("fire") || (args[0].equalsIgnoreCase("f")))
						 && Permissions.FIREWORK_FIRE.isAuthorized(user))
				{
					int amount = 1;
					boolean direction = false;
					if (args.length > 1)
					{
						if (Util.isInt(args[1]))
						{
							ISettings settings = ess.getSettings();
							int serverLimit = settings.getData().getCommands().getSpawnmob().getLimit();
							amount = Integer.parseInt(args[1]);
							if (amount > serverLimit)
							{
								amount = serverLimit;
								user.sendMessage(_("Mob quantity limited to server limit."));
							}
						}
						else
						{
							direction = true;
						}
					}
					for (int i = 0; i < amount; i++)
					{
						Firework firework = (Firework)player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
						FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
						if (direction)
						{
							final Vector vector = player.getEyeLocation().getDirection().multiply(0.070);
							if (fmeta.getPower() > 1)
							{
								fmeta.setPower(1);
							}
							firework.setVelocity(vector);
						}
						firework.setFireworkMeta(fmeta);
					}
				}
				else
				{
					final MetaItemStack mStack = new MetaItemStack(stack);
					for (String arg : args)
					{
						try
						{
							mStack.addFireworkMeta(user, true, arg, ess);
						}
						catch (Exception e)
						{
							user.sendMessage(_("§6Firework parameters:§c color:<color> [fade:<color>] [shape:<shape>] [effect:<effect>]n§6To use multiple colors/effects, separate values with commas: §cred,blue,pinkn§6Shapes:§c star, ball, large, creeper, burst §6Effects:§c trail, twinkle."));
							throw e;
						}
					}

					if (mStack.isValidFirework())
					{
						FireworkMeta fmeta = (FireworkMeta)mStack.getItemStack().getItemMeta();
						FireworkEffect effect = mStack.getFireworkBuilder().build();
						if (fmeta.getEffects().size() > 0 && !Permissions.FIREWORK_MULTIPLE.isAuthorized(user))
						{
							throw new Exception(_("§4You cannot apply more than one charge to this firework."));
						}
						fmeta.addEffect(effect);
						stack.setItemMeta(fmeta);
					}
					else
					{
						user.sendMessage(_("§6Firework parameters:§c color:<color> [fade:<color>] [shape:<shape>] [effect:<effect>]n§6To use multiple colors/effects, separate values with commas: §cred,blue,pinkn§6Shapes:§c star, ball, large, creeper, burst §6Effects:§c trail, twinkle."));
						throw new Exception(_("§4Invalid firework charge parameters inserted, must set a color first."));
					}
				}
			}
			else
			{
				throw new NotEnoughArgumentsException();
			}
		}
		else
		{
			throw new Exception(_("§4You must be holding a firework to add effects."));
		}
	}
}
