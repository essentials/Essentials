package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n.tl_;

public class PlayerNotFoundException extends NoSuchFieldException
{
	public PlayerNotFoundException()
	{
		super(tl_("playerNotFound"));
	}
}
