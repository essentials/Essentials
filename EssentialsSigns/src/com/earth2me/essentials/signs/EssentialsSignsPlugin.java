package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.EssentialsPlugin;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import org.bukkit.plugin.PluginManager;


public class EssentialsSignsPlugin extends EssentialsPlugin implements ISignsPlugin
{
	private transient SignsConfigHolder config;

	@Override
	public void onEnable()
	{
		// Call this FIRST.
		super.onEnable();

		final PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new SignBlockListener(getContext(), this), this);
		pluginManager.registerEvents(new SignPlayerListener(getContext(), this), this);
		pluginManager.registerEvents(new SignEntityListener(getContext(), this), this);

		config = new SignsConfigHolder(getContext(), this);

		getContext().getLogger().info(_("loadinfo", this.getDescription().getName(), this.getDescription().getVersion(), "essentials team"));
	}

	@Override
	public SignsConfigHolder getSignsConfig()
	{
		return config;
	}
}
