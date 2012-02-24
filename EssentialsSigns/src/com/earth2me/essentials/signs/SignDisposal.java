package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.craftbukkit.ShowInventory;


public class SignDisposal extends EssentialsSign
{
	public SignDisposal()
	{
		super("Disposal");
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IContext ess)
	{
		ShowInventory.showEmptyInventory(player.getBase());
		return true;
	}
}
