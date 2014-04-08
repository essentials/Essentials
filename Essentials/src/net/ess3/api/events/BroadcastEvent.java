package net.ess3.api.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This handles the broadcast command.
 *
 */
public class BroadcastEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private boolean eventCancelled = false;
	private CommandSender broadcaster;
	private String broadcastMessage;
	private String broadcastPrefix;
	private List<String> broadcastRecipients = new ArrayList<String>();

	/**
	 * Create a new BroadcastEvent instance.
	 * @param broadcaster - The sender that typed the command.
	 * @param broadcastMessage - The broadcasted message.
	 */
	public BroadcastEvent(CommandSender broadcaster, String broadcastMessage) {
		this(broadcaster, broadcastMessage, broadcaster.getServer().getOnlinePlayers());
	}

	/**
	 * Create a new BroadcastEvent instance.
	 * @param broadcaster - The sender that typed the command.
	 * @param broadcastMessage - The broadcasted message.
	 * @param recipients - The recipients of the broadcast.
	 */
	public BroadcastEvent(CommandSender broadcaster, String broadcastMessage, Player[] recipients) {
		this.broadcaster = broadcaster;
		this.broadcastMessage = broadcastMessage;
		this.broadcastPrefix = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Broadcast" + ChatColor.GOLD + "] " + ChatColor.GREEN;
		for (Player recipient : recipients) {
			if (recipient != null) this.broadcastRecipients.add(recipient.getName());
		}
	}

	/** Get the prefix and message **/
	public String getFullMessage() {
		return this.broadcastPrefix + this.broadcastMessage;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/** Get the message of the broadcast **/
	public String getMessage() {
		return this.broadcastMessage;
	}

	/** Get the prefix of the broadcast message **/
	public String getPrefix() {
		return this.broadcastPrefix;
	}

	/** Get the recipients of the broadcast **/
	public List<Player> getRecipients() {
		List<Player> playerList = new ArrayList<Player>();
		for (String recipientName : this.broadcastRecipients) {
			Player recipient = Bukkit.getPlayerExact(recipientName);
			if (recipient != null) playerList.add(recipient);
		}
		return playerList;
	}

	/** Get the sender using the broadcast command **/
	public CommandSender getSender() {
		return this.broadcaster;
	}

	@Override
	public boolean isCancelled() {
		return this.eventCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.eventCancelled = cancelled;
	}

	/** Set the message of the broadcast **/
	public void setMessage(String message) {
		this.broadcastMessage = message;
	}

	/** Set the message of the broadcast **/
	public void setPrefix(String prefix) {
		this.broadcastPrefix = prefix;
	}

	/** Set the recipients of the broadcast **/
	public void setRecipients(List<Player> recipients) {
		this.broadcastRecipients = new ArrayList<String>();
		for (Player recipient : recipients) {
			if (recipient != null) this.broadcastRecipients.add(recipient.getName());
		}
	}

}
