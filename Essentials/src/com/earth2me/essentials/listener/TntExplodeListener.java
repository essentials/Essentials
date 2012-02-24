package com.earth2me.essentials.listener;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.craftbukkit.FakeExplosion;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;


public class TntExplodeListener implements Listener, Runnable
{
	private final transient IContext context;
	private transient AtomicBoolean enabled = new AtomicBoolean(false);
	private transient int timer = -1;

	public TntExplodeListener(final IContext ess)
	{
		super();
		this.context = ess;
	}

	public void enable()
	{
		if (enabled.compareAndSet(false, true))
		{
			timer = context.getScheduler().scheduleSyncDelayedTask(this, 1000);
			return;
		}
		if (timer != -1)
		{
			context.getServer().getScheduler().cancelTask(timer);
			timer = context.getScheduler().scheduleSyncDelayedTask(this, 1000);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		if (!enabled.get())
		{
			return;
		}
		if (event.getEntity() instanceof LivingEntity)
		{
			return;
		}
		FakeExplosion.createExplosion(event, context.getServer(), context.getServer().getOnlinePlayers());
		event.setCancelled(true);
	}

	@Override
	public void run()
	{
		enabled.set(false);
	}
}
