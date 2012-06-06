package com.earth2me.essentials.listener;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.utils.textreader.IText;
import com.earth2me.essentials.utils.textreader.KeywordReplacer;
import com.earth2me.essentials.utils.textreader.TextInput;
import com.earth2me.essentials.utils.textreader.TextPager;
import com.earth2me.essentials.user.UserData.TimestampType;
import com.earth2me.essentials.utils.LocationUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Cleanup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public class EssentialsPlayerListener implements Listener
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private final transient Server server;
	private final transient IEssentials ess;

	public EssentialsPlayerListener(final IEssentials parent)
	{
		super();
		this.ess = parent;
		this.server = parent.getServer();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		final IUser user = ess.getUser(event.getPlayer());
		user.updateCompass();
		user.updateDisplayName();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(final PlayerChatEvent event)
	{
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireReadLock();
		if (user.getData().isMuted())
		{
			event.setCancelled(true);
			user.sendMessage(_("playerMuted"));
			LOGGER.info(_("mutedUserSpeaks", user.getName()));
		}
		final Iterator<Player> it = event.getRecipients().iterator();
		while (it.hasNext())
		{
			final IUser player = ess.getUser(it.next());
			if (player.isIgnoringPlayer(user.getName()))
			{
				it.remove();
			}
		}
		user.updateActivity(true);
		user.updateDisplayName();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerMove(final PlayerMoveEvent event)
	{
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireReadLock();
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();

		if (user.getData().isAfk() && settings.getData().getCommands().getAfk().isFreezeAFKPlayers())
		{
			final Location from = event.getFrom();
			final Location to = event.getTo().clone();
			to.setX(from.getX());
			to.setY(from.getY());
			to.setZ(from.getZ());
			try
			{
				event.setTo(LocationUtil.getSafeDestination(to));
			}
			catch (Exception ex)
			{
				event.setTo(to);
			}
			return;
		}

		final Location afk = user.getAfkPosition();
		if (afk == null || !event.getTo().getWorld().equals(afk.getWorld()) || afk.distanceSquared(event.getTo()) > 9)
		{
			user.updateActivity(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireReadLock();
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		if (settings.getData().getCommands().getGod().isRemoveOnDisconnect() && user.isGodModeEnabled())
		{
			user.toggleGodModeEnabled();
		}
		if (user.getData().getInventory() != null)
		{
			user.getInventory().setContents(user.getData().getInventory().getBukkitInventory());
			user.getData().setInventory(null);
		}
		user.updateActivity(false);
		user.dispose();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		ess.getBackup().startTask();
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireWriteLock();

		user.updateDisplayName();
		user.getData().setIpAddress(user.getAddress().getAddress().getHostAddress());
		user.updateActivity(false);
		if (Permissions.SLEEPINGIGNORED.isAuthorized(user))
		{
			user.setSleepingIgnored(true);
		}

		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();

		if (!settings.getData().getCommands().isDisabled("motd") && Permissions.MOTD.isAuthorized(user))
		{
			try
			{
				final IText input = new TextInput(user, "motd", true, ess);
				final IText output = new KeywordReplacer(input, user, ess);
				final TextPager pager = new TextPager(output, true);
				pager.showPage("1", null, "motd", user);
			}
			catch (IOException ex)
			{
				if (settings.getData().getGeneral().isDebug())
				{
					LOGGER.log(Level.WARNING, ex.getMessage(), ex);
				}
				else
				{
					LOGGER.log(Level.WARNING, ex.getMessage());
				}
			}
		}

		if (!settings.getData().getCommands().isDisabled("mail") && Permissions.MAIL.isAuthorized(user))
		{
			final List<String> mail = user.getData().getMails();
			if (mail == null || mail.isEmpty())
			{
				user.sendMessage(_("noNewMail"));
			}
			else
			{
				user.sendMessage(_("youHaveNewMail", mail.size()));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(final PlayerLoginEvent event)
	{
		switch (event.getResult())
		{
		case ALLOWED:
		case KICK_FULL:
		case KICK_BANNED:
			break;
		default:
			return;
		}
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireWriteLock();
		user.getData().setNpc(false);

		final long currentTime = System.currentTimeMillis();
		final boolean banExpired = user.checkBanTimeout(currentTime);
		user.checkMuteTimeout(currentTime);
		user.checkJailTimeout(currentTime);

		if (!banExpired && (user.isBanned() || event.getResult() == Result.KICK_BANNED))
		{
			final String banReason = user.getData().getBan() == null ? "" : user.getData().getBan().getReason();
			event.disallow(Result.KICK_BANNED, banReason == null || banReason.isEmpty() || banReason.equalsIgnoreCase("ban") ? _("defaultBanReason") : banReason);
			return;
		}

		if (server.getOnlinePlayers().length >= server.getMaxPlayers() && !Permissions.JOINFULLSERVER.isAuthorized(user))
		{
			event.disallow(Result.KICK_FULL, _("serverFull"));
			return;
		}
		event.allow();

		user.setTimestamp(TimestampType.LOGIN, System.currentTimeMillis());
		user.updateCompass();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleport(final PlayerTeleportEvent event)
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		//There is TeleportCause.COMMMAND but plugins have to actively pass the cause in on their teleports.
		if ((event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.COMMAND) && settings.getData().getCommands().getBack().isRegisterBackInListener())
		{
			final IUser user = ess.getUser(event.getPlayer());
			user.setLastLocation();
		}

	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerEggThrow(final PlayerEggThrowEvent event)
	{
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireReadLock();
		final ItemStack hand = new ItemStack(Material.EGG, 1);
		if (user.getData().hasUnlimited(hand.getType()))
		{
			user.getInventory().addItem(hand);
			user.updateInventory();
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event)
	{
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireReadLock();
		if (user.getData().hasUnlimited(event.getBucket()))
		{
			event.getItemStack().setType(event.getBucket());
			ess.scheduleSyncDelayedTask(new Runnable()
			{
				@Override
				public void run()
				{
					user.updateInventory();
				}
			});
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event)
	{
		final IUser user = ess.getUser(event.getPlayer());
		final String cmd = event.getMessage().toLowerCase(Locale.ENGLISH).split(" ")[0].replace("/", "").toLowerCase(Locale.ENGLISH);
		final List<String> commands = Arrays.asList("msg", "r", "mail", "m", "t", "emsg", "tell", "er", "reply", "ereply", "email");
		if (commands.contains(cmd))
		{
			for (Player player : ess.getServer().getOnlinePlayers())
			{
				@Cleanup
				IUser spyer = ess.getUser(player);
				spyer.acquireReadLock();
				if (spyer.getData().isSocialspy() && !user.equals(spyer))
				{
					player.sendMessage(user.getDisplayName() + " : " + event.getMessage());
				}
			}
		}
		if (!cmd.equalsIgnoreCase("afk"))
		{
			user.updateActivity(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChangedWorld(final PlayerChangedWorldEvent event)
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireReadLock();
		if (settings.getData().getChat().getChangeDisplayname())
		{
			user.updateDisplayName();
		}
		if (!settings.getData().getWorldOptions(event.getPlayer().getLocation().getWorld().getName()).isGodmode() && !Permissions.NOGOD_OVERRIDE.isAuthorized(user))
		{
			if (user.getData().isGodmode())
			{
				user.sendMessage(_("noGodWorldWarning"));
			}
		}
		if (settings.getData().getCommands().getTpa().isCancelTpRequestsOnWorldChange())
		{
			if (user.getTeleportRequester() != null)
			{
				user.requestTeleport(null, false);
				user.sendMessage(_("teleportRequestsCancelledWorldChange"));
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireReadLock();
		user.updateActivity(true);
		switch (event.getAction())
		{
		case RIGHT_CLICK_BLOCK:
			if (event.isCancelled())
			{
				return;
			}
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			if (settings.getData().getCommands().getHome().isUpdateBedAtDaytime() && event.getClickedBlock().getType() == Material.BED_BLOCK)
			{
				event.getPlayer().setBedSpawnLocation(event.getClickedBlock().getLocation());
			}
			break;
		case LEFT_CLICK_AIR:
		case LEFT_CLICK_BLOCK:
			if (user.getData().hasPowerTools() && user.getData().isPowerToolsEnabled())
			{
				if (usePowertools(user))
				{
					event.setCancelled(true);
				}
			}
			break;
		default:
			break;
		}
	}

	private boolean usePowertools(final IUser user)
	{
		final ItemStack is = user.getItemInHand();
		int id;
		if (is == null || (id = is.getTypeId()) == 0)
		{
			return false;
		}
		
		final List<String> commandList = user.getData().getPowertool(is.getType());
		if (commandList == null || commandList.isEmpty())
		{
			return false;
		}
		boolean used = false;
		// We need to loop through each command and execute
		for (final String command : commandList)
		{
			if (command.matches(".*\\{player\\}.*"))
			{
				//user.sendMessage("Click a player to use this command");
				continue;
			}
			else if (command.startsWith("c:"))
			{
				used = true;
				user.chat(command.substring(2));
			}
			else
			{
				used = true;
				ess.scheduleSyncDelayedTask(
						new Runnable()
						{
							@Override
							public void run()
							{
								user.getServer().dispatchCommand(user.getBase(), command);
							}
						});
			}
		}
		return used;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerPickupItem(final PlayerPickupItemEvent event)
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		if (!settings.getData().getCommands().getAfk().isDisableItemPickupWhileAfk())
		{
			return;
		}
		@Cleanup
		final IUser user = ess.getUser(event.getPlayer());
		user.acquireReadLock();
		if (user.getData().isAfk())
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickEvent(final InventoryClickEvent event)
	{
		if (event.getView().getTopInventory().getType() == InventoryType.PLAYER)
		{
			final IUser user = ess.getUser(event.getWhoClicked());
			final InventoryHolder invHolder = event.getView().getTopInventory().getHolder();
			if (invHolder != null && invHolder instanceof HumanEntity)
			{
				final IUser invOwner = ess.getUser((HumanEntity)invHolder);
				if (user.isInvSee() && (!Permissions.INVSEE_MODIFY.isAuthorized(user)
										|| Permissions.INVSEE_PREVENT_MODIFY.isAuthorized(invOwner)
										|| !invOwner.isOnline()))
				{
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryCloseEvent(final InventoryCloseEvent event)
	{
		if (event.getView().getTopInventory().getType() == InventoryType.PLAYER)
		{
			final IUser user = ess.getUser(event.getPlayer());
			user.setInvSee(false);
		}
	}
}
