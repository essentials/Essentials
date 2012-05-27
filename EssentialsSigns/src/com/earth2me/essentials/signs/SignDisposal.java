package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.IUser;


public class SignDisposal extends EssentialsSign
{
	public SignDisposal()
	{
		super("Disposal");
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess)
	{
		player.sendMessage("Bukkit broke this sign :(");
		//TODO: wait for a fix in bukkit
		//Problem: Items can be duplicated
		//player.getBase().openInventory(ess.getServer().createInventory(player, 36));
		return true;
	}
}
