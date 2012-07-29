package net.ess3.bukkit.events;

import net.ess3.api.server.events.EventListener;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;


public abstract class BaseEventExecutor implements EventExecutor
{
	protected final EventListener listener;
	
	public BaseEventExecutor(final EventListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public abstract void execute(Listener ll, Event event) throws EventException;
}
