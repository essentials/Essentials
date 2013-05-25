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
	/**
	 * Get the amount of money in a users account
	 *
	 * @return
	 */
	double getMoney();

	/**
	 * Remove money from the account
	 *
	 * @param value
	 */
	void takeMoney(double value);

	/**
	 *
	 * @param value
	 * @param initiator
	 */
	void takeMoney(double value, CommandSender initiator);

	/**
	 *
	 * @param value
	 */
	void giveMoney(double value);

	/**
	 *
	 * @param value
	 * @param initiator
	 */
	void giveMoney(double value, CommandSender initiator);

	/**
	 *
	 * @param itemStack
	 * @param canSpew
	 * @throws ChargeException
	 */
	void giveItems(ItemStack itemStack, Boolean canSpew) throws ChargeException;

	/**
	 *
	 * @param itemStacks
	 * @param canSpew
	 * @throws ChargeException
	 */
	void giveItems(List<ItemStack> itemStacks, Boolean canSpew) throws ChargeException;

	/**
	 *
	 * @param value
	 */
	void setMoney(double value);

	/**
	 *
	 * @param receiver
	 * @param value
	 * @throws Exception
	 */
	void payUser(final IUser receiver, final double value) throws Exception;

	/**
	 *
	 */
	void setLastLocation();

	/**
	 *
	 * @param name
	 * @return
	 * @throws Exception
	 */
	Location getHome(String name) throws Exception;

	/**
	 *
	 * @param loc
	 * @return
	 */
	Location getHome(Location loc);

	/**
	 *
	 * @return
	 */
	boolean isHidden(); //TODO: implement this?

	/**
	 *
	 * @return
	 */
	ITeleport getTeleport();

	/**
	 *
	 * @param cooldownType
	 * @param cooldown
	 * @param set
	 * @param bypassPermission
	 * @throws CooldownException
	 */
	void checkCooldown(UserData.TimestampType cooldownType, double cooldown, boolean set, IPermission bypassPermission) throws CooldownException;

	/**
	 *
	 * @return
	 */
	boolean toggleAfk();

	/**
	 *
	 * @param broadcast
	 */
	void updateActivity(boolean broadcast);

	/**
	 *
	 */
	void updateDisplayName();

	/**
	 *
	 */
	void setDisplayNick();

	/**
	 *
	 * @param currentTime
	 * @return
	 */
	boolean checkJailTimeout(long currentTime);

	/**
	 *
	 * @param currentTime
	 * @return
	 */
	boolean checkMuteTimeout(long currentTime);

	/**
	 *
	 * @param currentTime
	 * @return
	 */
	boolean checkBanTimeout(long currentTime);

	/**
	 *
	 * @param name
	 * @return
	 */
	long getTimestamp(UserData.TimestampType name);

	/**
	 *
	 * @param name
	 * @param value
	 */
	void setTimestamp(UserData.TimestampType name, long value);

	/**
	 *
	 * @param currentTime
	 */
	void setLastOnlineActivity(long currentTime);

	/**
	 *
	 */
	void checkActivity();

	/**
	 *
	 * @return
	 */
	long getLastOnlineActivity();

	/**
	 *
	 * @return
	 */
	boolean isGodModeEnabled();

	/**
	 *
	 * @param user
	 * @return
	 */
	boolean isIgnoringPlayer(IUser user);

	/**
	 *
	 * @param user
	 * @param set
	 */
	void setIgnoredPlayer(IUser user, boolean set);

	/**
	 *
	 * @return
	 */
	Location getAfkPosition();

	/**
	 *
	 */
	void updateCompass();

	/**
	 *
	 * @return
	 */
	Set<String> getHomes();

	/**
	 *
	 * @param string
	 */
	void addMail(String string);

	/**
	 *
	 * @param mute
	 */
	void setMuted(boolean mute);

	/**
	 *
	 * @param user
	 * @param b
	 */
	void requestTeleport(IUser user, boolean b);

	/**
	 *
	 * @return
	 */
	boolean isTpRequestHere();

	/**
	 *
	 * @return
	 */
	IUser getTeleportRequester();

	/**
	 *
	 * @return
	 */
	long getTeleportRequestTime();

	/**
	 *
	 * @return
	 */
	boolean gotMailInfo();

	/**
	 *
	 * @return
	 */
	List<String> getMails();

	/**
	 *
	 * @param money
	 * @return
	 */
	boolean canAfford(double money);

	/**
	 *
	 * @param userMoney
	 */
	void updateMoneyCache(double userMoney);

	/**
	 *
	 * @param amount
	 * @param b
	 * @return
	 */
	boolean canAfford(double amount, boolean b);

	/**
	 *
	 * @return
	 */
	boolean isVanished();

	/**
	 *
	 */
	void resetInvulnerabilityAfterTeleport();

	/**
	 *
	 */
	void toggleVanished();

	/**
	 *
	 * @return
	 */
	boolean isInvSee();

	/**
	 *
	 * @param invsee
	 */
	void setInvSee(boolean invsee);

	/**
	 *
	 * @return
	 */
	boolean isEnderSee();

	/**
	 *
	 * @param endersee
	 */
	void setEnderSee(boolean endersee);

	/**
	 *
	 * @return
	 */
	boolean hasInvulnerabilityAfterTeleport();

	/**
	 *
	 * @param set
	 */
	void setGodModeEnabled(boolean set);

	/**
	 *
	 * @param set
	 */
	void setVanished(boolean set);

	/**
	 *
	 * @param throttle
	 * @return
	 */
	boolean checkSignThrottle(int throttle);

	/**
	 *
	 * @return
	 */
	boolean isRecipeSee();

	/**
	 *
	 * @param recipeSee
	 */
	void setRecipeSee(boolean recipeSee);

	/**
	 * Since the Player object should not be stored for a long time, this method should be called again with a null
	 * value.
	 *
	 * @param player
	 */
	void setPlayerCache(Player player);

	/**
	 * If this is not cached using the setPlayerCache method, this call is slow and should not be called often.
	 * <p/>
	 * It iterates over all players, that are online and does a equal check on their names.
	 *
	 * @return
	 */
	@Override
	Player getPlayer();
}
