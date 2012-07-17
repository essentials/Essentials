package net.ess3.api;

import java.util.List;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.Location;
import net.ess3.api.server.Player;
import net.ess3.storage.IStorageObjectHolder;
import net.ess3.user.CooldownException;
import net.ess3.user.UserData;


public interface IUser extends Player, IStorageObjectHolder<UserData>, IReload, IReplyTo, Comparable<IUser>
{
	Player getBase();

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

	boolean isHidden();

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

	boolean isIgnoringPlayer(IUser user);

	void setIgnoredPlayer(IUser user, boolean set);

	Location getAfkPosition();
	
	void dispose();

	void updateCompass();

	List<String> getHomes();

	void addMail(String string);

	void setMuted(boolean mute);

	boolean toggleSocialSpy();

	void requestTeleport(IUser user, boolean b);

	boolean isTpRequestHere();

	IUser getTeleportRequester();

	boolean toggleTeleportEnabled();

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
	
	boolean hasInvulnerabilityAfterTeleport();
	
	void update(final Player base);
	
	void setGodModeEnabled(boolean set);
	
	void setVanished(boolean set);
	
}
