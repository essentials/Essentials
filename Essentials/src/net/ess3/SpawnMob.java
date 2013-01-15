package net.ess3;

import java.util.*;
import java.util.regex.Pattern;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.bukkit.LivingEntities;
import net.ess3.bukkit.LivingEntities.MobException;
import net.ess3.commands.NotEnoughArgumentsException;
import net.ess3.permissions.Permissions;
import net.ess3.user.User;
import net.ess3.utils.LocationUtil;
import net.ess3.utils.Util;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.material.Colorable;


public class SpawnMob
{
	private static transient Pattern colon = Pattern.compile(":");
	private static transient Pattern comma = Pattern.compile(",");

	public static String mobList(final IUser user) throws NotEnoughArgumentsException
	{
		final Set<String> mobList = LivingEntities.getLivingEntityList();
		final List<String> availableList = new ArrayList<String>();
		for (String mob : mobList)
		{
			if (Permissions.SPAWNMOB.isAuthorized(user, mob))
			{
				availableList.add(mob);
			}
		}
		if (availableList.isEmpty())
		{
			availableList.add(_("none"));
		}

		Collections.sort(availableList);
		return Util.joinList(availableList);
	}

	public static String[] mobData(final String mobString)
	{
		String[] returnString = new String[4];

		final String[] parts = comma.split(mobString);
		String[] mobParts = colon.split(parts[0]);

		returnString[0] = mobParts[0];
		if (mobParts.length == 2)
		{
			returnString[1] = mobParts[1];
		}

		if (parts.length > 1)
		{
			String[] mountParts = colon.split(parts[1]);
			returnString[2] = mountParts[0];
			if (mountParts.length == 2)
			{
				returnString[3] = mountParts[1];
			}
		}

		return returnString;
	}

	// This method spawns a mob where the user is looking, owned by user
	public static void spawnmob(final IEssentials ess, final Server server, final IUser user, final String[] Data, int mobCount) throws Exception
	{
		final Block block = LocationUtil.getTarget(user.getPlayer()).getBlock();
		if (block == null)
		{
			throw new Exception(_("unableToSpawnMob"));
		}
		spawnmob(ess, server, user, user, block.getLocation(), Data, mobCount);
	}

	// This method spawns a mob at loc, owned by noone
	public static void spawnmob(final IEssentials ess, final Server server, final CommandSender sender, final Location loc, final String[] Data, int mobCount) throws Exception
	{
		spawnmob(ess, server, sender, null, loc, Data, mobCount);
	}

	// This method spawns a mob at target, owned by target
	public static void spawnmob(final IEssentials ess, final Server server, final CommandSender sender, final IUser target, final String[] Data, int mobCount) throws Exception
	{
		spawnmob(ess, server, sender, target, target.getPlayer().getLocation(), Data, mobCount);
	}

	// This method spawns a mob at loc, owned by target
	public static void spawnmob(final IEssentials ess, final Server server, final CommandSender sender, final IUser target, final Location loc, final String[] Data, int mobCount) throws Exception
	{
		final Location sloc = LocationUtil.getSafeDestination(loc);
		final String mobType = Data[0];
		final String mobData = Data[1];
		final String mountType = Data[2];
		final String mountData = Data[3];

		EntityType mob = LivingEntities.fromName(mobType);
		EntityType mobMount = null;

		checkSpawnable(ess, sender, mob);

		if (mountType != null)
		{
			mobMount = LivingEntities.fromName(mountType);
			checkSpawnable(ess, sender, mobMount);
		}

		ISettings settings = ess.getSettings();
		int serverLimit = settings.getData().getCommands().getSpawnmob().getLimit();

		if (mobCount > serverLimit)
		{
			mobCount = serverLimit;
			sender.sendMessage(_("mobSpawnLimit"));
		}

		try
		{
			for (int i = 0; i < mobCount; i++)
			{
				spawnMob(ess, server, sender, target, sloc, mob, mobData, mobMount, mountData);
			}
			sender.sendMessage(mobCount + " " + mob.getName().toLowerCase(Locale.ENGLISH) + " " + _("spawned"));
		}
		catch (MobException e1)
		{
			throw new Exception(_("unableToSpawnMob"), e1);
		}
		catch (NumberFormatException e2)
		{
			throw new Exception(_("numberRequired"), e2);
		}
		catch (NullPointerException np)
		{
			throw new Exception(_("soloMob"), np);
		}
	}

