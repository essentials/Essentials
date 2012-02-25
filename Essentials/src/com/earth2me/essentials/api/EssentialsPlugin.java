package com.earth2me.essentials.api;

import com.earth2me.essentials.components.ComponentPlugin;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import java.util.logging.Level;
import org.bukkit.plugin.PluginManager;


/**
 * Defines a base type for sub-plugins for Essentials.
 */
public abstract class EssentialsPlugin extends ComponentPlugin implements IEssentialsPlugin
{
	private transient IContext context;

	@Override
	public void onEnable()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		setContext(((IEssentials)pluginManager.getPlugin("Essentials3")).getContext());

		if (!this.getDescription().getVersion().equals(context.getEssentials().getDescription().getVersion()))
		{
			getContext().getLogger().log(Level.WARNING, _("versionMismatchAll"));
		}

		if (!context.getEssentials().isEnabled())
		{
			this.setEnabled(false);
			return;
		}

		super.onEnable();
	}

	@Override
	public IContext getContext()
	{
		return context;
	}

	private void setContext(IContext context)
	{
		this.context = context;
	}
}
