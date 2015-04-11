package org.mcess.essentials.antibuild;

import org.mcess.essentials.IConf;
import org.mcess.essentials.User;
import net.ess3.api.IEssentials;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mcess.essentials.I18n;


public class EssentialsConnect
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private final transient IEssentials ess;
	private final transient IAntiBuild protect;

	public EssentialsConnect(Plugin essPlugin, Plugin essProtect)
	{
		if (!essProtect.getDescription().getVersion().equals(essPlugin.getDescription().getVersion()))
		{
			LOGGER.log(Level.WARNING, I18n.tl("versionMismatchAll"));
		}
		ess = (IEssentials)essPlugin;
		protect = (IAntiBuild)essProtect;
		AntiBuildReloader pr = new AntiBuildReloader();
		pr.reloadConfig();
		ess.addReloadListener(pr);
	}

	public void onDisable()
	{
	}

	public IEssentials getEssentials()
	{
		return ess;
	}

	public void alert(final User user, final String item, final String type)
	{
		final Location loc = user.getLocation();
		final String warnMessage = I18n.tl("alertFormat", user.getName(), type, item,
				loc.getWorld().getName() + "," + loc.getBlockX() + ","
						+ loc.getBlockY() + "," + loc.getBlockZ());
		LOGGER.log(Level.WARNING, warnMessage);
		for (Player p : ess.getServer().getOnlinePlayers())
		{
			final User alertUser = ess.getUser(p);
			if (alertUser.isAuthorized("essentials.protect.alerts"))
			{
				alertUser.sendMessage(warnMessage);
			}
		}
	}


	private class AntiBuildReloader implements IConf
	{
		@Override
		public void reloadConfig()
		{
			for (AntiBuildConfig protectConfig : AntiBuildConfig.values())
			{
				if (protectConfig.isList())
				{
					protect.getSettingsList().put(protectConfig, ess.getSettings().getProtectList(protectConfig.getConfigName()));
				}
				else
				{
					protect.getSettingsBoolean().put(protectConfig, ess.getSettings().getProtectBoolean(protectConfig.getConfigName(), protectConfig.getDefaultValueBoolean()));
				}

			}

		}
	}
}
