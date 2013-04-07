package com.earth2me.essentials;

import com.google.common.io.PatternFilenameFilter;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import net.ess3.api.*;
import net.ess3.user.User;
import org.bukkit.Material;


class UpdateUserFiles
{
	private static final String BROKENNAME = "BROKENNAME";
	private final IEssentials ess;

	public UpdateUserFiles(File folder, IEssentials ess)
	{
		this.ess = ess;
		for (String filename : folder.list(new PatternFilenameFilter(".+\\.yml")))
		{
			File file = new File(folder, filename);
			String name = filename.substring(0, filename.length() - 4);
			if (name.contains("_"))
			{
				String origName = name;
				name = getRealName(name);
				if (name.equals(BROKENNAME))
				{
					ess.getLogger().log(Level.WARNING, "Could not convert player {0}", origName);
					continue;
				}
			}
			UserData userData = new UserData(ess, file);
			if (userData.isNPC())
			{
				ess.getEconomy().createNPC(name);
				try
				{
					ess.getEconomy().setMoney(name, userData.getMoney());
				}
				catch (UserDoesNotExistException ex)
				{
					ess.getLogger().log(Level.WARNING, "Could not convert player " + name, ex);
				}
				catch (NoLoanPermittedException ex)
				{
					ess.getLogger().log(Level.WARNING, "Could not convert player " + name, ex);
				}
			}
			else
			{
				IUser user = ess.getUserMap().getUser(name);
				if (user == null)
				{
					try
					{
						user = new User(ess.getServer().getOfflinePlayer(name), ess);
					}
					catch (InvalidNameException ex)
					{
						ess.getLogger().log(Level.WARNING, "Could not convert player " + name, ex);
						continue;
					}
				}
				net.ess3.user.UserData data = user.getData();
				data.setPowerToolsEnabled(userData.arePowerToolsEnabled());
				data.getBan().setReason(userData.getBanReason());
				data.getBan().setTimeout(userData.getBanTimeout());
				userData.getHomes();
				for (String playerName : userData.getIgnoredPlayers())
				{
					data.setIgnore(playerName.toLowerCase(Locale.ENGLISH), true);
				}

				data.setJail(userData.getJail());
				data.setTimestamp(net.ess3.user.UserData.TimestampType.JAIL, userData.getJailTimeout());
				for (String kit : userData.getKitTimestamps())
				{
					data.setTimestamp(net.ess3.user.UserData.TimestampType.KIT, kit, userData.getKitTimestamp(kit));
				}
				data.setTimestamp(net.ess3.user.UserData.TimestampType.LASTHEAL, userData.getLastHealTimestamp());
				data.setLastLocation(userData.getLastLocation());
				data.setTimestamp(net.ess3.user.UserData.TimestampType.LOGIN, userData.getLastLogin());
				data.setIpAddress(userData.getLastLoginAddress());
				data.setTimestamp(net.ess3.user.UserData.TimestampType.LOGOUT, userData.getLastLogout());
				data.setTimestamp(net.ess3.user.UserData.TimestampType.LASTTELEPORT, userData.getLastTeleportTimestamp());
				for (String mail : userData.getMails())
				{
					data.addMail(mail);
				}
				data.setMoney(userData.getMoney());
				data.setTimestamp(net.ess3.user.UserData.TimestampType.MUTE, userData.getMuteTimeout());
				data.setMuted(userData.getMuted());
				data.setNickname(userData.getNickname());
				for (String string : userData.getPowertools())
				{
					try
					{
						int id = Integer.parseInt(string);
						Material mat = Material.getMaterial(id);
						if (mat != null)
						{
							data.setPowertool(mat, userData.getPowertool(id));
						}
					}
					catch (NumberFormatException ex)
					{
					}
				}
				for (Integer integer : userData.getUnlimited())
				{
					Material mat = Material.getMaterial(integer);
					if (mat != null)
					{
						data.setUnlimited(mat, true);
					}
				}
				data.setAfk(userData.isAfk());
				data.setGodmode(userData.isGodModeEnabled());
				data.setJailed(userData.isJailed());
				data.setMuted(userData.isMuted());
				data.setSocialspy(userData.isSocialSpyEnabled());
				data.setTeleportEnabled(userData.isTeleportEnabled());
				user.queueSave();
			}
		}
	}

	private String getRealName(String name)
	{
		String realname = getPlayer(name);
		return realname == null ? BROKENNAME : realname;
	}
	private final Map<String, String> players = new HashMap<String, String>();

	private String getPlayer(String check)
	{
		synchronized (players)
		{
			if (players.isEmpty())
			{
				File worldFolder = ess.getServer().getWorlds().get(0).getWorldFolder();
				File playersFolder = new File(worldFolder, "players");
				for (String filename : playersFolder.list(new PatternFilenameFilter(".+\\.dat")))
				{
					String name = filename.substring(0, filename.length() - 4).toLowerCase(Locale.ENGLISH);
					String sanitizedName = Util.sanitizeFileName(name);
					String mapName = players.get(sanitizedName);
					if (mapName != null && !mapName.equals(name))
					{
						players.put(sanitizedName, BROKENNAME);
					}
					else
					{
						players.put(sanitizedName, name);
					}
				}
			}
		}
		return players.get(check);
	}
}
