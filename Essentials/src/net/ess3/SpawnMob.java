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
	private static Pattern colon = Pattern.compile(":");
	private static Pattern comma = Pattern.compile(",");

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

	public static List<String> mobParts(final String mobString)
	{
		String[] mobParts = comma.split(mobString);

		List<String> mobs = new ArrayList<String>();

		for (String mobPart : mobParts)
		{
			String[] mobDatas = colon.split(mobPart);
			mobs.add(mobDatas[0]);
		}

		return mobs;
	}

	public static List<String> mobData(final String mobString)
	{
		String[] mobParts = comma.split(mobString);

		List<String> mobData = new ArrayList<String>();

		for (String mobPart : mobParts)
		{
			String[] mobDatas = colon.split(mobPart);
			if (mobDatas.length == 1)
			{
				mobData.add(null);
			}
			else
			{
				mobData.add(mobDatas[1]);
			}
		}
		return mobData;
	}

	// This method spawns a mob where the user is looking, owned by user
	public static void spawnmob(final IEssentials ess, final Server server, final IUser user, final List<String> parts, final List<String> data, int mobCount) throws Exception
	{
		final Block block = LocationUtil.getTarget(user.getPlayer()).getBlock();
		if (block == null)
		{
			throw new Exception(_("§4Unable to spawn mob."));
		}
		spawnmob(ess, server, user, user, block.getLocation(), parts, data, mobCount);
	}

	// This method spawns a mob at loc, owned by noone
	public static void spawnmob(final IEssentials ess, final Server server, final CommandSender sender, final Location loc, final List<String> parts, final List<String> data, int mobCount) throws Exception
	{
		spawnmob(ess, server, sender, null, loc, parts, data, mobCount);
	}

	// This method spawns a mob at target, owned by target
	public static void spawnmob(final IEssentials ess, final Server server, final CommandSender sender, final IUser target, final List<String> parts, final List<String> data, int mobCount) throws Exception
	{
		spawnmob(ess, server, sender, target, target.getPlayer().getLocation(), parts, data, mobCount);
	}

	// This method spawns a mob at loc, owned by target
	public static void spawnmob(final IEssentials ess, final Server server, final CommandSender sender, final IUser target, final Location loc, final List<String> parts, final List<String> data, int mobCount) throws Exception
	{
		final Location sloc = LocationUtil.getSafeDestination(loc);

		for (int i = 0; i < parts.size(); i++)
		{
			EntityType mob = LivingEntities.fromName(parts.get(i));
			checkSpawnable(ess, sender, mob);
		}

		ISettings settings = ess.getSettings();
		int serverLimit = settings.getData().getCommands().getSpawnmob().getLimit();

		if (mobCount > serverLimit)
		{
			mobCount = serverLimit;
			sender.sendMessage(_("Mob quantity limited to server limit."));
		}

		EntityType mob = LivingEntities.fromName(parts.get(0));
		try
		{
			for (int i = 0; i < mobCount; i++)
			{
				spawnMob(ess, server, sender, target, sloc, parts, data);
			}
			sender.sendMessage(mobCount + " " + mob.getName().toLowerCase(Locale.ENGLISH) + " " + _("spawned"));
		}
		catch (MobException e1)
		{
			throw new Exception(_("§4Unable to spawn mob."), e1);
		}
		catch (NumberFormatException e2)
		{
			throw new Exception(_("A number goes there, silly."), e2);
		}
		catch (NullPointerException np)
		{
			throw new Exception(_("§4That mob likes to be alone."), np);
		}
	}

	private static void spawnMob(final IEssentials ess, final Server server, final CommandSender sender, final IUser target, final Location sloc, final List<String> parts, final List<String> data) throws Exception
	{
		EntityType mob;
		Entity spawnedMob = null;
		Entity spawnedMount;
		final World spawningWorld = sloc.getWorld();

		for (int i = 0; i < parts.size(); i++)
		{
			if (i == 0)
			{
				mob = EntityType.fromName(parts.get(i));
				spawnedMob = spawningWorld.spawn(sloc, (Class<? extends LivingEntity>)mob.getEntityClass());

				if (data.get(i) != null)
				{
					changeMobData(mob, spawnedMob, data.get(i), target);
				}
			}

			int next = (i + 1);
			if (next < parts.size())
			{
				EntityType mMob = EntityType.fromName(parts.get(next));
				spawnedMount = spawningWorld.spawn(sloc, (Class<? extends LivingEntity>)mMob.getEntityClass());

				if (data.get(next) != null)
				{
					changeMobData(mMob, spawnedMount, data.get(next), target);
				}

				spawnedMob.setPassenger(spawnedMount);

				spawnedMob = spawnedMount;
			}
		}
	}

	private static void checkSpawnable(IEssentials ess, CommandSender sender, EntityType mob) throws Exception
	{
		if (mob == null)
		{
			throw new Exception(_("Invalid mob type."));
		}

		if (!Permissions.SPAWNMOB.isAuthorized((User)sender, mob.getName()))
		{
			throw new Exception(_("§4You don't have permission to spawn this mob."));
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
				throw new Exception(_("§4Malformed size."), e);
			}
		}
		if (spawned instanceof Ageable && data.contains("baby"))
		{
			((Ageable)spawned).setBaby();
			data = data.replace("baby", "");
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
				throw new Exception(_("§4Malformed color."), e);
			}
		}
		if (spawned instanceof Tameable && data.contains("tamed") && target != null)
		{
			final Tameable tameable = ((Tameable)spawned);
			tameable.setTamed(true);
			tameable.setOwner(target.getPlayer());
			data = data.replace("tamed", "");
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
			if (data.contains("siamese") || data.contains("white"))
			{
				((Ocelot)spawned).setCatType(Ocelot.Type.SIAMESE_CAT);
			}
			else if (data.contains("red") || data.contains("orange") || data.contains("tabby"))
			{
				((Ocelot)spawned).setCatType(Ocelot.Type.RED_CAT);
			}
			else if (data.contains("black") || data.contains("tuxedo"))
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
		if (spawned instanceof Zombie)
		{
			if (data.contains("villager"))
			{
				((Zombie)spawned).setVillager(true);
			}
			if (data.contains("baby"))
			{
				((Zombie)spawned).setBaby(true);
			}
		}
		if (type == EntityType.SKELETON)
		{
			if (data.contains("wither"))
			{
				((Skeleton)spawned).setSkeletonType(Skeleton.SkeletonType.WITHER);
			}
		}
		if (type == EntityType.EXPERIENCE_ORB)
		{
			((ExperienceOrb)spawned).setExperience(Integer.parseInt(data));
		}
	}
}
