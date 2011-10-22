package com.earth2me.essentials;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;


public class EssentialsPluginListener extends ServerListener implements IConf
{
	private final transient IEssentials ess;
	private static final Logger LOGGER = Logger.getLogger("Minecraft");

	public EssentialsPluginListener(final IEssentials ess)
	{
		this.ess = ess;
	}

	@Override
	public void onPluginEnable(final PluginEnableEvent event)
	{
		ess.getPermissionsHandler().checkPermissions();
	}

	@Override
	public void onPluginDisable(final PluginDisableEvent event)
	{
		ess.getPermissionsHandler().checkPermissions();
	}

	@Override
	public void reloadConfig()
	{
		ess.getPermissionsHandler().setUseSuperperms(ess.getSettings().useBukkitPermissions());
		ess.getPermissionsHandler().checkPermissions();
	}
}
