package com.earth2me.essentials.listeners;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.ISettingsComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.users.Inventory;
import com.earth2me.essentials.components.users.TimeStampType;
import com.earth2me.essentials.perm.Permissions;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.TextInput;
import com.earth2me.essentials.textreader.TextPager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import lombok.Cleanup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;


public class EssentialsPlayerListener implements Listener
{
	private final transient Server server;
	private final transient IContext context;

	public EssentialsPlayerListener(final IContext parent)
	{
		super();
		this.context = parent;
		this.server = parent.getServer();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		final IUserComponent user = context.getUser(event.getPlayer());
		user.updateCompass();
		user.updateDisplayName();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(final PlayerChatEvent event)
	{
		@Cleanup
		final IUserComponent user = context.getUser(event.getPlayer());
		if (user.isMuted())
		{
			event.setCancelled(true);
			user.sendMessage($("playerMuted"));
			context.getLogger().info($("mutedUserSpeaks", user.getName()));
		}
		final Iterator<Player> it = event.getRecipients().iterator();
		while (it.hasNext())
		{
			final IUserComponent player = context.getUser(it.next());
			if (player.isIgnoringPlayer(user.getName()))
			{
				it.remove();
			}
		}
		user.updateActivity(true);
		user.updateDisplayName();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMove(final PlayerMoveEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		@Cleanup
		final IUserComponent user = context.getUser(event.getPlayer());
		@Cleanup(value = "unlock")
		final ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();

		if (user.isAfk() && settings.getData().getCommands().getAfk().isFreezeAFKPlayers())
		{
			final Location from = event.getFrom();
			final Location to = event.getTo().clone();
			to.setX(from.getX());
			to.setY(from.getY());
			to.setZ(from.getZ());
			try
			{
				event.setTo(Util.getSafeDestination(to));
			}
			catch (Exception ex)
			{
				event.setTo(to);
			}
			return;
		}

		Location afk;
		try
		{
			afk = user.getAfkPosition();
		}
		catch (Throwable ex)
		{
			afk = null;
		}

		if (afk == null || !event.getTo().getWorld().equals(afk.getWorld()) || afk.distanceSquared(event.getTo()) > 9)
		{
			user.updateActivity(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		@Cleanup
		final IUserComponent user = context.getUser(event.getPlayer());

		@Cleanup
		final ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();
		if (settings.getData().getCommands().getGod().isRemoveOnDisconnect() && user.isGodModeEnabled())
		{
			user.toggleGodModeEnabled();
		}
		if (user.getInventory() != null)
		{
			user.setLastInventory(new Inventory(user.getInventory().getContents()));
		}
		user.updateActivity(false);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		context.getBackup().startTask();

		@Cleanup
		final IUserComponent user = context.getUser(event.getPlayer());

		user.updateDisplayName();
		user.setIpAddress(user.getAddress().getAddress().getHostAddress());
		user.updateActivity(false);
		if (Permissions.SLEEPINGIGNORED.isAuthorized(user))
		{
			user.setSleepingIgnored(true);
		}

		@Cleanup
		final ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();

		if (!settings.getData().getCommands().isDisabled("motd") && Permissions.MOTD.isAuthorized(user))
		{
			try
			{
				final IText input = new TextInput(user, "motd", true, context);
				final IText output = new KeywordReplacer(input, user, context);
				final TextPager pager = new TextPager(output, true);
				pager.showPage("1", null, "motd", user);
			}
			catch (IOException ex)
			{
				if (settings.getData().getGeneral().isDebug())
				{
					context.getLogger().log(Level.WARNING, ex.getMessage(), ex);
				}
				else
				{
					context.getLogger().log(Level.WARNING, ex.getMessage());
				}
			}
		}

		if (!settings.getData().getCommands().isDisabled("mail") && Permissions.MAIL.isAuthorized(user))
		{
			final List<String> mail = user.getMails();
			if (mail == null || mail.isEmpty())
			{
				user.sendMessage($("noNewMail"));
			}
			else
			{
				user.sendMessage($("youHaveNewMail", mail.size()));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(final PlayerLoginEvent event)
	{
		if (event.getResult() != Result.ALLOWED && event.getResult() != Result.KICK_FULL && event.getResult() != Result.KICK_BANNED)
		{
			return;
		}
		@Cleanup
		final IUserComponent user = context.getUser(event.getPlayer());
		user.setNpc(false);

		final long currentTime = System.currentTimeMillis();
		final boolean banExpired = user.checkBanTimeout(currentTime);
		user.checkMuteTimeout(currentTime);
		user.checkJailTimeout(currentTime);

		if (!banExpired && (user.isBanned() || event.getResult() == Result.KICK_BANNED))
		{
			final String banReason = user.getBan() == null ? "" : user.getBan().getReason();
			event.disallow(Result.KICK_BANNED, banReason == null || banReason.isEmpty() || banReason.equalsIgnoreCase("ban") ? $("defaultBanReason") : banReason);
			return;
		}

		if (server.getOnlinePlayers().length >= server.getMaxPlayers() && !Permissions.JOINFULLSERVER.isAuthorized(user))
		{
			event.disallow(Result.KICK_FULL, $("serverFull"));
			return;
		}
		event.allow();

		user.setTimeStamp(TimeStampType.LOGIN, System.currentTimeMillis());
		user.updateCompass();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerTeleport(final PlayerTeleportEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}

		@Cleanup
		final ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();
		final IUserComponent user = context.getUser(event.getPlayer());
		//There is TeleportCause.COMMMAND but plugins have to actively pass the cause in on their teleports.
		if ((event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.COMMAND) && settings.getData().getCommands().getBack().isRegisterBackInListener())
		{
			user.setLastLocation();
		}

		user.updateDisplayName();
		user.updateCompass();
	}

	@EventHandler(priority = EventPriority.HIGH)
	@SuppressWarnings("deprecation")
	public void onPlayerEggThrow(final PlayerEggThrowEvent event)
	{
		@Cleanup
		final IUserComponent user = context.getUser(event.getPlayer());

		final ItemStack hand = new ItemStack(Material.EGG, 1);
		if (user.hasUnlimited(hand.getType()))
		{
			user.getInventory().addItem(hand);
			user.updateInventory();
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event)
	{
		@Cleanup
		final IUserComponent user = context.getUser(event.getPlayer());

		if (user.hasUnlimited(event.getBucket()))
		{
			event.getItemStack().setType(event.getBucket());
			context.getScheduler().scheduleSyncDelayedTask(new Runnable()
			{
				@Override
				@SuppressWarnings("deprecation")
				public void run()
				{
					user.updateInventory();
				}
			});
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerAnimation(final PlayerAnimationEvent event)
	{
		final IUserComponent user = context.getUser(event.getPlayer());
		user.updateActivity(true);
		if (event.getAnimationType() == PlayerAnimationType.ARM_SWING)
		{
			usePowertools(user);
		}
	}

	private void usePowertools(final IUserComponent user)
	{
		if (!user.hasPowerTools() || !user.isPowerToolsEnabled())
		{
			return;
		}

		final ItemStack hand = user.getItemInHand();
		Material type;
		if (hand == null || (type = hand.getType()) == Material.AIR)
		{
			return;
		}
		final List<String> commandList = user.getPowerTool(type);

		if (commandList == null || commandList.isEmpty())
		{
			return;
		}

		// We need to loop through each command and execute
		for (String command : commandList)
		{
			if (command.matches(".*\\{player\\}.*"))
			{
				//user.sendMessage("Click a player to use this command");
				continue;
			}
			else if (command.startsWith("c:"))
			{
				user.chat(command.substring(2));
			}
			else
			{
				user.getServer().dispatchCommand(user.getBase(), command);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		final IUserComponent user = context.getUser(event.getPlayer());
		final String cmd = event.getMessage().toLowerCase(Locale.ENGLISH).split(" ")[0].replace("/", "").toLowerCase(Locale.ENGLISH);
		final List<String> commands = Arrays.asList("msg", "r", "mail", "m", "t", "emsg", "tell", "er", "reply", "ereply", "email");
		if (commands.contains(cmd))
		{
			for (Player player : context.getServer().getOnlinePlayers())
			{
				@Cleanup
				IUserComponent spyer = context.getUser(player);

				if (spyer.isSocialSpy() && !user.equals(spyer))
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
		final ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();
		@Cleanup
		final IUserComponent user = context.getUser(event.getPlayer());

		if (!settings.getData().getWorldOptions(event.getPlayer().getLocation().getWorld().getName()).isGodmode() && !Permissions.NOGOD_OVERRIDE.isAuthorized(user))
		{
			if (user.isGodModeEnabled())
			{
				user.sendMessage($("noGodWorldWarning"));
			}
		}
		if (settings.getData().getCommands().getTpa().isCancelTpRequestsOnWorldChange())
		{
			if (user.getTeleporter() != null)
			{
				user.requestTeleport(null, false);
				user.sendMessage($("teleportRequestsCancelledWorldChange"));
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
		{
			return;
		}

		@Cleanup
		final ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();
		if (settings.getData().getCommands().getHome().isUpdateBedAtDaytime() && event.getClickedBlock().getType() == Material.BED_BLOCK)
		{
			event.getPlayer().setBedSpawnLocation(event.getClickedBlock().getLocation());
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerPickupItem(final PlayerPickupItemEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}

		@Cleanup
		final ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();
		if (!settings.getData().getCommands().getAfk().isDisableItemPickupWhileAfk())
		{
			return;
		}

		@Cleanup
		final IUserComponent user = context.getUser(event.getPlayer());

		if (user.isAfk())
		{
			event.setCancelled(true);
		}
	}
}
