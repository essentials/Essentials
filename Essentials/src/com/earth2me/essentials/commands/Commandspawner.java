package com.earth2me.essentials.commands;

import com.earth2me.essentials.*;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.bukkit.Mob;
import com.earth2me.essentials.economy.Trade;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.SpawnerPermissions;
import com.earth2me.essentials.utils.LocationUtil;
import com.earth2me.essentials.utils.Util;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;


public class Commandspawner extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1 || args[0].length() < 2)
		{
			throw new NotEnoughArgumentsException(_("mobsAvailable", Util.joinList(Mob.getMobList())));
		}

		final Location target = LocationUtil.getTarget(user);
		if (target == null || target.getBlock().getType() != Material.MOB_SPAWNER)
		{
			throw new Exception(_("mobSpawnTarget"));
		}

		String name = args[0];

		Mob mob = null;
		mob = Mob.fromName(name);
		if (mob == null)
		{
			throw new Exception(_("invalidMob"));
		}
		if (ess.getSettings().getProtectPreventSpawn(mob.getType().toString().toLowerCase(Locale.ENGLISH)))
		{
			throw new Exception(_("disabledToSpawnMob"));
		}
		if (!user.isAuthorized("essentials.spawner." + mob.name.toLowerCase(Locale.ENGLISH)))
		{
			throw new Exception(_("noPermToSpawnMob"));
		}
		final Trade charge = new Trade("spawner-" + mob.name.toLowerCase(Locale.ENGLISH), ess);
		charge.isAffordableFor(user);
		try
		{
			String name = args[0];

			Mob mob = null;
			mob = Mob.fromName(name);
			if (mob == null)
			{
				user.sendMessage(_("invalidMob"));
				return;
			}
			if (!SpawnerPermissions.getPermission(mob.name).isAuthorized(user))
			{
				throw new Exception(_("unableToSpawnMob"));
			}
			final Trade charge = new Trade("spawner-" + mob.name.toLowerCase(Locale.ENGLISH), ess);
			charge.isAffordableFor(user);
			((CreatureSpawner)target.getBlock().getState()).setSpawnedType(mob.getType());
			charge.charge(user);
			user.sendMessage(_("setSpawner", mob.name));
		}
		catch (Throwable ex)
		{
			throw new Exception(_("mobSpawnError"), ex);
		}
		charge.charge(user);
		user.sendMessage(_("setSpawner", mob.name));

	}
}
