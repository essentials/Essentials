package net.ess3.bukkit.events;

import net.ess3.api.server.Plugin;
import net.ess3.api.server.events.EventFactory;
import net.ess3.api.server.events.EventListener;
import net.ess3.api.server.events.EventPriority;
import net.ess3.api.server.events.EventType;


public class BukkitEventFactory implements EventFactory
{
	private Plugin plugin;

	public BukkitEventFactory(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public void register(EventListener listener, EventType type, EventPriority priority, boolean ignoreCancelled)
	{
		BukkitListener bukkitListener = new BukkitListener(plugin);
		bukkitListener.register(listener, type, priority, ignoreCancelled);
	}
}
