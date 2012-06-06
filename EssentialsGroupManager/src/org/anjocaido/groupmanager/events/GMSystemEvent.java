package org.anjocaido.groupmanager.events;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author ElgarL
 * 
 */
public class GMSystemEvent extends Event {

	/**
	 * 
	 */
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {

		return handlers;
	}

	public static HandlerList getHandlerList() {

		return handlers;
	}

	//////////////////////////////

	protected Action action;

	public GMSystemEvent(Action action) {

		super();

		this.action = action;
	}

	public Action getAction() {

		return this.action;
	}

	public enum Action {
		RELOADED, SAVED, DEFAULT_GROUP_CHANGED, VALIDATE_TOGGLE,
	}

	public void schedule(final GMSystemEvent event) {

		if (Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("GroupManager"), new Runnable() {

			@Override
			public void run() {

				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		}, 1) == -1)
			GroupManager.logger.warning("Could not schedule GM Event.");
	}
}