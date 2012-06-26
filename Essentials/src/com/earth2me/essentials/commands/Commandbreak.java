package com.earth2me.essentials.commands;

import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import static com.earth2me.essentials.I18n._;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;


public class Commandbreak extends EssentialsCommand
{
	//TODO: Switch to use util class
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final Block block = user.getTargetBlock(null, 20);
		if (block == null)
		{
			throw new NoChargeException();
		}
		if (block.getType() == Material.AIR)
		{
			throw new NoChargeException();
		}
		if (block.getType() == Material.BEDROCK && !Permissions.BREAK_BEDROCK.isAuthorized(user))
		{
			throw new Exception(_("noBreakBedrock"));
		}
		//final List<ItemStack> list = (List<ItemStack>)block.getDrops();		
		//final BlockBreakEvent event = new BlockBreakEvent(block, user.getBase(), list);
		final BlockBreakEvent event = new BlockBreakEvent(block, user.getBase());
		server.getPluginManager().callEvent(event);
		if (event.isCancelled())
		{
			throw new NoChargeException();
		}
		else
		{
			block.setType(Material.AIR);
		}
	}
}