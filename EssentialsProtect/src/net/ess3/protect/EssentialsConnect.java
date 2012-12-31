package net.ess3.protect;

import static net.ess3.I18n._;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import net.ess3.api.IEssentials;
import net.ess3.bukkit.BukkitPlugin;


public class EssentialsConnect
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private final transient IEssentials ess;
	private final transient IProtect protect;

	public EssentialsConnect(final Plugin essPlugin, final Plugin essProtect)
	{
		if (!essProtect.getDescription().getVersion().equals(essPlugin.getDescription().getVersion()))
		{
			LOGGER.log(Level.WARNING, _("versionMismatchAll"));
		}
		ess = ((BukkitPlugin)essPlugin).getEssentials();
		protect = (IProtect)essProtect;
		protect.setSettings(new ProtectHolder(ess));
	}

	public IEssentials getEssentials()
	{
		return ess;
	}
}