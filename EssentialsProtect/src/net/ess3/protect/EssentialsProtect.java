package net.ess3.protect;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsProtect extends JavaPlugin implements IProtect
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private transient EssentialsConnect ess = null;
	private transient ProtectHolder settings = null;

	@Override
	public void onEnable()
	{
		final PluginManager pm = this.getServer().getPluginManager();
		final Plugin essPlugin = pm.getPlugin("Essentials-3");
		if (essPlugin == null || !essPlugin.isEnabled())
		{
			enableEmergencyMode(pm);
			return;
		}
		ess = new EssentialsConnect(essPlugin, this);

		final EssentialsProtectBlockListener blockListener = new EssentialsProtectBlockListener(this);
		pm.registerEvents(blockListener, this);

		final EssentialsProtectEntityListener entityListener = new EssentialsProtectEntityListener(this);
		pm.registerEvents(entityListener, this);

		final EssentialsProtectWeatherListener weatherListener = new EssentialsProtectWeatherListener(this);
		pm.registerEvents(weatherListener, this);
	}

	private void enableEmergencyMode(final PluginManager pm)
	{
		final EmergencyListener emListener = new EmergencyListener();
		pm.registerEvents(emListener, this);

		for (Player player : getServer().getOnlinePlayers())
		{
			player.sendMessage("Essentials Protect is in emergency mode. Check your log for errors."); //TODO: tl this
		}
		LOGGER.log(Level.SEVERE, "Essentials not installed or failed to load. Essenials Protect is in emergency mode now."); //TODO: tl this
	}

	@Override
	public EssentialsConnect getEssentialsConnect()
	{
		return ess;
	}

	@Override
	public ProtectHolder getSettings()
	{
		return settings;
	}

	@Override
	public void setSettings(final ProtectHolder settings)
	{
		this.settings = settings;
	}
}
