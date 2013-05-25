package net.ess3.api;

import org.bukkit.command.CommandSender;


public interface IReplyTo
{
	/**
	 *
	 * @param user
	 */
	void setReplyTo(CommandSender user);

	/**
	 *
	 * @return
	 */
	CommandSender getReplyTo();
}
