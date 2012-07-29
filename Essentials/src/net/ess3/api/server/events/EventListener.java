package net.ess3.api.server.events;

import net.ess3.api.IUser;
import net.ess3.api.ondemand.OnDemand;
import net.ess3.api.server.Block;


public abstract class EventListener
{
	private static EventFactory eventFactory;

	public final void register(EventType type, EventPriority priority, boolean ignoreCancelled)
	{
		eventFactory.register(this, type, priority, ignoreCancelled);
	}

	public boolean onBlockPlace(Block block, OnDemand<IUser> user)
	{
		return true;
	}
}
