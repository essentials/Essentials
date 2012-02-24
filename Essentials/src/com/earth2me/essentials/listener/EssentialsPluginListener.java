package com.earth2me.essentials.listener;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IReloadable;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.perm.GmGroups;
import com.earth2me.essentials.perm.VaultGroups;
import com.earth2me.essentials.register.payment.PaymentMethods;
import com.earth2me.essentials.settings.General;
import com.earth2me.essentials.settings.GroupsComponent;
import java.util.logging.Level;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;


public class EssentialsPluginListener implements Listener, IReloadable
{
	private final transient IContext ess;

	public EssentialsPluginListener(final IContext ess)
	{
		super();
		this.ess = ess;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(final PluginEnableEvent event)
	{
		checkGroups();
		//ess.getPermissionsHandler().checkPermissions();
		ess.getCommands().addPlugin(event.getPlugin());
		if (!PaymentMethods.hasMethod() && PaymentMethods.setMethod(ess.getServer().getPluginManager()))
		{
			ess.getLogger().log(Level.INFO, "Payment method found ({0} version: {1})", new Object[]
					{
						PaymentMethods.getMethod().getName(), PaymentMethods.getMethod().getVersion()
					});
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(final PluginDisableEvent event)
	{
		checkGroups();
		//ess.getPermissionsHandler().checkPermissions();
		ess.getCommands().removePlugin(event.getPlugin());
		// Check to see if the plugin thats being disabled is the one we are using
		if (PaymentMethods.hasMethod() && PaymentMethods.checkDisabled(event.getPlugin()))
		{
			PaymentMethods.reset();
			ess.getLogger().log(Level.INFO, "Payment method was disabled. No longer accepting payments.");
		}
	}

	@Override
	public void onReload()
	{
		//ess.getPermissionsHandler().setUseSuperperms(ess.getSettings().useBukkitPermissions());
	}

	private void checkGroups()
	{
		ISettingsComponent settings = ess.getSettings();
		settings.acquireReadLock();
		General.GroupStorage storage = General.GroupStorage.FILE;
		try
		{
			storage = settings.getData().getGeneral().getGroupStorage();
		}
		finally
		{
			settings.unlock();
		}
		if (storage == General.GroupStorage.GROUPMANAGER)
		{
			Plugin groupManager = ess.getServer().getPluginManager().getPlugin("GroupManager");
			if (groupManager != null && groupManager.isEnabled() && !(ess.getGroups() instanceof GmGroups))
			{
				if (ess.getGroups() instanceof GroupsComponent)
				{
					ess.removeReloadListener((GroupsComponent)ess.getGroups());
				}
				ess.setGroups(new GmGroups(ess, groupManager));
				return;
			}
		}
		if (storage == General.GroupStorage.VAULT)
		{
			Plugin vault = ess.getServer().getPluginManager().getPlugin("Vault");
			if (vault != null && vault.isEnabled() && !(ess.getGroups() instanceof VaultGroups))
			{
				if (ess.getGroups() instanceof GroupsComponent)
				{
					ess.removeReloadListener((GroupsComponent)ess.getGroups());
				}
				ess.setGroups(new VaultGroups(ess));
				return;
			}
		}
		if (!(ess.getGroups() instanceof GroupsComponent))
		{
			ess.setGroups(new GroupsComponent(ess));
			ess.addReloadListener((GroupsComponent)ess.getGroups());
		}
	}
}
