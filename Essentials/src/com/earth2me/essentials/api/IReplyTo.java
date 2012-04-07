package com.earth2me.essentials.api;

import com.earth2me.essentials.api.server.ICommandSender;


public interface IReplyTo
{
	void setReplyTo(ICommandSender user);

	ICommandSender getReplyTo();
}
