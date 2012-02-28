package com.earth2me.essentials.components.settings.users;

import org.bukkit.command.CommandSender;


public interface IReplyTo
{
	void setReplyTo(CommandSender user);

	CommandSender getReplyTo();
}
