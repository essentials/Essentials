package net.ess3.api;

import org.bukkit.command.CommandSender;



public interface IReplyTo
{
	void setReplyTo(CommandSender user);

	CommandSender getReplyTo();
}
