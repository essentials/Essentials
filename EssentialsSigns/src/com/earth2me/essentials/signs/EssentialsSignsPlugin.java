package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.EssentialsPlugin;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import org.bukkit.plugin.PluginManager;


public final class EssentialsSignsPlugin extends EssentialsPlugin implements ISignsPlugin
{
	private transient SignsSettingsComponent config;

	@Override
	public void onEnable()
	{
		// Call this FIRST.
		super.onEnable();

		final PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new SignBlockListener(getContext(), this), this);
		pluginManager.registerEvents(new SignPlayerListener(getContext(), this), this);
		pluginManager.registerEvents(new SignEntityListener(getContext(), this), this);

		config = new SignsSettingsComponent(getContext(), this);

		getContext().getLogger().info($("loadinfo", this.getDescription().getName(), this.getDescription().getVersion(), "essentials team"));
	}

	@Override
	public SignsSettingsComponent getSignsConfig()
	{
		return config;
	}
}
