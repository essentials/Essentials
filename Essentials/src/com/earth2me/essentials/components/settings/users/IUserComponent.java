package com.earth2me.essentials.components.settings.users;

import com.earth2me.essentials.api.IPermissions;
import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.economy.ChargeException;
import com.earth2me.essentials.storage.LocationData;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;


public interface IUserComponent extends Comparable<IUserComponent>, IComponent, IReplyTo, IStatelessPlayer, CommandSender
{
	IStatelessPlayer getBase();

	void setBase(final IStatelessPlayer base);

	LocationData getLastLocation();

	void setLastLocation(final LocationData lastLocation);

	void addMail(String string);

	boolean canAfford(final double cost);

	void checkActivity();

	boolean checkBanTimeout(final long currentTime);

	void checkCooldown(final TimestampType cooldownType, final double cooldown, final boolean set, final IPermissions bypassPermission) throws CooldownException;

	boolean checkJailTimeout(final long currentTime);

	boolean checkMuteTimeout(final long currentTime);

	Location getHome(Location loc);

	Location getHome(String name) throws Exception;

	List<String> getHomes();

	List<String> getMails();

	double getMoney();

	String getNick(boolean addprefixsuffix);

	long getTimestamp(final TimestampType name);

	void giveItems(ItemStack itemStack, Boolean canSpew) throws ChargeException;

	void giveItems(List<ItemStack> itemStacks, Boolean canSpew) throws ChargeException;

	void giveMoney(final double value);

	void giveMoney(final double value, final CommandSender initiator);

	boolean gotMailInfo();

	boolean isGodModeEnabled();

	boolean isIgnoringPlayer(final String name);

	void payUser(final IUserComponent receiver, final double value) throws Exception;

	void requestTeleport(final UserComponent player, final boolean here);

	void setAfk(final boolean set);

	void setDisplayNick();

	void setHome(String name, Location loc);

	void setHome();

	void setHome(final String name);

	void setIgnoredPlayer(final String name, final boolean set);

	void setLastLocation();

	void setMoney(final double value);

	@SuppressWarnings(value = "MapReplaceableByEnumMap")
	void setTimestamp(final TimestampType name, final long value);

	void takeMoney(final double value);

	void takeMoney(final double value, final CommandSender initiator);

	boolean toggleAfk();

	boolean toggleGodModeEnabled();

	boolean toggleGodMode();

	boolean toggleMuted();

	boolean toggleSocialSpy();

	boolean toggleTeleportEnabled();

	void updateActivity(final boolean broadcast);

	void updateCompass();

	void updateDisplayName();

}
