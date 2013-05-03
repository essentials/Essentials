package net.ess3.antibuild;

import java.util.logging.Level;
import java.util.logging.Logger;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.bukkit.BukkitPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class EssentialsConnect
{
	private final IEssentials ess;
	private final IAntiBuild antib;

	public EssentialsConnect(Plugin essPlugin, Plugin essProtect)
	{
		if (!essProtect.getDescription().getVersion().equals(essPlugin.getDescription().getVersion()))
		{
			essPlugin.getLogger().log(Level.WARNING, _("§4Version mismatch! Please update all Essentials jars to the same version."));
		}
		ess = ((BukkitPlugin)essPlugin).getEssentials();
		antib = (IAntiBuild)essProtect;
		AntiBuildHolder settings = new AntiBuildHolder(ess);
		antib.setSettings(settings);
		ess.addReloadListener(settings);
	}

	public void onDisable()
	{
	}

	public IEssentials getEssentials()
	{
		return ess;
	}

	public void alert(final Player user, final String item, final String type)
	{
		final Location loc = user.getLocation();
		final String warnMessage = _(
				"alertFormat", user.getName(), type, item, loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
		ess.getLogger().log(Level.WARNING, warnMessage);
		for (Player p : ess.getServer().getOnlinePlayers())
		{
			if (Permissions.ALERTS.isAuthorized(p))
			{
				p.sendMessage(warnMessage);
			}
		}
	}
}
