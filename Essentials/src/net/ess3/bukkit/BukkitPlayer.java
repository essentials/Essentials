package net.ess3.bukkit;

import lombok.Delegate;
import lombok.Getter;
import net.ess3.api.IUser;
import net.ess3.api.server.IInventory;
import net.ess3.api.server.Location;
import net.ess3.api.server.Player;
import net.ess3.api.server.World;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;


public class BukkitPlayer extends BukkitCommandSender implements Player
{
	private interface Excludes
	{
		org.bukkit.World getWorld();

		org.bukkit.Location getLocation();

		org.bukkit.Location getEyeLocation();

		org.bukkit.inventory.ItemStack getItemInHand();

		org.bukkit.Location getBedSpawnLocation();
		
		org.bukkit.inventory.Inventory getInventory();
		
		void setTotalExperience(int i);
		
		int getTotalExperience();
	}
	@Delegate(types =
	{
		org.bukkit.entity.Player.class, org.bukkit.entity.LivingEntity.class
	}, excludes =
	{
		OfflinePlayer.class, org.bukkit.command.CommandSender.class, Excludes.class
	})
	@Getter
	private transient org.bukkit.entity.Player onlinePlayer;
	/**
	 * a set of data and methods common to both offline and online players.
	 */
	@Delegate(excludes =
	{
		Excludes.class
	})
	@Getter
	private transient OfflinePlayer safePlayer;
	private final transient BukkitServer server;
	private transient IUser user = null;

	public BukkitPlayer(final OfflinePlayer player, final BukkitServer server)
	{
		super(player.getPlayer());
		this.server = server;
		if (player.isOnline())
		{

			setOnlinePlayer(player.getPlayer());
		}
		else
		{
			setOfflinePlayer(player);
		}
	}

	public final void setOfflinePlayer(final OfflinePlayer offlinePlayer)
	{
		safePlayer = offlinePlayer;
		onlinePlayer = null;
	}

	public final void setOnlinePlayer(final org.bukkit.entity.Player onlinePlayer)
	{
		safePlayer = this.onlinePlayer = onlinePlayer;
	}

	public String getSafeDisplayName()
	{
		return onlinePlayer == null ? getName() : getDisplayName();
	}

	@Override
	public IUser getUser()
	{
		return user;
	}

	@Override
	public boolean isPlayer()
	{
		return true;
	}

	@Override
	public World getWorld()
	{
		return server.getWorld(onlinePlayer.getWorld().getName());
	}

	@Override
	public Location getLocation()
	{
		return new BukkitLocation(onlinePlayer.getLocation());
	}

	@Override
	public Location getEyeLocation()
	{
		return new BukkitLocation(onlinePlayer.getEyeLocation());
	}

	@Override
	public BukkitItemStack getItemInHand()
	{
		return new BukkitItemStack(onlinePlayer.getItemInHand());
	}

	@Override
	public Location getBedSpawnLocation()
	{
		return new BukkitLocation(onlinePlayer.getBedSpawnLocation());
	}

	@Override
	public IInventory getInventory()
	{
		return new Inventory(onlinePlayer.getInventory());
	}
	
	@Override
	public void setTotalExperience(final int exp)
	{
		if (exp < 0)
		{
			throw new IllegalArgumentException("Experience is negative!");
		}
		onlinePlayer.setExp(0);
		onlinePlayer.setLevel(0);
		onlinePlayer.setTotalExperience(0);
		int amount = exp;
		while (amount > 0)
		{
			final int expToLevel = getExpToLevel();
			amount -= expToLevel;
			if (amount >= 0)
			{
				// give until next level
				onlinePlayer.giveExp(expToLevel);
			}
			else
			{
				// give the rest
				amount += expToLevel;
				onlinePlayer.giveExp(amount);
				amount = 0;
			}
		}
	}

	/*private int getExpToLevel()
	{		
		return getExpToLevel(onlinePlayer.getLevel());
	}*/
	
	private static int getExpToLevel(final int level)
	{		
		return 7 + (level * 7 >> 1);
	}
	
	@Override
	public int getTotalExperience()
	{
		int exp = (int) (getExpToLevel() * onlinePlayer.getExp());
		int currentLevel = onlinePlayer.getLevel();
		
		while (currentLevel > 0) {			
			currentLevel--;
			exp += getExpToLevel(currentLevel);
		}
		return exp;
	}
	
	
	@Override
	public void sendMessage(final String message)
	{
		onlinePlayer.sendMessage(message);
	}

	@Override
	public void sendMessage(final String[] string)
	{
		onlinePlayer.sendMessage(string);
	}
	
	
	@Override
	public void setCompassTarget(final Location loc)
	{
		onlinePlayer.setCompassTarget(((BukkitLocation)loc).getBukkitLocation());
	}
	
	
	@Override
	public boolean isInSurvivalMode()
	{
		return onlinePlayer.getGameMode() == GameMode.SURVIVAL;
	}
}
