package com.earth2me.essentials.components.users;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;


public interface IStatelessPlayer extends Player, LivingEntity, CommandSender, ServerOperator
{
	void setOfflinePlayer(OfflinePlayer offlinePlayer);

	void setOnlinePlayer(Player onlinePlayer);

	OfflinePlayer getSafePlayer();

	Player getOnlinePlayer();

	String getSafeDisplayName();
}
