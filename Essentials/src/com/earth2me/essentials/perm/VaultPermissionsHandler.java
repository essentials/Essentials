package com.earth2me.essentials.perm;

import java.util.Arrays;
import java.util.List;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;


public class VaultPermissionsHandler implements IPermissionsHandler
{
	private transient final Permission perm;
	private transient final Chat chat;

	public VaultPermissionsHandler(final Permission perm, final Chat chat)
	{
		this.perm = perm;
		this.chat = chat;
	}

	@Override
	public String getGroup(Player base)
	{
		return perm.getPrimaryGroup(base);
	}

	@Override
	public List<String> getGroups(Player base)
	{
		return Arrays.asList(perm.getPlayerGroups(base));
	}

	@Override
	public boolean canBuild(Player base, String group)
	{
		return false;
	}

	@Override
	public boolean inGroup(Player base, String group)
	{
		return perm.playerInGroup(base, group);
	}

	@Override
	public boolean hasPermission(Player base, String node)
	{
		return perm.playerHas(base, node);
	}

	@Override
	public String getPrefix(Player base)
	{
		if (chat == null)
			return null;

		return chat.getPlayerPrefix(base);
	}

	@Override
	public String getSuffix(Player base)
	{
		if (chat == null)
			return null;

		return chat.getPlayerSuffix(base);
	}
}