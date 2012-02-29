package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.Mob;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.perm.SpawnerPermissions;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;


public class Commandspawner extends EssentialsCommand
{
	@Override
	protected void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1 || args[0].length() < 2)
		{
			throw new NotEnoughArgumentsException($("mobsAvailable", Util.joinList(Mob.getMobList())));
		}

		final Location target = Util.getTarget(user);
		if (target == null || target.getBlock().getType() != Material.MOB_SPAWNER)
		{
			throw new Exception($("mobSpawnTarget"));
		}

		try
		{
			String name = args[0];

			Mob mob = null;
			mob = Mob.fromName(name);
			if (mob == null)
			{
				user.sendMessage($("invalidMob"));
				return;
			}
			if (!SpawnerPermissions.getPermission(mob.name).isAuthorized(user))
			{
				throw new Exception($("unableToSpawnMob"));
			}
			final Trade charge = new Trade("spawner-" + mob.name.toLowerCase(Locale.ENGLISH), getContext());
			charge.isAffordableFor(user);
			((CreatureSpawner)target.getBlock().getState()).setCreatureType(mob.getType());
			charge.charge(user);
			user.sendMessage($("setSpawner", mob.name));
		}
		catch (Throwable ex)
		{
			throw new Exception($("mobSpawnError"), ex);
		}
	}
}
