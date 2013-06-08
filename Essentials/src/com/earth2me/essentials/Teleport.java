package com.earth2me.essentials;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.ITeleport;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Teleport implements ITeleport
{
	public class Target
	{
		private final Location location;
		private final String name;

		Target(Location location)
		{
			this.location = location;
			this.name = null;
		}

		Target(Player entity)
		{
			this.name = entity.getName();
			this.location = null;
		}

		public Location getLocation()
		{
			if (this.name != null)
			{

				return ess.getServer().getPlayerExact(name).getLocation();
			}
			return location;
		}
	}
	private final IUser teleportOwner;
	private final IEssentials ess;
	private TimedTeleport timedTeleport;

	public Teleport(IUser user, IEssentials ess)
	{
		this.teleportOwner = user;
		this.ess = ess;
	}

	public void cooldown(boolean check) throws Exception
	{
		final Calendar time = new GregorianCalendar();
		if (teleportOwner.getLastTeleportTimestamp() > 0)
		{
			// Take the current time, and remove the delay from it.
			final double cooldown = ess.getSettings().getTeleportCooldown();
			final Calendar earliestTime = new GregorianCalendar();
			earliestTime.add(Calendar.SECOND, -(int)cooldown);
			earliestTime.add(Calendar.MILLISECOND, -(int)((cooldown * 1000.0) % 1000.0));
			// This value contains the most recent time a teleportPlayer could have been used that would allow another use.
			final long earliestLong = earliestTime.getTimeInMillis();

			// When was the last teleportPlayer used?
			final Long lastTime = teleportOwner.getLastTeleportTimestamp();

			if (lastTime > time.getTimeInMillis())
			{
				// This is to make sure time didn't get messed up on last teleportPlayer use.
				// If this happens, let's give the user the benifit of the doubt.
				teleportOwner.setLastTeleportTimestamp(time.getTimeInMillis());
				return;
			}
			else if (lastTime > earliestLong && !teleportOwner.isAuthorized("essentials.teleport.cooldown.bypass"))
			{
				time.setTimeInMillis(lastTime);
				time.add(Calendar.SECOND, (int)cooldown);
				time.add(Calendar.MILLISECOND, (int)((cooldown * 1000.0) % 1000.0));
				throw new Exception(_("timeBeforeTeleport", Util.formatDateDiff(time.getTimeInMillis())));
			}
		}
		// if justCheck is set, don't update lastTeleport; we're just checking
		if (!check)
		{
			teleportOwner.setLastTeleportTimestamp(time.getTimeInMillis());
		}
	}

	private void warnUser(final IUser user, final double delay)
	{
		Calendar c = new GregorianCalendar();
		c.add(Calendar.SECOND, (int)delay);
		c.add(Calendar.MILLISECOND, (int)((delay * 1000.0) % 1000.0));
		user.sendMessage(_("dontMoveMessage", Util.formatDateDiff(c.getTimeInMillis())));
	}

	//The now function is used when you want to skip tp delay when teleporting someone to a location or player.
	@Override
	public void now(Location loc, boolean cooldown, TeleportCause cause) throws Exception
	{
		if (cooldown)
		{
			cooldown(false);
		}
		now(teleportOwner, new Target(loc), cause);
	}

	@Override
	public void now(Player entity, boolean cooldown, TeleportCause cause) throws Exception
	{
		if (cooldown)
		{
			cooldown(false);
		}
		now(teleportOwner, new Target(entity), cause);
	}

	protected void now(IUser teleportee, Target target, TeleportCause cause) throws Exception
	{
		cancel(false);
		teleportee.setLastLocation();
		teleportee.getBase().teleport(Util.getSafeDestination(target.getLocation()), cause);
	}

	//The teleportPlayer function is used when you want to normally teleportPlayer someone to a location or player.
	//This method is nolonger used internally and will be removed.
	@Deprecated
	public void teleport(Location loc, Trade chargeFor) throws Exception
	{
		teleport(loc, chargeFor, TeleportCause.PLUGIN);
	}

	@Override
	public void teleport(Location loc, Trade chargeFor, TeleportCause cause) throws Exception
	{
		teleport(teleportOwner, new Target(loc), chargeFor, cause);
	}

	@Override
	public void teleport(Player entity, Trade chargeFor, TeleportCause cause) throws Exception
	{
		teleport(teleportOwner, new Target(entity), chargeFor, cause);
	}

	@Override
	public void teleportPlayer(IUser teleportee, Location loc, Trade chargeFor, TeleportCause cause) throws Exception
	{
		teleport(teleportee, new Target(loc), chargeFor, cause);
	}

	@Override
	public void teleportPlayer(IUser teleportee, Player entity, Trade chargeFor, TeleportCause cause) throws Exception
	{
		teleport(teleportee, new Target(entity), chargeFor, cause);
	}

	private void teleport(IUser teleportee, Target target, Trade chargeFor, TeleportCause cause) throws Exception
	{
		double delay = ess.getSettings().getTeleportDelay();

		if (chargeFor != null)
		{
			chargeFor.isAffordableFor(teleportOwner);
		}
		cooldown(true);
		if (delay <= 0 || teleportOwner.isAuthorized("essentials.teleport.timer.bypass")
			|| teleportee.isAuthorized("essentials.teleport.timer.bypass"))
		{
			cooldown(false);
			now(teleportee, target, cause);
			if (chargeFor != null)
			{
				chargeFor.charge(teleportOwner);
			}
			return;
		}

		cancel(false);
		warnUser(teleportee, delay);
		initTimer((long)(delay * 1000.0), teleportee, target, chargeFor, cause, false);
	}

	//The teleportToMe function is a wrapper used to handle teleporting players to them, like /tphere
	@Override
	public void teleportToMe(IUser otherUser, Trade chargeFor, TeleportCause cause) throws Exception
	{
		Target target = new Target(teleportOwner);
		teleport(otherUser, target, chargeFor, cause);
	}

	//The respawn function is a wrapper used to handle tp fallback, on /jail and /home
	@Override
	public void respawn(final Trade chargeFor, TeleportCause cause) throws Exception
	{
		double delay = ess.getSettings().getTeleportDelay();
		if (chargeFor != null)
		{
			chargeFor.isAffordableFor(teleportOwner);
		}
		cooldown(true);
		if (delay <= 0 || teleportOwner.isAuthorized("essentials.teleport.timer.bypass"))
		{
			cooldown(false);
			respawnNow(teleportOwner, cause);
			if (chargeFor != null)
			{
				chargeFor.charge(teleportOwner);
			}
			return;
		}

		cancel(false);
		warnUser(teleportOwner, delay);
		initTimer((long)(delay * 1000.0), teleportOwner, null, chargeFor, cause, true);
	}

	protected void respawnNow(IUser teleportee, TeleportCause cause) throws Exception
	{
		final Player player = teleportee.getBase();
		Location bed = player.getBedSpawnLocation();
		if (bed != null)
		{
			now(teleportee, new Target(bed), cause);
		}
		else
		{
			if (ess.getSettings().isDebug())
			{
				ess.getLogger().info("Could not find bed spawn, forcing respawn event.");
			}
			final PlayerRespawnEvent pre = new PlayerRespawnEvent(player, player.getWorld().getSpawnLocation(), false);
			ess.getServer().getPluginManager().callEvent(pre);
			now(teleportee, new Target(pre.getRespawnLocation()), cause);
		}
	}

	//The warp function is a wrapper used to teleportPlayer a player to a /warp
	@Override
	public void warp(IUser teleportee, String warp, Trade chargeFor, TeleportCause cause) throws Exception
	{
		Location loc = ess.getWarps().getWarp(warp);
		teleportee.sendMessage(_("warpingTo", warp));
		teleport(teleportee, new Target(loc), chargeFor, cause);
	}

	//The back function is a wrapper used to teleportPlayer a player /back to their previous location.
	@Override
	public void back(Trade chargeFor) throws Exception
	{
		teleport(teleportOwner, new Target(teleportOwner.getLastLocation()), chargeFor, TeleportCause.COMMAND);
	}

	//This function is used to throw a user back after a jail sentence
	@Override
	public void back() throws Exception
	{
		now(teleportOwner, new Target(teleportOwner.getLastLocation()), TeleportCause.COMMAND);
	}

	//This function handles teleporting to /home
	@Override
	public void home(Location loc, Trade chargeFor) throws Exception
	{
		teleport(teleportOwner, new Target(loc), chargeFor, TeleportCause.COMMAND);
	}

	//If we need to cancelTimer a pending teleportPlayer call this method
	private void cancel(boolean notifyUser)
	{
		if (timedTeleport != null)
		{
			timedTeleport.cancelTimer(notifyUser);
			timedTeleport = null;
		}
	}

	private void initTimer(long delay, IUser teleportUser, Target target, Trade chargeFor, TeleportCause cause, boolean respawn)
	{
		timedTeleport = new TimedTeleport(teleportOwner, ess, this, delay, teleportUser, target, chargeFor, cause, respawn);
	}
}