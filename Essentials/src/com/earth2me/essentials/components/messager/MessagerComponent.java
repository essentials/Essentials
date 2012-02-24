/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.earth2me.essentials.components.messager;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IMessagerComponent;
import com.earth2me.essentials.components.Component;
import com.earth2me.essentials.components.users.IUser;
import org.bukkit.entity.Player;


/**
 * Provides a means of transportation for messages from the server to clients.
 */
public class MessagerComponent extends Component implements IMessagerComponent
{
	public MessagerComponent(final IContext context)
	{
		super(context);
	}

	/**
	 * Broadcast a message to all users on the server.
	 *
	 * @param sender The originator of the message, or null to bypass checks associated with this, such as ignore lists.
	 * @param message The message to send to all players.
	 * @return The number of users who received the message.
	 */
	@Override
	public int broadcastMessage(IUser sender, String message)
	{
		if (sender == null)
		{
			return getContext().getServer().broadcastMessage(message);
		}

		if (sender.isHidden())
		{
			return 0;
		}

		final Player[] players = getContext().getServer().getOnlinePlayers();

		for (Player player : players)
		{
			final IUser user = getContext().getUser(player);
			if (!user.isIgnoringPlayer(sender.getName()))
			{
				player.sendMessage(message);
			}
		}

		return players.length;
	}
}
