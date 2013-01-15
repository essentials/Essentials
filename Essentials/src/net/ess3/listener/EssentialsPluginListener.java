package net.ess3.listener;

import java.util.logging.Level;
import net.ess3.api.IEssentials;
import net.ess3.api.IReload;
import net.ess3.api.ISettings;
import net.ess3.economy.register.Methods;
import net.ess3.ranks.GMGroups;
import net.ess3.ranks.RanksStorage;
import net.ess3.ranks.VaultGroups;
import net.ess3.settings.General;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;


public class EssentialsPluginListener implements Listener, IReload
{
	private final IEssentials ess;

	public EssentialsPluginListener(final IEssentials ess)
	{
		super();
		this.ess = ess;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(final PluginEnableEvent event)
	{
		checkGroups();
		ess.getPlugin().onPluginEnable(event.getPlugin());
		ess.getCommandHandler().addPlugin(event.getPlugin());
		if (!Methods.hasMethod() && Methods.setMethod(ess.getServer().getPluginManager()))
		{
			ess.getLogger().log(
					Level.INFO, "Payment method found ({0} version: {1})", new Object[]
					{
						Methods.getMethod().getName(), Methods.getMethod().getVersion()
					});
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(final PluginDisableEvent event)
	{
		checkGroups();
		ess.getPlugin().onPluginDisable(event.getPlugin());
		ess.getCommandHandler().removePlugin(event.getPlugin());
		// Check to see if the plugin thats being disabled is the one we are using
		if (Methods.hasMethod() && Methods.checkDisabled(event.getPlugin()))
		{
			Methods.reset();
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
		ISettings settings = ess.getSettings();
		General.GroupStorage storage = settings.getData().getGeneral().getGroupStorage();

		if (storage == General.GroupStorage.GROUPMANAGER)
		{
			final Plugin groupManager = ess.getServer().getPluginManager().getPlugin("GroupManager");
			if (groupManager != null && groupManager.isEnabled() && !(ess.getRanks() instanceof GMGroups))
			{
				if (ess.getRanks() instanceof RanksStorage)
				{
					ess.removeReloadListener((RanksStorage)ess.getRanks());
				}
				ess.setRanks(new GMGroups(ess, groupManager));
				return;
			}
		}
		if (storage == General.GroupStorage.VAULT)
		{
			final Plugin vault = ess.getServer().getPluginManager().getPlugin("Vault");
			if (vault != null && vault.isEnabled() && !(ess.getRanks() instanceof VaultGroups))
			{
				if (ess.getRanks() instanceof RanksStorage)
				{
					ess.removeReloadListener((RanksStorage)ess.getRanks());
				}
				ess.setRanks(new VaultGroups(ess));
				return;
			}
		}
		if (!(ess.getRanks() instanceof RanksStorage))
		{
			ess.setRanks(new RanksStorage(ess));
			ess.addReloadListener((RanksStorage)ess.getRanks());
		}
	}
}
