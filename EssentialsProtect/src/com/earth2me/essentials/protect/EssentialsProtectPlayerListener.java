package com.earth2me.essentials.protect;

import com.earth2me.essentials.protect.settings.ProtectSettingsComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class EssentialsProtectPlayerListener implements Listener
{
	private final transient IEssentialsProtectPlugin parent;

	public EssentialsProtectPlayerListener(final IEssentialsProtectPlugin parent)
	{
		this.parent = parent;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		// Do not return if cancelled, because the interact event has 2 cancelled states.
		final Player user = event.getPlayer();

		final ProtectSettingsComponent settings = parent.getSettings();
		settings.acquireReadLock();
		try
		{
			if (event.hasItem()
				&& (event.getItem().getType() == Material.WATER_BUCKET
					|| event.getItem().getType() == Material.LAVA_BUCKET)
				&& !Permissions.BUILD.isAuthorized(user))
			{
				if (settings.getData().isWarnOnBuildDisallow())
				{
					user.sendMessage($("buildAlert"));
				}
				event.setCancelled(true);
				return;
			}

			if (!Permissions.INTERACT.isAuthorized(user))
			{
				if (settings.getData().isWarnOnBuildDisallow())
				{
					user.sendMessage($("buildAlert"));
				}
				event.setCancelled(true);
				return;
			}

			final ItemStack item = event.getItem();
			if (item != null
				&& !ItemUsePermissions.getPermission(item.getType()).isAuthorized(user))
			{
				event.setCancelled(true);
				return;
			}

			if (Permissions.OWNERINFO.isAuthorized(user) && event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				final StringBuilder stringBuilder = new StringBuilder();
				boolean first = true;
				final Block blockClicked = event.getClickedBlock();
				for (String owner : parent.getStorage().getOwners(blockClicked))
				{
					if (!first)
					{
						stringBuilder.append(", ");
					}
					first = false;
					stringBuilder.append(owner);
				}
				final String ownerNames = stringBuilder.toString();
				if (ownerNames != null && !ownerNames.isEmpty())
				{
					user.sendMessage($("protectionOwner", ownerNames));
				}
			}
			if (item != null
				&& !Permissions.ALERTS_NOTRIGGER.isAuthorized(user)
				&& settings.getData().getAlertOnUse().contains(item.getType()))
			{
				parent.getContext().getMessager().alert(user, item.getType().toString(), $("alertUsed"), Permissions.ALERTS);
			}
		}
		finally
		{
			settings.unlock();
		}
	}
}
