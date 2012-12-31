package net.ess3.listener;

import static net.ess3.I18n._;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.api.IUserMap;
import net.ess3.permissions.Permissions;
import net.ess3.settings.Commands;
import net.ess3.user.UserData.TimestampType;
import net.ess3.utils.FormatUtil;
import net.ess3.utils.LocationUtil;
import net.ess3.utils.textreader.*;


public class EssentialsPlayerListener implements Listener
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private final transient Server server;
	private final transient IEssentials ess;
	private final transient IUserMap userMap;

	public EssentialsPlayerListener(final IEssentials parent)
	{
		super();
		this.ess = parent;
		userMap = ess.getUserMap();
		this.server = parent.getServer();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		final IUser user = userMap.getUser(event.getPlayer());
		user.updateCompass();
		user.updateDisplayName();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(final AsyncPlayerChatEvent event) // TODO: Does this update work?
	{
		final IUser user = userMap.getUser(event.getPlayer());
		if (user.getData().isMuted())
		{
			event.setCancelled(true);
			user.sendMessage(_("playerMuted"));
			LOGGER.info(_("mutedUserSpeaks", user.getName()));
		}
		final Iterator<Player> it = event.getRecipients().iterator();
		while (it.hasNext())
		{
			final IUser u = userMap.getUser(it.next());
			if (u.getData().getIgnore().contains(user.getName()))
			{
				it.remove();
			}
		}
		user.updateActivity(true);
		user.setDisplayNick();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerMove(final PlayerMoveEvent event)
	{
		final IUser user = userMap.getUser(event.getPlayer());

		final ISettings settings = ess.getSettings();

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
		final String quitMessage = ess.getSettings().getData().getGeneral().getLeaveMessage();
		if (quitMessage != null)
		{
			final IText itOutput = new KeywordReplacer(new SimpleTextInput(quitMessage), userMap.getUser(event.getPlayer()), ess);
			final SimpleTextPager stPager = new SimpleTextPager(itOutput);
			event.setQuitMessage(FormatUtil.replaceFormat(stPager.getString(0)));
		}
		else
		{
			event.setQuitMessage(quitMessage);
		}


		final IUser user = userMap.getUser(event.getPlayer());

		final ISettings settings = ess.getSettings();
		if (settings.getData().getCommands().getGod().isRemoveOnDisconnect() && user.isGodModeEnabled())
		{
			user.setGodModeEnabled(false);
		}
		if (user.isVanished())
		{
			user.toggleVanished();
		}
		if (user.getData().getInventory() != null)
		{
			user.getPlayer().getInventory().setContents(user.getData().getInventory().getBukkitInventory());
			user.getData().setInventory(null);
		}
		user.updateActivity(false);
		//user.getPlayer().dispose();
		if (settings.getData().getGeneral().isPtClearOnQuit())
		{
			user.getData().clearAllPowertools();
			user.queueSave();
			user.sendMessage(_("powerToolClearAll"));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		ess.getPlugin().scheduleAsyncDelayedTask(
				new Runnable()
				{
					@Override
					public void run()
					{
						delayedJoin(event.getPlayer());
					}
				});
/* TODO: Make sure my update is good
		if (!event.getPlayer().isOnline())
		{
			return;
		}

		ess.getBackup().startTask();

		final String joinMessage = ess.getSettings().getData().getGeneral().getJoinMessage();
		if (joinMessage != null)
		{
			final IText itOutput = new KeywordReplacer(new SimpleTextInput(joinMessage), ess.getUserMap().getUser(event.getPlayer()), ess);
			final SimpleTextPager stPager = new SimpleTextPager(itOutput);
			event.setJoinMessage(FormatUtil.replaceFormat(stPager.getString(0)));
		}
		else
		{
			event.setJoinMessage(joinMessage);
		}

		final IUser user = ess.getUserMap().getUser(event.getPlayer());

		user.updateDisplayName();
		user.getData().setIpAddress(user.getPlayer().getAddress().getAddress().getHostAddress());
		user.updateActivity(false);

		for (String p : ess.getVanishedPlayers())
		{
			if (!Permissions.VANISH_SEE_OTHERS.isAuthorized(user))
			{
				user.getPlayer().hidePlayer(ess.getUserMap().getUser(p).getPlayer());
			}
		}

		if (Permissions.SLEEPINGIGNORED.isAuthorized(user))
		{
			user.getPlayer().setSleepingIgnored(true);
		}
		user.queueSave();


		final ISettings settings = ess.getSettings();

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
		}*/
	}

	public void delayedJoin(final Player player)
	{
		if (!player.isOnline())
		{
			return;
		}
		ess.getBackup().startTask();
		final IUser user = userMap.getUser(player);
		user.setDisplayNick();
		user.updateCompass();
		user.getData().setTimestamp(TimestampType.LOGIN, System.currentTimeMillis());
		user.updateActivity(false);

		if (!ess.getVanishedPlayers().isEmpty() && !Permissions.VANISH_SEE_OTHERS.isAuthorized(user))
		{
			for (String p : ess.getVanishedPlayers())
			{
				final Player toVanish = userMap.getUser(p).getPlayer();
				if (toVanish.isOnline())
				{
					user.setVanished(true);
				}
			}
		}

		if (Permissions.SLEEPINGIGNORED.isAuthorized(user))
		{
			ess.getPlugin().scheduleSyncDelayedTask(
					new Runnable()
					{
						@Override
						public void run()
						{
							user.getPlayer().setSleepingIgnored(true);
						}
					});
		}

		final Commands settings = ess.getSettings().getData().getCommands();

		if (!settings.isDisabled("motd") && Permissions.MOTD.isAuthorized(user))
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
				if (ess.getSettings().isDebug())
				{
					LOGGER.log(Level.WARNING, ex.getMessage(), ex);
				}
				else
				{
					LOGGER.log(Level.WARNING, ex.getMessage());
				}
			}
		}

		if (!settings.isDisabled("mail") && Permissions.MAIL.isAuthorized(user))
		{
			final List<String> mail = user.getMails();
			if (mail.isEmpty())
			{
				final String msg = _("noNewMail");
				if (!msg.isEmpty())
				{
					user.sendMessage(msg);
				}
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

		userMap.addPrejoinedPlayer(event.getPlayer());
		final IUser user = userMap.getUser(event.getPlayer());
		userMap.removePrejoinedPlayer(event.getPlayer());
		user.getData().setNpc(false);

		final long currentTime = System.currentTimeMillis();
		final boolean banExpired = user.checkBanTimeout(currentTime);
		user.checkMuteTimeout(currentTime);
		user.checkJailTimeout(currentTime);

		if (!banExpired && (user.isBanned() || event.getResult() == Result.KICK_BANNED))
		{
			final String banReason = user.getData().getBan() == null ? "" : user.getData().getBan().getReason();
			event.disallow(
					Result.KICK_BANNED, banReason == null || banReason.isEmpty() || banReason.equalsIgnoreCase("ban") ? _("defaultBanReason") : banReason);
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
		user.queueSave();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleport(final PlayerTeleportEvent event)
	{

		final ISettings settings = ess.getSettings();
		//There is TeleportCause.COMMMAND but plugins have to actively pass the cause in on their teleports.
		if ((event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.COMMAND) && settings.getData().getCommands().getBack().isRegisterBackInListener())
		{
			final IUser user = userMap.getUser(event.getPlayer());
			user.setLastLocation();
		}

	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerEggThrow(final PlayerEggThrowEvent event)
	{

		final IUser user = userMap.getUser(event.getPlayer());
		final ItemStack hand = new ItemStack(Material.EGG, 1);
		if (user.getData().hasUnlimited(hand.getType()))
		{
			user.getPlayer().getInventory().addItem(hand);
			user.getPlayer().updateInventory();
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event)
	{

		final IUser user = userMap.getUser(event.getPlayer());
		if (user.getData().hasUnlimited(event.getBucket()))
		{
			event.getItemStack().setType(event.getBucket());
			ess.getPlugin().scheduleSyncDelayedTask(
					new Runnable()
					{
						@Override
						public void run()
						{
							user.getPlayer().updateInventory();
						}
					});
		}
	}

	private final Pattern spaceSplit = Pattern.compile(" ");

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event)
	{
		final IUser user = userMap.getUser(event.getPlayer());
		final String cmd = spaceSplit.split(event.getMessage().toLowerCase(Locale.ENGLISH))[0].replace("/", "").toLowerCase(Locale.ENGLISH);
		if (ess.getSettings().getData().getCommands().getSocialspy().getSocialspyCommands().contains(cmd))
		{
			for (Player player : ess.getServer().getOnlinePlayers())
			{
				IUser spyer = userMap.getUser(player);
				if (spyer.getData().isSocialspy() && !user.equals(spyer))
				{
					player.sendMessage(user.getPlayer().getDisplayName() + " : " + event.getMessage());
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
		final ISettings settings = ess.getSettings();

		final IUser user = userMap.getUser(event.getPlayer());
		if (settings.getData().getChat().getChangeDisplayname())
		{
			user.updateDisplayName();
		}
		if (!settings.getData().getWorldOptions(event.getPlayer().getLocation().getWorld().getName()).isGodmode() && !Permissions.NOGOD_OVERRIDE.isAuthorized(
				user))
		{
			if (user.getData().isGodmode())
			{
				user.sendMessage(_("noGodWorldWarning"));
			}
		}
		if (settings.getData().getCommands().getTeleport().isCancelRequestsOnWorldChange())
		{
			if (user.getTeleportRequester() != null)
			{
				user.requestTeleport(null, false);
				user.sendMessage(_("teleportRequestsCancelledWorldChange"));
			}
		}
		if (settings.getData().getGeneral().isPtClearOnWorldChange())
		{
			user.getData().clearAllPowertools();
			user.queueSave();
			user.sendMessage(_("powerToolClearAll"));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		final IUser user = userMap.getUser(event.getPlayer());
		user.updateActivity(true);
		switch (event.getAction())
		{
		case RIGHT_CLICK_BLOCK:
			if (event.isCancelled())
			{
				return;
			}

			final ISettings settings = ess.getSettings();
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
		final ItemStack is = user.getPlayer().getItemInHand();
		if (is == null || is.getTypeId() == 0)
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
				user.getPlayer().chat(command.substring(2));
			}
			else
			{
				used = true;
				ess.getPlugin().scheduleSyncDelayedTask(
						new Runnable()
						{
							@Override
							public void run()
							{
								user.getServer().dispatchCommand(user.getPlayer(), command);
							}
						});
			}
		}
		return used;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerPickupItem(final PlayerPickupItemEvent event)
	{
		final ISettings settings = ess.getSettings();
		if (!settings.getData().getCommands().getAfk().isDisableItemPickupWhileAfk())
		{
			return;
		}

		final IUser user = userMap.getUser(event.getPlayer());
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
			final IUser user = userMap.getUser((Player)event.getWhoClicked());
			final InventoryHolder invHolder = event.getView().getTopInventory().getHolder();
			if (invHolder != null && invHolder instanceof Player)
			{
				final IUser invOwner = userMap.getUser((Player)invHolder);
				if (user.isInvSee() && (!Permissions.INVSEE_MODIFY.isAuthorized(user) || Permissions.INVSEE_PREVENT_MODIFY.isAuthorized(
						invOwner) || !invOwner.isOnline()))
				{
					event.setCancelled(true);
				}
			}
		}
		if (event.getView().getTopInventory().getType() == InventoryType.WORKBENCH)
		{
			final IUser user = userMap.getUser((Player)event.getWhoClicked());
			if (user.isRecipeSee())
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryCloseEvent(final InventoryCloseEvent event)
	{
		if (event.getView().getTopInventory().getType() == InventoryType.PLAYER)
		{
			final IUser user = userMap.getUser((Player)event.getPlayer());
			user.setInvSee(false);
		}
		else if (event.getView().getTopInventory().getType() == InventoryType.WORKBENCH)
		{
			final IUser user = userMap.getUser((Player)event.getPlayer());
			if (user.isRecipeSee())
			{
				user.setRecipeSee(false);
				event.getView().getTopInventory().clear();
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerFishEvent(final PlayerFishEvent event)
	{
		final IUser user = userMap.getUser((Player)event.getPlayer());
		user.updateActivity(true);
	}
}
