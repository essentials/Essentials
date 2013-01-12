package net.ess3.commands;

import java.util.Collections;
import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.bukkit.LivingEntities;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;


public class Commandkillall extends EssentialsCommand
{
	//TODO: Tidy - missed this during command cleanup
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		String type = "all";
		int radius = -1;
		World world;
		if (isUser(sender))
		{
			world = getPlayer(sender).getWorld();
			if (args.length == 1)
			{
				try
				{
					radius = Integer.parseInt(args[0]);
				}
				catch (NumberFormatException e1)
				{
					type = args[0];
				}
			}
			else if (args.length > 1)
			{
				type = args[0];
				try
				{
					radius = Integer.parseInt(args[1]);
				}
				catch (NumberFormatException e)
				{
					throw new Exception(_("numberRequired"), e);
				}
			}
		}
		else
		{
			if (args.length == 0)
			{
				throw new NotEnoughArgumentsException();
			}
			else if (args.length == 1)
			{
				world = ess.getWorld(args[0]);
			}
			else
			{
				type = args[0];
				world = ess.getWorld(args[1]);
			}
		}
		if (radius >= 0)
		{
			radius *= radius;
		}
		final String killType = type.toLowerCase(Locale.ENGLISH);
		final boolean animals = killType.startsWith("animal");
		final boolean monster = killType.startsWith("monster") || killType.startsWith("mob");
		final boolean all = killType.equals("all");
		Class<? extends Entity> entityClass = null;
		if (!animals && !monster && !all)
		{
			if (LivingEntities.fromName(killType) == null)
			{
				throw new Exception(_("invalidMob"));
			}
			entityClass = LivingEntities.fromName(killType).getEntityClass();
		}
		int numKills = 0;
		final Location loc = isUser(sender) ? getPlayer(sender).getLocation() : null;
		for (Chunk chunk : world.getLoadedChunks())
		{
			for (Entity entity : chunk.getEntities())
			{
				if (loc != null)
				{
					if (radius >= 0 && loc.distanceSquared(entity.getLocation()) > radius)
					{
						continue;
					}
				}
				if (!(entity instanceof LivingEntity) || entity instanceof HumanEntity)
				{
					continue;
				}
				if (entity instanceof Wolf)
				{
					if (((Wolf)entity).isTamed())
					{
						continue;
					}
				}
				if (entity instanceof Ocelot)
				{
					if (((Ocelot)entity).isTamed())
					{
						continue;
					}
				}
				if (animals)
				{
					if (entity instanceof Animals || entity instanceof NPC || entity instanceof Snowman || entity instanceof WaterMob)
					{
						EntityDeathEvent event = new EntityDeathEvent((LivingEntity)entity, Collections.<ItemStack>emptyList());
						ess.getServer().getPluginManager().callEvent(event);
						entity.remove();
						numKills++;
					}
				}
				else if (monster)
				{
					if (entity instanceof Monster || entity instanceof ComplexLivingEntity || entity instanceof Flying || entity instanceof Slime)
					{
						EntityDeathEvent event = new EntityDeathEvent((LivingEntity)entity, Collections.<ItemStack>emptyList());
						ess.getServer().getPluginManager().callEvent(event);
						entity.remove();
						numKills++;
					}
				}
				else if (all)
				{
					EntityDeathEvent event = new EntityDeathEvent((LivingEntity)entity, Collections.<ItemStack>emptyList());
					ess.getServer().getPluginManager().callEvent(event);
					entity.remove();
					numKills++;
				}
				else if (entityClass != null && entityClass.isAssignableFrom(entity.getClass()))
				{
					EntityDeathEvent event = new EntityDeathEvent((LivingEntity)entity, Collections.<ItemStack>emptyList());
					ess.getServer().getPluginManager().callEvent(event);
					entity.remove();
					numKills++;
				}
			}
		}
		sender.sendMessage(_("kill", numKills));
	}
}
