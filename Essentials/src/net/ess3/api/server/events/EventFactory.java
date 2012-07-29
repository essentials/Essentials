package net.ess3.api.server.events;


public interface EventFactory
{
	public void register(EventListener listener, EventType type, EventPriority priority, boolean ignoreCancelled);
}
