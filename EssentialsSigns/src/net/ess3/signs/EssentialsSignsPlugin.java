package net.ess3.signs;

import java.util.logging.Level;
import java.util.logging.Logger;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IPlugin;
import net.ess3.signs.listeners.SignBlockListener;
import net.ess3.signs.listeners.SignEntityListener;
import net.ess3.signs.listeners.SignPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsSignsPlugin extends JavaPlugin implements ISignsPlugin
{
	private SignsConfigHolder config;

	@Override
	public void onEnable()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		final IPlugin plugin = (IPlugin)pluginManager.getPlugin("Essentials-3");
		final IEssentials ess = (IEssentials)plugin.getEssentials();
		if (!this.getDescription().getVersion().equals(plugin.getDescription().getVersion()))
		{
			getLogger().log(Level.WARNING, _("§4Version mismatch! Please update all Essentials jars to the same version."));
		}
		if (!plugin.isEnabled())
		{
			this.setEnabled(false);
			return;
		}

		final SignBlockListener signBlockListener = new SignBlockListener(ess, this);
		pluginManager.registerEvents(signBlockListener, this);

		final SignPlayerListener signPlayerListener = new SignPlayerListener(ess, this);
		pluginManager.registerEvents(signPlayerListener, this);

		final SignEntityListener signEntityListener = new SignEntityListener(ess, this);
		pluginManager.registerEvents(signEntityListener, this);

		config = new SignsConfigHolder(ess, this);
	}

	@Override
	public void onDisable()
	{
	}

	@Override
	public SignsConfigHolder getSettings()
	{
		return config;
	}
}
