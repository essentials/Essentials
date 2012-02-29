package com.earth2me.essentials.components.users;

import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.IPermissions;
import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.economy.ChargeException;
import com.earth2me.essentials.storage.ISubStorageComponent;
import com.earth2me.essentials.storage.LocationData;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public interface IUserComponent extends ISubStorageComponent<UserSurrogate, IEssentials>, Comparable<IUserComponent>, IComponent, IReplyTo, IStatelessPlayer, CommandSender, LivingEntity, Player, OfflinePlayer
{
	IStatelessPlayer getBase();

	void setBase(final IStatelessPlayer base);

	LocationData getLastLocation();

	void setLastLocation(final LocationData lastLocation);

	void addMail(String string);

	boolean isMuted();

	void setMuted(final boolean muted);

	Location getAfkPosition();

	void setAfkPosition(final Location afkPosition);

	void setLastInventory(final Inventory inventory);

	Inventory getLastInventory();

	void setIpAddress(final String ipAddress);

	String getIpAddress();

	void setNpc(final boolean npc);

	boolean isNpc();

	Ban getBan();

	void setBan(Ban ban);

	boolean canAfford(final double cost);

	void checkActivity();

	boolean hasPowerTools();

	boolean isPowerToolsEnabled();

	boolean isSocialSpy();

	void setSocialSpy(boolean socialSpy);

	ITeleporter getTeleporter();

	void setPowerToolsEnabled(final boolean powerToolsEnabled);

	List<String> getPowerTool(final Material material);

	void setPowerTool(final Material material, List<String> powertool);

	boolean checkBanTimeout(final long currentTime);

	void checkCooldown(final TimeStampType cooldownType, final double cooldown, final boolean set, final IPermissions bypassPermission) throws CooldownException;

	boolean checkJailTimeout(final long currentTime);

	boolean checkMuteTimeout(final long currentTime);

	Location getHome(Location loc);

	Location getHome(String name) throws Exception;

	Map<String, LocationData> getHomes();

	List<String> getMails();

	double getMoney();

	String getNick(boolean addprefixsuffix);

	long getTimeStamp(final TimeStampType name);

	void giveItems(ItemStack itemStack, Boolean canSpew) throws ChargeException;

	void giveItems(List<ItemStack> itemStacks, Boolean canSpew) throws ChargeException;

	void giveMoney(final double value);

	void giveMoney(final double value, final CommandSender initiator);

	boolean gotMailInfo();

	boolean isGodModeEnabled();

	boolean isIgnoringPlayer(final String name);

	void payUser(final IUserComponent receiver, final double value) throws Exception;

	void requestTeleport(final IUserComponent player, final boolean here);

	void setAfk(final boolean set);

	boolean isAfk();

	void setDisplayNick();

	void setHome(String name, Location loc);

	void setHome();

	void setHome(final String name);

	void setIgnoredPlayer(final String name, final boolean set);

	void setLastLocation();

	void setMoney(final double value);

	@SuppressWarnings(value = "MapReplaceableByEnumMap")
	void setTimeStamp(final TimeStampType name, final long value);

	void takeMoney(final double value);

	void takeMoney(final double value, final CommandSender initiator);

	boolean toggleAfk();

	boolean toggleGodModeEnabled();

	boolean toggleMuted();

	boolean toggleSocialSpy();

	boolean toggleTeleportEnabled();

	void updateActivity(final boolean broadcast);

	void updateCompass();

	void updateDisplayName();

	boolean hasUnlimited(Material type);

	void setJail(String jail);

	boolean isJailed();

	String getJail();

	void setLastOnlineActivity(long currentTime);

	long getLastOnlineActivity();

	boolean isHidden();

	public IUserComponent getTeleportRequester();

	public boolean isTeleportRequestHere();

	public long getTeleportRequestTime();
}
