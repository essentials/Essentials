package com.earth2me.essentials.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * Timer Command. Very useful for Commandblock-things. It changes after <time>
 * the block at the given Location into a redstone block (Mode: ON); Mode OFF would be Air, and TOGGLE places one if
 * there is none and sets air if there is one ;) | Default is ON | Usage: /timer <x> <y> <z> <time> [mode] | Modes: ON,
 * OFF, TOGGLE
 *
 * @author Moehritz
 */
public class Commandtimer extends EssentialsCommand
{
	public Commandtimer()
	{
		super("timer");
	}


	private enum TimerMode
	{
		ON, OFF, TOGGLE
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (!(args.length == 4 || args.length == 5))
		{
			throw new NotEnoughArgumentsException();
		}

		Location current;
		if (sender instanceof Player)
		{
			current = ((Player)sender).getLocation();
		}
		else if (sender instanceof BlockCommandSender)
		{
			current = ((BlockCommandSender)sender).getBlock().getLocation();
		}
		else
		{
			throw new Exception();
		}

		// Location where the redstoneblock will be placed
		final double x = args[0].startsWith("~") ? current.getX() + Integer.parseInt(args[0].substring(1)) : Integer.parseInt(args[0]);
		final double y = args[1].startsWith("~") ? current.getY() + Integer.parseInt(args[1].substring(1)) : Integer.parseInt(args[1]);
		final double z = args[2].startsWith("~") ? current.getZ() + Integer.parseInt(args[2].substring(1)) : Integer.parseInt(args[2]);
		final Location location = new Location(current.getWorld(), x, y, z, current.getYaw(), current.getPitch());

		// Time in ticks
		Long time = Long.parseLong(args[3]);

		// TimerMode
		TimerMode mode = null;

		if (args.length == 5)
		{
			mode = TimerMode.valueOf(args[4]);
		}
		if (mode == null)
		{
			mode = TimerMode.ON;
		}

		new BlockTimer(location, time, mode).start();
	}


	private class BlockTimer implements Runnable
	{
		private Location loc;
		private TimerMode mode;
		private Long time;

		private BlockTimer(Location loc, Long time, TimerMode mode)
		{
			this.loc = loc;
			this.time = time;
			this.mode = mode;
		}

		private void start()
		{
			ess.getScheduler().scheduleSyncDelayedTask(ess, this, time);
		}

		@Override
		public void run()
		{
			Block b = loc.getBlock();
			switch (mode)
			{
			case ON:
				b.setType(Material.REDSTONE_BLOCK);
				break;
			case OFF:
				b.setType(Material.AIR);
				break;
			case TOGGLE:
				if (b.getType().equals(Material.REDSTONE_BLOCK))
				{
					b.setType(Material.AIR);
				}
				else
				{
					b.setType(Material.REDSTONE_BLOCK);
				}
				break;
			}
		}
	}
}
