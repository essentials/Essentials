package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.bukkit.Mob;
import com.earth2me.essentials.bukkit.Mob.MobException;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.SpawnmobPermissions;
import com.earth2me.essentials.utils.LocationUtil;
import com.earth2me.essentials.utils.Util;
import java.util.HashSet;
import java.util.*;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.material.Colorable;


public class Commandspawnmob extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			final Set<String> mobList = Mob.getMobList();
			final Set<String> availableList = new HashSet<String>();
			for (String mob : mobList)
			{
				if (SpawnmobPermissions.getPermission(mob).isAuthorized(user))
				{
					availableList.add(mob);
				}
			}
			if (availableList.isEmpty())
			{
				availableList.add(_("none"));
			}
			throw new NotEnoughArgumentsException(_("mobsAvailable", Util.joinList(availableList)));
		}


		final String[] mountparts = args[0].split(",");
		String[] parts = mountparts[0].split(":");
		String mobType = parts[0];
		String mobData = null;
		if (parts.length == 2)
		{
			mobData = parts[1];
		}
		String mountType = null;
		String mountData = null;
		if (mountparts.length > 1)
		{
			parts = mountparts[1].split(":");
			mountType = parts[0];
			if (parts.length == 2)
			{
				mountData = parts[1];
			}
		}


		Entity spawnedMob = null;
		Mob mob = null;
		Entity spawnedMount = null;
		Mob mobMount = null;

		mob = Mob.fromName(mobType);
		if (mob == null)
		{
			throw new Exception(_("invalidMob"));
		}

		if (!SpawnmobPermissions.getPermission(mob.name).isAuthorized(user))
		{
			throw new Exception(_("noPermToSpawnMob"));
		}

		final Block block = LocationUtil.getTarget(user).getBlock();
		if (block == null)
		{
			throw new Exception(_("unableToSpawnMob"));
		}
		IUser otherUser = null;
		if (args.length >= 3)
		{
			otherUser = getPlayer(args, 2);
		}
		final Location loc = (otherUser == null) ? block.getLocation() : otherUser.getLocation();
		final Location sloc = LocationUtil.getSafeDestination(loc);
		try
		{
			spawnedMob = mob.spawn(user, server, sloc);
		}
		catch (MobException e)
		{
			throw new Exception(_("unableToSpawnMob"), e);
		}

		if (mountType != null)
		{
			mobMount = Mob.fromName(mountType);
			if (mobMount == null)
			{
				user.sendMessage(_("invalidMob"));
				return;
			}

			if (!SpawnmobPermissions.getPermission(mobMount.name).isAuthorized(user))
			{
				throw new Exception(_("noPermToSpawnMob"));
			}
			try
			{
				spawnedMount = mobMount.spawn(user, server, loc);
			}
			catch (MobException e)
			{
				throw new Exception(_("unableToSpawnMob"), e);
			}
			spawnedMob.setPassenger(spawnedMount);
		}
		if (mobData != null)
		{
			changeMobData(mob.getType(), spawnedMob, mobData, user);
		}
		if (spawnedMount != null && mountData != null)
		{
			changeMobData(mobMount.getType(), spawnedMount, mountData, user);
		}
		if (args.length >= 2)
		{
			int mobCount = Integer.parseInt(args[1]);
			int serverLimit = 1;
			ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			try
			{
				serverLimit = settings.getData().getCommands().getSpawnmob().getLimit();
			}
			finally
			{
				settings.unlock();
			}
			if (mobCount > serverLimit)
			{
				mobCount = serverLimit;
				user.sendMessage(_("mobSpawnLimit"));
			}

			try
			{
				for (int i = 1; i < mobCount; i++)
				{
					spawnedMob = mob.spawn(user, server, loc);
					if (mobMount != null)
					{
						try
						{
							spawnedMount = mobMount.spawn(user, server, loc);
						}
						catch (MobException e)
						{
							throw new Exception(_("unableToSpawnMob"), e);
						}
						spawnedMob.setPassenger(spawnedMount);
					}
					if (mobData != null)
					{
						changeMobData(mob.getType(), spawnedMob, mobData, user);
					}
					if (spawnedMount != null && mountData != null)
					{
						changeMobData(mobMount.getType(), spawnedMount, mountData, user);
					}
				}
				user.sendMessage(mobCount + " " + mob.name.toLowerCase(Locale.ENGLISH) + mob.suffix + " " + _("spawned"));
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
		else
		{
			user.sendMessage(mob.name + " " + _("spawned"));
		}
	}

	private void changeMobData(final EntityType type, final Entity spawned, String data, final IUser user) throws Exception
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
		if (spawned instanceof Tameable && data.contains("tamed"))
		{
			final Tameable tameable = ((Tameable)spawned);
			tameable.setTamed(true);
			tameable.setOwner(user.getBase());
		}
		if (type == EntityType.WOLF
			&& data.contains("angry"))
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
			for (Profession prof : Villager.Profession.values())
			{
				if (data.contains(prof.toString().toLowerCase(Locale.ENGLISH)))
				{
					((Villager)spawned).setProfession(prof);
				}
			}
		}
		if (type == EntityType.OCELOT)
		{
			if (data.contains("siamese"))
			{
				((Ocelot)spawned).setCatType(Ocelot.Type.SIAMESE_CAT);
			}
			if (data.contains("red"))
			{
				((Ocelot)spawned).setCatType(Ocelot.Type.RED_CAT);
			}
			if (data.contains("black"))
			{
				((Ocelot)spawned).setCatType(Ocelot.Type.BLACK_CAT);
			}
		}
	}
}
