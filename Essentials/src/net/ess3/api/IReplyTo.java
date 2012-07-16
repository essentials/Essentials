package net.ess3.api;

import net.ess3.api.server.CommandSender;


public interface IReplyTo
{
	void setReplyTo(CommandSender user);

	CommandSender getReplyTo();
}
