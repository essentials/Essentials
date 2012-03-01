package com.earth2me.essentials.components.messenger;

import org.bukkit.command.CommandSender;


public interface IReplyTo
{
	void setReplyTo(CommandSender user);

	CommandSender getReplyTo();
}
