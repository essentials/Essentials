package com.earth2me.essentials;


import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventPriority;


public interface ISettings
{
	boolean areSignsDisabled();

	String getAnnounceNewPlayerFormat();

	boolean getAnnounceNewPlayers();

	String getNewPlayerKit();

	String getBackupCommand();

	long getBackupInterval();

	//MessageFormat getChatFormat(String group);

	int getChatRadius();

	double getCommandCost(String label);

	String getCurrencySymbol();

	int getOversizedStackSize();

	int getDefaultStackSize();

	double getHealCooldown();

	Map<String, Object> getKit(String name);

	ConfigurationSection getKits();

	String getLocale();

	String getNewbieSpawn();

	String getNicknamePrefix();

	ChatColor getOperatorColor() throws Exception;

	boolean getPerWarpPermission();

	boolean getProtectBoolean(final String configName, boolean def);

	int getProtectCreeperMaxHeight();

	List<Integer> getProtectList(final String configName);

	boolean getProtectPreventSpawn(final String creatureName);

	String getProtectString(final String configName);

	boolean getRespawnAtHome();

	Set getMultipleHomes();

	int getHomeLimit(String set);

	boolean getSortListByGroups();

	int getSpawnMobLimit();

	int getStartingBalance();

	double getTeleportCooldown();

	double getTeleportDelay();

	boolean hidePermissionlessHelp();

	boolean isCommandDisabled(String label);

	boolean isCommandOverridden(String name);

	boolean isDebug();

	boolean isEcoDisabled();

	boolean isTradeInStacks(int id);

	List<Integer> itemSpawnBlacklist();

	List<String> enabledSigns();

	boolean permissionBasedItemSpawn();

	boolean showNonEssCommandsInHelp();

	boolean warnOnBuildDisallow();

	boolean warnOnSmite();

	double getMaxMoney();

	double getMinMoney();

	boolean isEcoLogEnabled();

	boolean isEcoLogUpdateEnabled();

	boolean removeGodOnDisconnect();

	boolean changeDisplayName();

	boolean changePlayerListName();

	boolean isPlayerCommand(String string);

	boolean useBukkitPermissions();

	boolean addPrefixSuffix();

	boolean disablePrefix();

	boolean disableSuffix();

	long getAutoAfk();

	long getAutoAfkKick();

	boolean getFreezeAfkPlayers();

	boolean cancelAfkOnMove();

	boolean areDeathMessagesEnabled();

	public void setDebug(boolean debug);

	Set<String> getNoGodWorlds();

	boolean getUpdateBedAtDaytime();

	boolean getRepairEnchanted();

	boolean isWorldTeleportPermissions();

	boolean isWorldHomePermissions();

	boolean registerBackInListener();

	boolean getDisableItemPickupWhileAfk();

	EventPriority getRespawnPriority();

	long getTpaAcceptCancellation();

	boolean isMetricsEnabled();

	void setMetricsEnabled(boolean metricsEnabled);

	long getTeleportInvulnerability();

	boolean isTeleportInvulnerability();

	long getLoginAttackDelay();

	int getSignUsePerSecond();

	double getMaxFlySpeed();

	double getMaxWalkSpeed();

	public int getMailsPerMinute();

	public void setEssentialsChatActive(boolean b);
}