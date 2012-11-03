package com.earth2me.essentials;

import java.io.File;
import net.ess3.api.IEssentials;
import net.ess3.settings.General;


public class UpdateSettings
{
	UpdateSettings(File config, IEssentials ess)
	{
		Settings settings = new Settings(ess, config);
		net.ess3.settings.Settings data = ess.getSettings().getData();
		data.getChat().setChangeDisplayname(settings.changeDisplayName());
		data.getChat().setDefaultFormat(settings.getDefaultChatformat());
		data.getChat().setLocalRadius(settings.getChatRadius());
		data.getChat().setNicknamePrefix(settings.getNicknamePrefix());
		data.getCommands().getAfk().setAutoAFK(settings.getAutoAfk());
		data.getCommands().getAfk().setAutoAFKKick(settings.getAutoAfkKick());
		data.getCommands().getAfk().setDisableItemPickupWhileAfk(settings.getDisableItemPickupWhileAfk());
		data.getCommands().getAfk().setFreezeAFKPlayers(settings.getFreezeAfkPlayers());
		data.getCommands().getBack().setRegisterBackInListener(settings.registerBackInListener());
		data.getCommands().getGod().setRemoveOnDisconnect(settings.removeGodOnDisconnect());
		data.getCommands().getHelp().setHidePermissionlessCommands(settings.hidePermissionlessHelp());
		data.getCommands().getHelp().setShowNonEssCommandsInHelp(settings.showNonEssCommandsInHelp());
		data.getCommands().getHome().setBedSetsHome(false); //TODO
		data.getCommands().getHome().setRespawnAtHome(settings.getRespawnAtHome());
		data.getCommands().getHome().setSpawnIfNoHome(false); //TODO
		data.getCommands().getHome().setUpdateBedAtDaytime(false); //TODO
		data.getCommands().getLightning().setWarnPlayer(settings.warnOnSmite());
		data.getCommands().getList().setSortByGroups(settings.getSortListByGroups());
		data.getCommands().getNear().setDefaultRadius(0); //TODO
		data.getCommands().getSocialspy().setSocialspyCommands(null); //TODO
		data.getCommands().getSpawnmob().setLimit(settings.getSpawnMobLimit());
		data.getCommands().getSpeed().setMaxFlySpeed(settings.getMaxFlySpeed());
		data.getCommands().getSpeed().setMaxWalkSpeed(settings.getMaxWalkSpeed());
		data.getCommands().getTeleport().setCancelRequestsOnWorldChange(false); //TODO
		data.getCommands().getTeleport().setRequestTimeout(0); //TODO
		data.getCommands().getTeleport().setInvulnerability(settings.getTeleportInvulnerability());
		data.getEconomy().setCurrencySymbol(settings.getCurrencySymbol());
		data.getEconomy().setLogEnabled(settings.isEcoLogEnabled());
		data.getEconomy().setMaxMoney(settings.getMaxMoney());
		data.getEconomy().setMinMoney(settings.getMinMoney());
		data.getEconomy().setStartingBalance(settings.getStartingBalance());
		data.getEconomy().setTradeInStacks(settings.isTradeInStacks(0)); //TODO
		data.getEconomy().getWorth(); //TODO
		data.getGeneral().getBackup().setCommand(settings.getBackupCommand());
		data.getGeneral().getBackup().setInterval(settings.getBackupInterval());
		data.getGeneral().setDeathMessages(settings.areDeathMessagesEnabled());
		data.getGeneral().setDebug(settings.isDebug());
		data.getGeneral().setDefaultStacksize(settings.getDefaultStackSize());
		data.getGeneral().setGroupStorage(General.GroupStorage.FILE); //TODO
		data.getGeneral().setJoinMessage(null); //TODO
		data.getGeneral().setLeaveMessage(null); //TODO
		data.getGeneral().setLocale(settings.getLocale());
		data.getGeneral().setLoginAttackDelay(settings.getLoginAttackDelay());
		data.getGeneral().setMetricsEnabled(settings.isMetricsEnabled());
		data.getGeneral().setOversizedStacksize(settings.getOversizedStackSize());
		data.getGeneral().setWorldHomePermissions(settings.isWorldHomePermissions());
		data.getGeneral().setWorldTeleportPermissions(settings.isWorldTeleportPermissions());
		data.getWorldOptions(null); //TODO
		ess.getSettings().queueSave();
	}
}
