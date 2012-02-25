package com.earth2me.essentials.listeners;

import com.earth2me.essentials.api.*;
import com.earth2me.essentials.perm.GmGroupsComponent;
import com.earth2me.essentials.perm.VaultGroupsComponent;
import com.earth2me.essentials.register.payment.PaymentMethods;
import com.earth2me.essentials.components.settings.General;
import com.earth2me.essentials.components.settings.GroupStorage;
import com.earth2me.essentials.components.settings.groups.GroupsComponent;
import java.util.logging.Level;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;


public class EssentialsPluginListener implements Listener, IReloadable
{
	private final transient IContext context;

	public EssentialsPluginListener(final IContext ess)
	{
		super();
		this.context = ess;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(final PluginEnableEvent event)
	{
		checkGroups();
		//ess.getPermissionsHandler().checkPermissions();
		context.getCommands().addPlugin(event.getPlugin());
		if (!PaymentMethods.hasMethod() && PaymentMethods.setMethod(context.getServer().getPluginManager()))
		{
			context.getLogger().log(Level.INFO, "Payment method found ({0} version: {1})", new Object[]
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
		context.getCommands().removePlugin(event.getPlugin());
		// Check to see if the plugin thats being disabled is the one we are using
		if (PaymentMethods.hasMethod() && PaymentMethods.checkDisabled(event.getPlugin()))
		{
			PaymentMethods.reset();
			context.getLogger().log(Level.INFO, "Payment method was disabled. No longer accepting payments.");
		}
	}

	@Override
	public void reload()
	{
		//ess.getPermissionsHandler().setUseSuperperms(ess.getSettings().useBukkitPermissions());
	}

	private void checkGroups()
	{
		ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();
		GroupStorage storage = GroupStorage.FILE;
		try
		{
			storage = settings.getData().getGeneral().getGroupStorage();
		}
		finally
		{
			settings.unlock();
		}
		if (storage == GroupStorage.GROUPMANAGER)
		{
			Plugin groupManager = context.getServer().getPluginManager().getPlugin("GroupManager");
			if (groupManager != null && groupManager.isEnabled() && !(context.getGroups() instanceof GmGroupsComponent))
			{
				if (context.getGroups() instanceof IGroupsComponent)
				{
					context.getEssentials().remove(context.getGroups());
				}

				// TODO Come up with a better way of doing this that doesn't involve casting to Context.
				((Context)context).setGroups(new GmGroupsComponent(context, groupManager));
				return;
			}
		}
		if (storage == GroupStorage.VAULT)
		{
			Plugin vault = context.getServer().getPluginManager().getPlugin("Vault");
			if (vault != null && vault.isEnabled() && !(context.getGroups() instanceof VaultGroupsComponent))
			{
				if (context.getGroups() instanceof IGroupsComponent)
				{
					context.getEssentials().remove(context.getGroups());
				}
				// TODO Find a better way to do this.
				((Context)context).setGroups(new VaultGroupsComponent(context));
				return;
			}
		}
		if (!(context.getGroups() instanceof IGroupsComponent))
		{
			// TODO Find a better way to do this.
			((Context)context).setGroups(new GroupsComponent(context));
			context.getEssentials().add(context.getGroups());
		}
	}
}
