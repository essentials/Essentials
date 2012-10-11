package net.ess3.antibuild;

import java.util.logging.Level;
import java.util.logging.Logger;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.bukkit.BukkitPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class EssentialsConnect
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private final transient IEssentials ess;
	private final transient IAntiBuild antib;

	public EssentialsConnect(Plugin essPlugin, Plugin essProtect)
	{
		if (!essProtect.getDescription().getVersion().equals(essPlugin.getDescription().getVersion()))
		{
			LOGGER.log(Level.WARNING, _("versionMismatchAll"));
		}
		ess = ((BukkitPlugin)essPlugin).getEssentials();
		antib = (IAntiBuild)essProtect;
		antib.setSettings(new AntiBuildHolder(ess));	
	}

	public void onDisable()
	{
	}

	public IEssentials getEssentials()
	{
		return ess;
	}

	public void alert(final IUser user, final String item, final String type)
	{
		final Location loc = user.getPlayer().getLocation();
		final String warnMessage = _("alertFormat", user.getName(), type, item,
									 loc.getWorld().getName() + "," + loc.getBlockX() + ","
									 + loc.getBlockY() + "," + loc.getBlockZ());
		LOGGER.log(Level.WARNING, warnMessage);
		for (Player p : ess.getServer().getOnlinePlayers())
		{
			final IUser alertUser = ess.getUserMap().getUser(p);
			if (Permissions.ALERTS.isAuthorized(alertUser))
			{
				alertUser.sendMessage(warnMessage);
			}
		}
	}
}