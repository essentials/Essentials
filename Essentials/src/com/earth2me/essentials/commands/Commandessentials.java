package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.Util;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandessentials extends EssentialsCommand
{
	public Commandessentials()
	{
		super("essentials");
	}
	private transient int taskid;
	private final transient Map<Player, Block> noteBlocks = new HashMap<Player, Block>();

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length == 0) {
			run_disabled(server, sender, commandLabel, args);
		}
		else if (args[0].equalsIgnoreCase("debug"))
		{
			run_debug(server, sender, commandLabel, args);
		}
		else if (args[0].equalsIgnoreCase("nya"))
		{
			run_nya(server, sender, commandLabel, args);
		}
		else {
			run_reload(server, sender, commandLabel, args);
		}
	}
	
	private void run_disabled(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage("Essentials " + ess.getDescription().getVersion());
		sender.sendMessage("/<command> <reload/debug>");
		sender.sendMessage(_("blockList"));
		final StringBuilder disabledCommands = new StringBuilder();
		for (Map.Entry<String, String> entry : ess.getAlternativeCommandsHandler().disabledCommands().entrySet())
		{
			if (disabledCommands.length() > 0) {
				disabledCommands.append(", ");
			}
			disabledCommands.append(entry.getKey()).append(" => ").append(entry.getValue());
		}
		sender.sendMessage(disabledCommands.toString());
	}
	
	private void run_debug(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		ess.getSettings().setDebug(!ess.getSettings().isDebug());
		sender.sendMessage("Essentials " + ess.getDescription().getVersion() + " debug mode " + (ess.getSettings().isDebug() ? "enabled" : "disabled"));
	}
	
	private void run_reload(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		ess.reload();
		sender.sendMessage(_("essentialsReload", ess.getDescription().getVersion()));
	}
	
	private void run_nya(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		final Map<String, Byte> noteMap = new HashMap<String, Byte>();
		noteMap.put("1F#", (byte)0x0);
		noteMap.put("1G", (byte)0x1);
		noteMap.put("1G#", (byte)0x2);
		noteMap.put("1A", (byte)0x3);
		noteMap.put("1A#", (byte)0x4);
		noteMap.put("1B", (byte)0x5);
		noteMap.put("1C", (byte)0x6);
		noteMap.put("1C#", (byte)0x7);
		noteMap.put("1D", (byte)0x8);
		noteMap.put("1D#", (byte)0x9);
		noteMap.put("1E", (byte)0xA);
		noteMap.put("1F", (byte)0xB);
		noteMap.put("2F#", (byte)(0x0 + 0xC));
		noteMap.put("2G", (byte)(0x1 + 0xC));
		noteMap.put("2G#", (byte)(0x2 + 0xC));
		noteMap.put("2A", (byte)(0x3 + 0xC));
		noteMap.put("2A#", (byte)(0x4 + 0xC));
		noteMap.put("2B", (byte)(0x5 + 0xC));
		noteMap.put("2C", (byte)(0x6 + 0xC));
		noteMap.put("2C#", (byte)(0x7 + 0xC));
		noteMap.put("2D", (byte)(0x8 + 0xC));
		noteMap.put("2D#", (byte)(0x9 + 0xC));
		noteMap.put("2E", (byte)(0xA + 0xC));
		noteMap.put("2F", (byte)(0xB + 0xC));
			if (!noteBlocks.isEmpty())
			{
				return;
			}
			final String tuneStr = "1D#,1E,2F#,,2A#,1E,1D#,1E,2F#,2B,2D#,2E,2D#,2A#,2B,,2F#,,1D#,1E,2F#,2B,2C#,2A#,2B,2C#,2E,2D#,2E,2C#,,2F#,,2G#,,1D,1D#,,1C#,1D,1C#,1B,,1B,,1C#,,1D,,1D,1C#,1B,1C#,1D#,2F#,2G#,1D#,2F#,1C#,1D#,1B,1C#,1B,1D#,,2F#,,2G#,1D#,2F#,1C#,1D#,1B,1D,1D#,1D,1C#,1B,1C#,1D,,1B,1C#,1D#,2F#,1C#,1D,1C#,1B,1C#,,1B,,1C#,,2F#,,2G#,,1D,1D#,,1C#,1D,1C#,1B,,1B,,1C#,,1D,,1D,1C#,1B,1C#,1D#,2F#,2G#,1D#,2F#,1C#,1D#,1B,1C#,1B,1D#,,2F#,,2G#,1D#,2F#,1C#,1D#,1B,1D,1D#,1D,1C#,1B,1C#,1D,,1B,1C#,1D#,2F#,1C#,1D,1C#,1B,1C#,,1B,,1B,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1B,,";
			final String[] tune = tuneStr.split(",");
			for (Player player : server.getOnlinePlayers())
			{
				final Location loc = player.getLocation();
				loc.add(0, 3, 0);
				while (loc.getBlockY() < player.getLocation().getBlockY() + 10 && loc.getBlock().getTypeId() != 0)
				{
					loc.add(0, 1, 0);
				}
				if (loc.getBlock().getTypeId() == 0)
				{
					noteBlocks.put(player, loc.getBlock());
					loc.getBlock().setType(Material.NOTE_BLOCK);
				}
			}
			taskid = ess.scheduleSyncRepeatingTask(new Runnable()
			{
				int i = 0;

				@Override
				public void run()
				{
					final String note = tune[i];
					i++;
					if (i >= tune.length)
					{
						Commandessentials.this.stopTune();
					}
					if (note.isEmpty())
					{
						return;
					}
					Map<Player, Block> noteBlocks = Commandessentials.this.noteBlocks;
					for (Player onlinePlayer : server.getOnlinePlayers())
					{
						final Block block = noteBlocks.get(onlinePlayer);
						if (block == null || block.getType() != Material.NOTE_BLOCK)
						{
							continue;
						}
						onlinePlayer.playNote(block.getLocation(), (byte)0, noteMap.get(note));
					}
				}
			}, 20, 2);
			return;
	}

	private void stopTune()
	{
		ess.getScheduler().cancelTask(taskid);
		for (Block block : noteBlocks.values())
		{
			if (block.getType() == Material.NOTE_BLOCK)
			{
				block.setType(Material.AIR);
			}
		}
		noteBlocks.clear();
	}
}
