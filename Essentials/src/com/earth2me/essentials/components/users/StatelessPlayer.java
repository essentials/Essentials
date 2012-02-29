package com.earth2me.essentials.components.users;

import lombok.Delegate;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class StatelessPlayer implements IStatelessPlayer
{
	@Delegate(types={ Player.class, LivingEntity.class, CommandSender.class }, excludes={ OfflinePlayer.class })
	@Getter
	private transient Player onlinePlayer;

	/**
	 * a set of data and methods common to both offline and online players.
	 */
	@Delegate
	@Getter
	private transient OfflinePlayer safePlayer;

	public StatelessPlayer(OfflinePlayer player)
	{
		if (player.isOnline())
		{
			setOnlinePlayer(player.getPlayer());
		}
		else
		{
			setOfflinePlayer(player);
		}
	}

	@Override
	public final void setOfflinePlayer(OfflinePlayer offlinePlayer)
	{
		safePlayer = offlinePlayer;
		onlinePlayer = null;
	}

	@Override
	public final void setOnlinePlayer(Player onlinePlayer)
	{
		safePlayer = this.onlinePlayer = onlinePlayer;
	}

	@Override
	public String getSafeDisplayName()
	{
		return onlinePlayer == null ? getName() : getDisplayName();
	}
}
