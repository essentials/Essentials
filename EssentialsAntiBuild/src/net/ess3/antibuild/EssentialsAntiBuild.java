package net.ess3.antibuild;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsAntiBuild extends JavaPlugin implements IAntiBuild
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private transient EssentialsConnect ess = null;
	private transient AntiBuildHolder settings = null;

	@Override
	public void onEnable()
	{
		final PluginManager pm = this.getServer().getPluginManager();
		final Plugin essPlugin = pm.getPlugin("Essentials");
		if (essPlugin == null || !essPlugin.isEnabled())
		{
			return;
		}
		ess = new EssentialsConnect(essPlugin, this);

		final EssentialsAntiBuildListener blockListener = new EssentialsAntiBuildListener(this);
		pm.registerEvents(blockListener, this);
	}

	@Override
	public boolean checkProtectionItems(final int id)
	{
		//final List<Integer> itemList = settingsList.get(list);
		//return itemList != null && !itemList.isEmpty() && itemList.contains(id);
		return false;
	}

	@Override
	public EssentialsConnect getEssentialsConnect()
	{
		return ess;
	}	
	
	@Override
	public AntiBuildHolder getSettings()
	{
		return settings;
	}

	@Override
	public void setSettings(final AntiBuildHolder settings)
	{
		this.settings = settings;
	}
}
