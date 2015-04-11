package org.mcess.essentials.commands;

import org.mcess.essentials.I18n;

public class PlayerNotFoundException extends NoSuchFieldException
{
	public PlayerNotFoundException()
	{
		super(I18n.tl("playerNotFound"));
	}
}
