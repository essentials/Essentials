package com.earth2me.essentials.api;

import com.earth2me.essentials.api.server.CommandSender;


public interface IReplyTo
{
	void setReplyTo(CommandSender user);

	CommandSender getReplyTo();
}
