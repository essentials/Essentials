package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.I18n._;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;


public class Commandnuke extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws NoSuchFieldException, NotEnoughArgumentsException
	{
		List<Player> targets;
		if (args.length > 0)
		{
			targets = new ArrayList<Player>();
			int pos = 0;
			for (String arg : args)
			{
				targets.add(getPlayer(args, pos));
				pos++;
			}
		}
		else
		{
			targets = Arrays.asList(getServer().getOnlinePlayers());
		}
		getContext().getTntListener().enable();
		for (Player player : targets)
		{
			if (player == null)
			{
				continue;
			}
			player.sendMessage(_("nuke"));
			final Location loc = player.getLocation();
			final World world = loc.getWorld();
			for (int x = -10; x <= 10; x += 5)
			{
				for (int z = -10; z <= 10; z += 5)
				{
					final Location tntloc = new Location(world, loc.getBlockX() + x, 127, loc.getBlockZ() + z);
					final TNTPrimed tnt = world.spawn(tntloc, TNTPrimed.class);
				}
			}
		}
	}
}
