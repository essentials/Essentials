package net.ess3.commands;

import net.ess3.permissions.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandFillDispenser extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if(args.length < 3)
		{
			throw new NotEnoughArgumentsException();
		}
		if(Permissions.FILLDISPENSER.isAuthorized(sender))
		{
			sender.sendMessage("No permissions.");
			return;
		}
		
		FillDispenser(sender, args);
	}
	
	private void FillDispenser(final CommandSender sender, final String[] args) throws Exception
	{
		Player player = (Player)sender;
		
		final ItemStack it = ess.getItemDb().get(args[1]);
		
		it.setAmount(Integer.valueOf(args[2]));
		
		final int radius = Integer.valueOf(args[0]);
		
		int i = 0;
		for(int x = -radius; x <= radius; x++)
		{
			for(int y = 0; y <= 256; y++)
			{
				for(int z = -radius; z <= radius; z++)
				{
					World world = player.getLocation().getWorld();
					Location loc = player.getLocation();
					Block block = world.getBlockAt(new Location(loc.getWorld(), loc.getX() + x, y, loc.getZ() + z));
					
					if (block.getType() == Material.DISPENSER)
	                	{
	                    Dispenser dis = (Dispenser)world.getBlockAt(new Location(loc.getWorld(), loc.getX() + x, y, loc.getZ() + z)).getState();
	                    	ItemStack[] is = { 
	                    			it, it, it, 
	                    			it, it, it, 
	                    			it, it, it };

	                    	dis.getInventory().setContents(is);
	                    	dis.update();
	                    	i++;
	                	}
				}
			}
		}
		final String patched = String.valueOf(i);
		final String block = it.getType().toString();
		
		player.sendMessage(ChatColor.GOLD + "Filled " + patched + " dispensers with " + block);
	}
}