	private static void spawnMob(final IEssentials ess, final Server server, final CommandSender sender, final IUser target, final Location sloc, EntityType mob, String mobData, EntityType mobMount, String mountData) throws Exception
	{

		final World spawningWorld = sloc.getWorld();
		final Entity spawnedMob = spawningWorld.spawn(sloc, (Class<? extends LivingEntity>)mob.getEntityClass());
		Entity spawnedMount = null;

		if (mobMount != null)
		{
			spawnedMount = spawningWorld.spawn(sloc, (Class<? extends LivingEntity>)mobMount.getEntityClass());
			spawnedMob.setPassenger(spawnedMount);
		}
		if (mobData != null)
		{
			changeMobData(mob, spawnedMob, mobData, target);
		}
		if (spawnedMount != null && mountData != null)
		{
			changeMobData(mobMount, spawnedMount, mountData, target);
		}
	}

	private static void checkSpawnable(IEssentials ess, CommandSender sender, EntityType mob) throws Exception
	{
		if (mob == null)
		{
			throw new Exception(_("invalidMob"));
		}

		if (!Permissions.SPAWNMOB.isAuthorized((User)sender, mob.getName()))
		{
			throw new Exception(_("noPermToSpawnMob"));
		}
	}

	private static void changeMobData(final EntityType type, final Entity spawned, String data, final IUser target) throws Exception
	{
		data = data.toLowerCase(Locale.ENGLISH);

		if (spawned instanceof Slime)
		{
			try
			{
				((Slime)spawned).setSize(Integer.parseInt(data));
			}
			catch (Exception e)
			{
				throw new Exception(_("slimeMalformedSize"), e);
			}
		}
		if (spawned instanceof Ageable && data.contains("baby"))
		{
			((Ageable)spawned).setBaby();
			return;
		}
		if (spawned instanceof Colorable)
		{
			final String color = data.toUpperCase(Locale.ENGLISH).replace("BABY", "");
			try
			{
				if (color.equals("RANDOM"))
				{
					final Random rand = new Random();
					((Colorable)spawned).setColor(DyeColor.values()[rand.nextInt(DyeColor.values().length)]);
				}
				else
				{
					((Colorable)spawned).setColor(DyeColor.valueOf(color));
				}
			}
			catch (Exception e)
			{
				throw new Exception(_("sheepMalformedColor"), e);
			}
		}
		if (spawned instanceof Tameable && data.contains("tamed") && target != null)
		{
			final Tameable tameable = ((Tameable)spawned);
			tameable.setTamed(true);
			tameable.setOwner(target.getPlayer());
		}
		if (type == EntityType.WOLF && data.contains("angry"))
		{
			((Wolf)spawned).setAngry(true);
		}
		if (type == EntityType.CREEPER && data.contains("powered"))
		{
			((Creeper)spawned).setPowered(true);
		}
		if (type == EntityType.OCELOT)
		{
			if (data.contains("siamese"))
			{
				((Ocelot)spawned).setCatType(Ocelot.Type.SIAMESE_CAT);
			}
			else if (data.contains("red"))
			{
				((Ocelot)spawned).setCatType(Ocelot.Type.RED_CAT);
			}
			else if (data.contains("black"))
			{
				((Ocelot)spawned).setCatType(Ocelot.Type.BLACK_CAT);
			}
		}
		if (type == EntityType.VILLAGER)
		{
			for (Villager.Profession prof : Villager.Profession.values())
			{
				if (data.contains(prof.toString().toLowerCase(Locale.ENGLISH)))
				{
					((Villager)spawned).setProfession(prof);
				}
			}
		}
	}
}
