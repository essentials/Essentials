package net.ess3.update.chat;

import org.bukkit.entity.Player;


public interface Command
{
	void run(final IrcBot ircBot, final Player player);
}
