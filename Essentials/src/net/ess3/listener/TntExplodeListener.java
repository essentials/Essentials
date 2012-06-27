package net.ess3.listener;

import net.ess3.api.IEssentials;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;


public class TntExplodeListener implements Listener, Runnable
{
	private final transient IEssentials ess;
	private transient AtomicBoolean enabled = new AtomicBoolean(false);
	private transient int timer = -1;

	public TntExplodeListener(final IEssentials ess)
	{
		super();
		this.ess = ess;
	}

	public void enable()
	{
		if (enabled.compareAndSet(false, true))
		{
			timer = ess.scheduleSyncDelayedTask(this, 1000);
			return;
		}
		if (timer != -1)
		{
			ess.getServer().getScheduler().cancelTask(timer);
			timer = ess.scheduleSyncDelayedTask(this, 1000);
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
		if (event.blockList().size() < 1)
		{
			return;
		}
		event.setCancelled(true);
		event.getLocation().getWorld().createExplosion(event.getLocation(), 0F);
	}

	@Override
	public void run()
	{
		enabled.set(false);
	}
}
