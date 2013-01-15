package net.ess3.protect;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;


public class EssentialsProtectWeatherListener implements Listener
{
	private final IProtect prot;

	public EssentialsProtectWeatherListener(final IProtect prot)
	{
		this.prot = prot;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWeatherChange(final WeatherChangeEvent event)
	{
		final ProtectHolder settings = prot.getSettings();

		if (!event.isCancelled() && settings.getData().isDisableStorm() && event.toWeatherState())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLightningStrike(final LightningStrikeEvent event)
	{
		final ProtectHolder settings = prot.getSettings();

		if (!event.isCancelled() && settings.getData().isDisableLighting())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onThunderChange(final ThunderChangeEvent event)
	{
		final ProtectHolder settings = prot.getSettings();

		if (!event.isCancelled() && settings.getData().isDisableThunder() && event.toThunderState())
		{
			event.setCancelled(true);
		}
	}
}
