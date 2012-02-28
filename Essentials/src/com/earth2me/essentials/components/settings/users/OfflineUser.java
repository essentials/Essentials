package com.earth2me.essentials.components.settings.users;

import com.earth2me.essentials.api.IContext;
import lombok.Delegate;
import lombok.Getter;
import org.bukkit.OfflinePlayer;


public class OfflineUser implements OfflinePlayer
{
	@Getter
	@Delegate
	private transient final OfflinePlayer base;

	public OfflineUser(String name, IContext context)
	{
		this.base = context.getServer().getOfflinePlayer(name);
	}
}
