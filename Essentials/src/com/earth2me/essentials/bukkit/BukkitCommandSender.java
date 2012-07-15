package com.earth2me.essentials.bukkit;

import com.earth2me.essentials.api.server.CommandSender;
import com.earth2me.essentials.api.server.Permission;
import lombok.Delegate;
import lombok.Getter;


public class BukkitCommandSender implements CommandSender
{
	@Delegate
	@Getter
	private org.bukkit.command.CommandSender commandSender;

	public BukkitCommandSender(org.bukkit.command.CommandSender commandSender)
	{
		this.commandSender = commandSender;
	}

	@Override
	public boolean isPlayer()
	{
		return false;
	}

	@Override
	public boolean isOp()
	{
		return commandSender.isOp();
	}
	
	public boolean hasPermission(Permission permission)
	{
		if (commandSender == null) {
			return false;
		} else {
			return commandSender.hasPermission(((BukkitPermission)permission).getBukkitPermission());
		}
	}
}
