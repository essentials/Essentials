package net.ess3.api;

import java.util.List;
import java.util.Set;
import net.ess3.storage.IStorageObjectHolder;
import net.ess3.user.CooldownException;
import net.ess3.user.UserData;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public interface IUser extends OfflinePlayer, CommandSender, IStorageObjectHolder<UserData>, IReload, IReplyTo, Comparable<IUser>
{
	double getMoney();

	void takeMoney(double value);

	void takeMoney(double value, CommandSender initiator);

	void giveMoney(double value);

	void giveMoney(double value, CommandSender initiator);

	void giveItems(ItemStack itemStack, Boolean canSpew) throws ChargeException;

	void giveItems(List<ItemStack> itemStacks, Boolean canSpew) throws ChargeException;

	void setMoney(double value);

	void payUser(final IUser reciever, final double value) throws Exception;

	void setLastLocation();

	Location getHome(String name) throws Exception;

	Location getHome(Location loc);

	//boolean isHidden();
	ITeleport getTeleport();

	void checkCooldown(UserData.TimestampType cooldownType, double cooldown, boolean set, IPermission bypassPermission) throws CooldownException;

	boolean toggleAfk();

	void updateActivity(boolean broadcast);

	void updateDisplayName();

	void setDisplayNick();

	boolean checkJailTimeout(long currentTime);

	boolean checkMuteTimeout(long currentTime);

	boolean checkBanTimeout(long currentTime);

	long getTimestamp(UserData.TimestampType name);

	void setTimestamp(UserData.TimestampType name, long value);

	void setLastOnlineActivity(long currentTime);

	void checkActivity();

	long getLastOnlineActivity();

	boolean isGodModeEnabled();

	boolean isTeleportEnabled();

	boolean isIgnoringPlayer(IUser user);

	void setIgnoredPlayer(IUser user, boolean set);

	Location getAfkPosition();

	void updateCompass();

	Set<String> getHomes();

	void addMail(String string);

	void setMuted(boolean mute);

	void requestTeleport(IUser user, boolean b);

	boolean isTpRequestHere();

	IUser getTeleportRequester();

	long getTeleportRequestTime();

	boolean gotMailInfo();

	List<String> getMails();

	public boolean canAfford(double money);

	public void updateMoneyCache(double userMoney);

	public boolean canAfford(double amount, boolean b);

	boolean isVanished();

	void resetInvulnerabilityAfterTeleport();

	void toggleVanished();

	boolean isInvSee();

	void setInvSee(boolean invsee);

	boolean isEnderSee();

	void setEnderSee(boolean endersee);

	boolean hasInvulnerabilityAfterTeleport();

	void setGodModeEnabled(boolean set);

	void setTeleportEnabled(boolean set);

	void setVanished(boolean set);

	boolean checkSignThrottle(int throttle);

	/**
	 * Since the Player object should not be stored for a long time, this method should be called again with a null
	 * value.
	 *
	 * @param player
	 */
	void setPlayerCache(Player player);

	/**
	 * If this is not cached using the setPlayerCache method, this call is slow and should not be called often.
	 *
	 * It iterates over all players, that are online and does a equal check on their names.
	 *
	 * @return
	 */
	@Override
	Player getPlayer();
}
