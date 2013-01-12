package net.ess3.metrics;

import java.util.logging.Level;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class MetricsListener implements Listener
{
	private final transient IEssentials ess;
	private final transient MetricsStarter starter;

	public MetricsListener(final IEssentials parent, final MetricsStarter starter)
	{
		this.ess = parent;
		this.starter = starter;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		final IUser player = ess.getUserMap().getUser(event.getPlayer());

		ISettings settings = ess.getSettings();
		if (settings.getData().getGeneral().getMetricsEnabled() == null && (Permissions.ESSENTIALS.isAuthorized(
				event.getPlayer()) || event.getPlayer().hasPermission("bukkit.broadcast.admin")))
		{
			player.sendMessage(_("metrics1"));
			player.sendMessage(_("metrics2"));
			ess.getLogger().log(Level.INFO, _("metrics3"));
			settings.getData().getGeneral().setMetricsEnabled(true);
			settings.queueSave();
			ess.getPlugin().scheduleAsyncDelayedTask(starter, 5 * 1200);
		}
	}
}
