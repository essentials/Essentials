package net.ess3.protect;

import java.util.logging.Level;
import java.util.logging.Logger;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.bukkit.BukkitPlugin;
import org.bukkit.plugin.Plugin;


public class EssentialsConnect
{
	private final IEssentials ess;
	private final IProtect protect;

	public EssentialsConnect(final Plugin essPlugin, final Plugin essProtect)
	{
		if (!essProtect.getDescription().getVersion().equals(essPlugin.getDescription().getVersion()))
		{
			essPlugin.getLogger().log(Level.WARNING, _("§4Version mismatch! Please update all Essentials jars to the same version."));
		}
		ess = ((BukkitPlugin)essPlugin).getEssentials();
		protect = (IProtect)essProtect;
		ProtectHolder settings = new ProtectHolder(ess);
		protect.setSettings(settings);
		ess.addReloadListener(settings);
	}

	public IEssentials getEssentials()
	{
		return ess;
	}
}
