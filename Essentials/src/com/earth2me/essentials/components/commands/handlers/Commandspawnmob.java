package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Mob;
import com.earth2me.essentials.Mob.MobException;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.SpawnmobPermissions;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;


public class Commandspawnmob extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			Set<String> availableList = Mob.getMobList();
			for (String mob : availableList)
			{
				if (!SpawnmobPermissions.getPermission(mob).isAuthorized(user))
				{
					availableList.remove(mob);
				}
			}
			if (availableList.isEmpty())
			{
				availableList.add($("none"));
			}
			throw new NotEnoughArgumentsException($("mobsAvailable", Util.joinList(availableList)));
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
			throw new Exception($("invalidMob"));
		}

		if (!SpawnmobPermissions.getPermission(mob.name).isAuthorized(user))
		{
			throw new Exception($("noPermToSpawnMob"));
		}

		final Block block = Util.getTarget(user).getBlock();
		if (block == null)
		{
			throw new Exception($("unableToSpawnMob"));
		}
		IUserComponent otherUser = null;
		if (args.length >= 3)
		{
			otherUser = getPlayer(args, 2);
		}
		final Location loc = (otherUser == null) ? block.getLocation() : otherUser.getLocation();
		final Location sloc = Util.getSafeDestination(loc);
		try
		{
			spawnedMob = mob.spawn(user, getServer(), sloc);
		}
		catch (MobException e)
		{
			throw new Exception($("unableToSpawnMob"));
		}

		if (mountType != null)
		{
			mobMount = Mob.fromName(mountType);
			if (mobMount == null)
			{
				user.sendMessage($("invalidMob"));
				return;
			}

			if (!SpawnmobPermissions.getPermission(mobMount.name).isAuthorized(user))
			{
				throw new Exception($("noPermToSpawnMob"));
			}
			try
			{
				spawnedMount = mobMount.spawn(user, getServer(), loc);
			}
			catch (MobException e)
			{
				throw new Exception($("unableToSpawnMob"));
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
			ISettingsComponent settings = getContext().getSettings();
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
				user.sendMessage($("mobSpawnLimit"));
			}

			try
			{
				for (int i = 1; i < mobCount; i++)
				{
					spawnedMob = mob.spawn(user, getServer(), loc);
					if (mobMount != null)
					{
						try
						{
							spawnedMount = mobMount.spawn(user, getServer(), loc);
						}
						catch (MobException e)
						{
							throw new Exception($("unableToSpawnMob"));
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
				user.sendMessage(args[1] + " " + mob.name.toLowerCase(Locale.ENGLISH) + mob.suffix + " " + $("spawned"));
			}
			catch (MobException e1)
			{
				throw new Exception($("unableToSpawnMob"), e1);
			}
			catch (NumberFormatException e2)
			{
				throw new Exception($("numberRequired"), e2);
			}
			catch (NullPointerException np)
			{
				throw new Exception($("soloMob"), np);
			}
		}
		else
		{
			user.sendMessage(mob.name + " " + $("spawned"));
		}
	}

	private void changeMobData(final CreatureType type, final Entity spawned, final String data, final IUserComponent user) throws Exception
	{
		if (type == CreatureType.SLIME || type == CreatureType.MAGMA_CUBE)
		{
			try
			{
				((Slime)spawned).setSize(Integer.parseInt(data));
			}
			catch (Exception e)
			{
				throw new Exception($("slimeMalformedSize"), e);
			}
		}
		if ((type == CreatureType.SHEEP
			 || type == CreatureType.COW
			 || type == CreatureType.MUSHROOM_COW
			 || type == CreatureType.CHICKEN
			 || type == CreatureType.PIG
			 || type == CreatureType.WOLF)
			&& data.equalsIgnoreCase("baby"))
		{
			((Animals)spawned).setAge(-24000);
			return;
		}
		if (type == CreatureType.SHEEP)
		{
			if (data.toLowerCase(Locale.ENGLISH).contains("baby"))
			{
				((Sheep)spawned).setAge(-24000);
			}
			final String color = data.toUpperCase(Locale.ENGLISH).replace("BABY", "");
			try
			{

				if (color.equalsIgnoreCase("random"))
				{
					Random rand = new Random();
					((Sheep)spawned).setColor(DyeColor.values()[rand.nextInt(DyeColor.values().length)]);
				}
				else
				{
					((Sheep)spawned).setColor(DyeColor.valueOf(color));
				}
			}
			catch (Exception e)
			{
				throw new Exception($("sheepMalformedColor"), e);
			}
		}
		if (type == CreatureType.WOLF
			&& data.toLowerCase(Locale.ENGLISH).startsWith("tamed"))
		{
			final Wolf wolf = ((Wolf)spawned);
			wolf.setTamed(true);
			wolf.setOwner(user.getBase());
			wolf.setSitting(true);
			if (data.equalsIgnoreCase("tamedbaby"))
			{
				((Animals)spawned).setAge(-24000);
			}
		}
		if (type == CreatureType.WOLF
			&& data.toLowerCase(Locale.ENGLISH).startsWith("angry"))
		{
			((Wolf)spawned).setAngry(true);
			if (data.equalsIgnoreCase("angrybaby"))
			{
				((Animals)spawned).setAge(-24000);
			}
		}
		if (type == CreatureType.CREEPER && data.equalsIgnoreCase("powered"))
		{
			((Creeper)spawned).setPowered(true);
		}
	}
}
