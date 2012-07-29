package net.ess3.bukkit.events;

import net.ess3.api.server.Plugin;
import net.ess3.api.server.events.EventListener;
import net.ess3.api.server.events.EventPriority;
import net.ess3.api.server.events.EventType;
import net.ess3.bukkit.BukkitPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.EventExecutor;


public class BukkitListener implements Listener
{
	private Plugin plugin;

	public BukkitListener(final Plugin plugin)
	{
		this.plugin = plugin;
	}

	public void register(EventListener listener, EventType type, EventPriority priority, boolean ignoreCancelled)
	{
		Class<? extends Event> event = getEventClass(type);
		org.bukkit.event.EventPriority bukkitPriority = getEventPriority(priority);
		EventExecutor executor = getEventExecutor(type, listener);
		((BukkitPlugin)plugin).getBukkitServer().getPluginManager().registerEvent(event, this, bukkitPriority, executor, ((BukkitPlugin)plugin).getBukkitPlugin(), ignoreCancelled);
	}

	private static Class<? extends Event> getEventClass(final EventType type)
	{
		switch (type)
		{
		case PLACE_BLOCK:
			return BlockPlaceEvent.class;
		}
		throw new RuntimeException("Missing Event Class");
	}

	private static org.bukkit.event.EventPriority getEventPriority(final EventPriority priority)
	{
		switch (priority)
		{
		case LOWEST:
			return org.bukkit.event.EventPriority.LOWEST;
		case LOW:
			return org.bukkit.event.EventPriority.LOW;
		case NORMAL:
			return org.bukkit.event.EventPriority.NORMAL;
		case HIGH:
			return org.bukkit.event.EventPriority.HIGH;
		case HIGHEST:
			return org.bukkit.event.EventPriority.HIGHEST;
		case MONITOR:
			return org.bukkit.event.EventPriority.MONITOR;
		}
		throw new RuntimeException("Missing Event Priority");
	}

	private EventExecutor getEventExecutor(final EventType type, final EventListener listener)
	{
		switch (type)
		{
		case PLACE_BLOCK:
			return new PlaceBlockExecutor(listener, plugin.getServer());
		}
		throw new RuntimeException("Missing Event Executor");
	}
}
