package net.ess3.metrics;

import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class MetricsListener implements Listener
{
	private final transient Server server;
	private final transient IEssentials ess;
	private final transient MetricsStarter starter;

	public MetricsListener(final IEssentials parent, final MetricsStarter starter)
	{
		this.ess = parent;
		this.server = parent.getServer();
		this.starter = starter;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		/*final IUser player = ess.getUserMap().getUser(event.getPlayer());
		if (ess.getSettings().isMetricsEnabled() == false && (player.isAuthorized("essentials.essentials") || player.isAuthorized("bukkit.broadcast.admin")))
		{
			player.sendMessage("PluginMetrics collects minimal statistic data, starting in about 5 minutes.");
			player.sendMessage("To opt out, run /essentials opt-out");
			ess.getLogger().log(Level.INFO, "[Metrics] Admin join - Starting 5 minute opt-out period.");
			ess.getSettings().setMetricsEnabled(true);
			ess.getScheduler().scheduleAsyncDelayedTask(ess, starter, 5 * 1200);
		}*/
	}
}
