package com.earth2me.essentials;

import com.earth2me.essentials.craftbukkit.BanLookup;
import com.earth2me.essentials.craftbukkit.FakeWorld;
import com.earth2me.essentials.settings.Spawns;
import com.earth2me.essentials.storage.YamlStorageWriter;
import com.earth2me.essentials.utils.StringUtil;
import com.google.common.base.Charsets;
import net.ess3.api.IEssentials;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.earth2me.essentials.I18n.tl;


public class EssentialsUpgrade
{
	private final static Logger LOGGER = Logger.getLogger("Essentials");
	private final transient IEssentials ess;
	private final transient EssentialsConf doneFile;

	EssentialsUpgrade(final IEssentials essentials)
	{
		ess = essentials;
		if (!ess.getDataFolder().exists())
		{
			ess.getDataFolder().mkdirs();
		}
		doneFile = new EssentialsConf(new File(ess.getDataFolder(), "upgrades-done.yml"));
		doneFile.load();
	}

	private void moveMotdRulesToFile(String name)
	{
		if (doneFile.getBoolean("move" + name + "ToFile", false))
		{
			return;
		}
		try
		{
			final File file = new File(ess.getDataFolder(), name + ".txt");
			if (file.exists())
			{
				return;
			}
			final File configFile = new File(ess.getDataFolder(), "config.yml");
			if (!configFile.exists())
			{
				return;
			}
			final EssentialsConf conf = new EssentialsConf(configFile);
			conf.load();
			List<String> lines = conf.getStringList(name);
			if (lines != null && !lines.isEmpty())
			{
				if (!file.createNewFile())
				{
					throw new IOException("Failed to create file " + file);
				}
				PrintWriter writer = new PrintWriter(file);

				for (String line : lines)
				{
					writer.println(line);
				}
				writer.close();
			}
			doneFile.setProperty("move" + name + "ToFile", true);
			doneFile.save();
		}
		catch (IOException e)
		{
			LOGGER.log(Level.SEVERE, tl("upgradingFilesError"), e);
		}
	}

	private void removeLinesFromConfig(File file, String regex, String info) throws Exception
	{
		boolean needUpdate = false;
		final BufferedReader bReader = new BufferedReader(new FileReader(file));
		final File tempFile = File.createTempFile("essentialsupgrade", ".tmp.yml", ess.getDataFolder());
		final BufferedWriter bWriter = new BufferedWriter(new FileWriter(tempFile));
		do
		{
			final String line = bReader.readLine();
			if (line == null)
			{
				break;
			}
			if (line.matches(regex))
			{
				if (!needUpdate && info != null)
				{
					bWriter.write(info, 0, info.length());
					bWriter.newLine();
				}
				needUpdate = true;
			}
			else
			{
				if (line.endsWith("\r\n"))
				{
					bWriter.write(line, 0, line.length() - 2);
				}
				else if (line.endsWith("\r") || line.endsWith("\n"))
				{
					bWriter.write(line, 0, line.length() - 1);
				}
				else
				{
					bWriter.write(line, 0, line.length());
				}
				bWriter.newLine();
			}
		}
		while (true);
		bReader.close();
		bWriter.close();
		if (needUpdate)
		{
			if (!file.renameTo(new File(file.getParentFile(), file.getName().concat("." + System.currentTimeMillis() + ".upgradebackup"))))
			{
				throw new Exception(tl("configFileMoveError"));
			}
			if (!tempFile.renameTo(file))
			{
				throw new Exception(tl("configFileRenameError"));
			}
		}
		else
		{
			tempFile.delete();
		}
	}

	private void updateUsersPowerToolsFormat()
	{
		if (doneFile.getBoolean("updateUsersPowerToolsFormat", false))
		{
			return;
		}
		final File userdataFolder = new File(ess.getDataFolder(), "userdata");
		if (!userdataFolder.exists() || !userdataFolder.isDirectory())
		{
			return;
		}
		final File[] userFiles = userdataFolder.listFiles();

		for (File file : userFiles)
		{
			if (!file.isFile() || !file.getName().endsWith(".yml"))
			{
				continue;
			}
			final EssentialsConf config = new EssentialsConf(file);
			try
			{
				config.load();
				if (config.hasProperty("powertools"))
				{
					@SuppressWarnings("unchecked")
					final Map<String, Object> powertools = config.getConfigurationSection("powertools").getValues(false);
					if (powertools == null)
					{
						continue;
					}
					for (Map.Entry<String, Object> entry : powertools.entrySet())
					{
						if (entry.getValue() instanceof String)
						{
							List<String> temp = new ArrayList<String>();
							temp.add((String)entry.getValue());
							((Map<String, Object>)powertools).put(entry.getKey(), temp);
						}
					}
					config.forceSave();
				}
			}
			catch (RuntimeException ex)
			{
				LOGGER.log(Level.INFO, "File: " + file.toString());
				throw ex;
			}
		}
		doneFile.setProperty("updateUsersPowerToolsFormat", true);
		doneFile.save();
	}

	private void updateUsersHomesFormat()
	{
		if (doneFile.getBoolean("updateUsersHomesFormat", false))
		{
			return;
		}
		final File userdataFolder = new File(ess.getDataFolder(), "userdata");
		if (!userdataFolder.exists() || !userdataFolder.isDirectory())
		{
			return;
		}
		final File[] userFiles = userdataFolder.listFiles();

		for (File file : userFiles)
		{
			if (!file.isFile() || !file.getName().endsWith(".yml"))
			{
				continue;
			}
			final EssentialsConf config = new EssentialsConf(file);
			try
			{

				config.load();
				if (config.hasProperty("home") && config.hasProperty("home.default"))
				{
					@SuppressWarnings("unchecked")
					final String defworld = (String)config.getProperty("home.default");
					final Location defloc = getFakeLocation(config, "home.worlds." + defworld);
					if (defloc != null)
					{
						config.setProperty("homes.home", defloc);
					}

					Set<String> worlds = config.getConfigurationSection("home.worlds").getKeys(false);
					Location loc;
					String worldName;

					if (worlds == null)
					{
						continue;
					}
					for (String world : worlds)
					{
						if (defworld.equalsIgnoreCase(world))
						{
							continue;
						}
						loc = getFakeLocation(config, "home.worlds." + world);
						if (loc == null)
						{
							continue;
						}
						worldName = loc.getWorld().getName().toLowerCase(Locale.ENGLISH);
						if (worldName != null && !worldName.isEmpty())
						{
							config.setProperty("homes." + worldName, loc);
						}
					}
					config.removeProperty("home");
					config.forceSave();
				}

			}
			catch (RuntimeException ex)
			{
				LOGGER.log(Level.INFO, "File: " + file.toString());
				throw ex;
			}
		}
		doneFile.setProperty("updateUsersHomesFormat", true);
		doneFile.save();
	}

	private void sanitizeAllUserFilenames()
	{
		if (doneFile.getBoolean("sanitizeAllUserFilenames", false))
		{
			return;
		}
		final File usersFolder = new File(ess.getDataFolder(), "userdata");
		if (!usersFolder.exists())
		{
			return;
		}
		final File[] listOfFiles = usersFolder.listFiles();
		for (File listOfFile : listOfFiles)
		{
			final String filename = listOfFile.getName();
			if (!listOfFile.isFile() || !filename.endsWith(".yml"))
			{
				continue;
			}
			final String sanitizedFilename = StringUtil.sanitizeFileName(filename.substring(0, filename.length() - 4)) + ".yml";
			if (sanitizedFilename.equals(filename))
			{
				continue;
			}
			final File tmpFile = new File(listOfFile.getParentFile(), sanitizedFilename + ".tmp");
			final File newFile = new File(listOfFile.getParentFile(), sanitizedFilename);
			if (!listOfFile.renameTo(tmpFile))
			{
				LOGGER.log(Level.WARNING, tl("userdataMoveError", filename, sanitizedFilename));
				continue;
			}
			if (newFile.exists())
			{
				LOGGER.log(Level.WARNING, tl("duplicatedUserdata", filename, sanitizedFilename));
				continue;
			}
			if (!tmpFile.renameTo(newFile))
			{
				LOGGER.log(Level.WARNING, tl("userdataMoveBackError", sanitizedFilename, sanitizedFilename));
			}
		}
		doneFile.setProperty("sanitizeAllUserFilenames", true);
		doneFile.save();
	}

	private World getFakeWorld(final String name)
	{
		final File bukkitDirectory = ess.getDataFolder().getParentFile().getParentFile();
		final File worldDirectory = new File(bukkitDirectory, name);
		if (worldDirectory.exists() && worldDirectory.isDirectory())
		{
			return new FakeWorld(worldDirectory.getName(), World.Environment.NORMAL);
		}
		return null;
	}

	public Location getFakeLocation(EssentialsConf config, String path)
	{
		String worldName = config.getString((path != null ? path + "." : "") + "world");
		if (worldName == null || worldName.isEmpty())
		{
			return null;
		}
		World world = getFakeWorld(worldName);
		if (world == null)
		{
			return null;
		}
		return new Location(world,
							config.getDouble((path != null ? path + "." : "") + "x", 0),
							config.getDouble((path != null ? path + "." : "") + "y", 0),
							config.getDouble((path != null ? path + "." : "") + "z", 0),
							(float)config.getDouble((path != null ? path + "." : "") + "yaw", 0),
							(float)config.getDouble((path != null ? path + "." : "") + "pitch", 0));
	}

	private void deleteOldItemsCsv()
	{
		if (doneFile.getBoolean("deleteOldItemsCsv", false))
		{
			return;
		}
		final File file = new File(ess.getDataFolder(), "items.csv");
		if (file.exists())
		{
			try
			{
				final Set<BigInteger> oldconfigs = new HashSet<BigInteger>();
				oldconfigs.add(new BigInteger("66ec40b09ac167079f558d1099e39f10", 16)); // sep 1
				oldconfigs.add(new BigInteger("34284de1ead43b0bee2aae85e75c041d", 16)); // crlf
				oldconfigs.add(new BigInteger("c33bc9b8ee003861611bbc2f48eb6f4f", 16)); // jul 24
				oldconfigs.add(new BigInteger("6ff17925430735129fc2a02f830c1daa", 16)); // crlf

				MessageDigest digest = ManagedFile.getDigest();
				final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				final DigestInputStream dis = new DigestInputStream(bis, digest);
				final byte[] buffer = new byte[1024];
				try
				{
					while (dis.read(buffer) != -1)
					{
					}
				}
				finally
				{
					dis.close();
				}

				BigInteger hash = new BigInteger(1, digest.digest());
				if (oldconfigs.contains(hash) && !file.delete())
				{
					throw new IOException("Could not delete file " + file.toString());
				}
				doneFile.setProperty("deleteOldItemsCsv", true);
				doneFile.save();
			}
			catch (IOException ex)
			{
				Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}

	private void updateSpawnsToNewSpawnsConfig()
	{
		if (doneFile.getBoolean("updateSpawnsToNewSpawnsConfig", false))
		{
			return;
		}
		final File configFile = new File(ess.getDataFolder(), "spawn.yml");
		if (configFile.exists())
		{

			final EssentialsConf config = new EssentialsConf(configFile);
			try
			{
				config.load();
				if (!config.hasProperty("spawns"))
				{
					final Spawns spawns = new Spawns();
					Set<String> keys = config.getKeys(false);
					for (String group : keys)
					{
						Location loc = getFakeLocation(config, group);
						spawns.getSpawns().put(group.toLowerCase(Locale.ENGLISH), loc);
					}
					if (!configFile.renameTo(new File(ess.getDataFolder(), "spawn.yml.old")))
					{
						throw new Exception(tl("fileRenameError", "spawn.yml"));
					}
					PrintWriter writer = new PrintWriter(configFile);
					try
					{
						new YamlStorageWriter(writer).save(spawns);
					}
					finally
					{
						writer.close();
					}
				}
			}
			catch (Exception ex)
			{
				Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		doneFile.setProperty("updateSpawnsToNewSpawnsConfig", true);
		doneFile.save();
	}

	private void updateJailsToNewJailsConfig()
	{
		if (doneFile.getBoolean("updateJailsToNewJailsConfig", false))
		{
			return;
		}
		final File configFile = new File(ess.getDataFolder(), "jail.yml");
		if (configFile.exists())
		{

			final EssentialsConf config = new EssentialsConf(configFile);
			try
			{
				config.load();
				if (!config.hasProperty("jails"))
				{
					final com.earth2me.essentials.settings.Jails jails = new com.earth2me.essentials.settings.Jails();
					Set<String> keys = config.getKeys(false);
					for (String jailName : keys)
					{
						Location loc = getFakeLocation(config, jailName);
						jails.getJails().put(jailName.toLowerCase(Locale.ENGLISH), loc);
					}
					if (!configFile.renameTo(new File(ess.getDataFolder(), "jail.yml.old")))
					{
						throw new Exception(tl("fileRenameError", "jail.yml"));
					}
					PrintWriter writer = new PrintWriter(configFile);
					try
					{
						new YamlStorageWriter(writer).save(jails);
					}
					finally
					{
						writer.close();
					}
				}
			}
			catch (Exception ex)
			{
				Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		doneFile.setProperty("updateJailsToNewJailsConfig", true);
		doneFile.save();
	}

	private void warnMetrics()
	{
		if (doneFile.getBoolean("warnMetrics", false))
		{
			return;
		}
		ess.getSettings().setMetricsEnabled(false);
		doneFile.setProperty("warnMetrics", true);
		doneFile.save();
	}

	private void uuidFileChange()
	{
		if (doneFile.getBoolean("uuidFileChange", false))
		{
			return;
		}

		Boolean ignoreUFCache = doneFile.getBoolean("ignore-userfiles-cache", false);

		ess.getLogger().info("#### Starting Essentials UUID userdata conversion in a few seconds. ####"); // TODO TL
		ess.getLogger().info("We recommend you take a backup of your server before upgrading from the old username system.");

		try
		{
			Thread.sleep(15000);
		}
		catch (InterruptedException ex)
		{
			// NOOP
		}

		uuidFileConvert(ess, ignoreUFCache);

		doneFile.setProperty("uuidFileChange", true);
		doneFile.save();
	}

	public static void uuidFileConvert(IEssentials ess, Boolean ignoreUFCache)
	{
		ess.getLogger().info("Starting Essentials UUID userdata conversion"); // TODO TL
		final File userdir = new File(ess.getDataFolder(), "userdata");
		if (!userdir.exists())
		{
			return;
		}

		List<String> toConvert = new ArrayList<String>();
		for (String string : userdir.list())
		{
			if (!string.endsWith(".yml") || string.length() < 5)
			{
				continue;
			}

			final String name = string.substring(0, string.length() - 4);

			try
			{
				UUID.fromString(name);
			}
			catch (IllegalArgumentException ex)
			{
				toConvert.add(name);
			}
		}

		int countFiles = 0;
		int countFails = 0;
		int countEssCache = 0;
		int countMojang = 0;

		int countTotal = toConvert.size();
		if (countTotal == 0)
		{
			ess.getLogger().info("Nothing to do!");
			return;
		}

		List<String> converted = new ArrayList<String>();
		// Attempt as many from cache as possible first
		for (int i = 0; i < countTotal; i++)
		{
			final String name = toConvert.get(i);
			EssentialsConf conf = new EssentialsConf(new File(userdir, name + ".yml"));
			conf.load();
			conf.setProperty("lastAccountName", name);

			String uuidstr = conf.getString(ignoreUFCache ? "force-uuid" : "uuid");

			UUID uuid = null;
			try
			{
				uuid = UUID.fromString(uuidstr);
				countEssCache++;
			}
			catch (Exception e)
			{
				if (conf.getBoolean("npc", false))
				{
					uuid = UUID.nameUUIDFromBytes(("NPC:" + name).getBytes(Charsets.UTF_8));
					countEssCache++;
				}
			}

			conf.forceSave();
			if (uuid != null)
			{
				EssentialsUserConf config = new EssentialsUserConf(name, uuid, new File(uuid + ".yml"));
				config.convertLegacyFile();
				ess.getUserMap().trackUUID(uuid, name, false);

				converted.add(name);
			}
		}

		for (String s : converted)
		{
			toConvert.remove(s);
		}

		converted.clear();

		ess.getLogger().info("Converted " + countEssCache + " files from cache. Now converting " + toConvert.size() + " files using Mojang API"); // TODO TL

		Matcher matcher = Pattern.compile("([0-9a-f]{8})([0-9a-f]{4})([0-9a-f]{4})([0-9a-f]{4})([0-9a-f]+)").matcher("");

		ess.getLogger().info("We estimate that this operation will take " + Math.ceil((toConvert.size() / 60000) * 15) + "minutes due to API restrictions."); // TODO TL

		for (int i = 0; i < toConvert.size(); i += 100)
		{
			final int end = Math.min(i + 99, countTotal);
			List<String> toQuery = toConvert.subList(i, end);
			ess.getLogger().info(String.format("Converting files %d to %d (%d files)", i + 1, end, toQuery.size())); // TODO TL

			JSONArray payload = new JSONArray();
			for (String name : toQuery)
			{
				payload.add(name);
			}

			try
			{
				HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.mojang.com/profiles/minecraft").openConnection();
				connection.setRequestMethod("POST");
				connection.addRequestProperty("Content-Type", "application/json");
				connection.setDoOutput(true);

				PrintWriter writer = new PrintWriter(connection.getOutputStream());
				writer.print(payload);
				writer.flush();
				writer.close();

				int response = connection.getResponseCode();
				if (response != 200)
				{
					ess.getLogger().severe("Got error " + response + " - " + connection.getResponseMessage() + " from API"); // TODO better handling
					if (response == 429)
					{
						ess.getLogger().severe("Too many requests sent to Mojang Server. Waiting 1 minute then trying again.");
						i -= 100;
						try
						{
							Thread.sleep(60000);
						}
						catch (InterruptedException e)
						{

						}
						continue;
					}
					return;
				}

				JSONArray obj = (JSONArray) JSONValue.parse(new InputStreamReader(connection.getInputStream()));

				for (Object object : obj)
				{
					JSONObject jsonObject = (JSONObject) object;

					String name = ((String)jsonObject.get("name")).toLowerCase();
					String id = (String)jsonObject.get("id");

					id = matcher.reset(id).replaceAll("$1-$2-$3-$4-$5");

					UUID uuid = UUID.fromString(id);


					EssentialsUserConf conf = new EssentialsUserConf(name, uuid, new File(userdir, StringUtil.sanitizeFileName(name) + ".yml"));
					conf.convertLegacyFile();
					ess.getUserMap().trackUUID(uuid, name, false);
					converted.add(name);
					countMojang++;
				}

				System.out.println(obj);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
				return;
			}
			catch (IOException e)
			{ // TODO Better handling
				e.printStackTrace();
				return;
			}

			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{

			}
		}

		for (String s : converted)
		{
			toConvert.remove(s);
		}

		converted.clear();

		ess.getUserMap().getUUIDMap().forceWriteUUIDMap();

		ess.getLogger().info("Converted " + (countTotal - toConvert.size()) + "/" + countTotal + ".  Conversion complete.");
		ess.getLogger().info("Converted via cache: " + countEssCache + " :: Converted via lookup: " + countMojang + " :: Failed to convert: " + toConvert.size());
		ess.getLogger().info("To rerun the conversion type /essentials uuidconvert");
	}

	public void banFormatChange()
	{
		if (doneFile.getBoolean("banFormatChange", false))
		{
			return;
		}

		ess.getLogger().info("Starting Essentials ban format conversion");

		final File userdir = new File(ess.getDataFolder(), "userdata");
		if (!userdir.exists())
		{
			return;
		}

		int countFiles = 0;

		ess.getLogger().info("Found " + userdir.list().length + " files to convert...");

		for (String string : userdir.list())
		{
			if (!string.endsWith(".yml") || string.length() < 5)
			{
				continue;
			}

			final int showProgress = countFiles % 250;

			if (showProgress == 0)
			{
				ess.getLogger().info("Converted " + countFiles + "/" + userdir.list().length);
			}

			countFiles++;
			final File pFile = new File(userdir, string);
			final EssentialsConf conf = new EssentialsConf(pFile);
			conf.load();
			
			String banReason;
			Long banTimeout;

			try
			{
				banReason = conf.getConfigurationSection("ban").getString("reason");
			}
			catch (NullPointerException n)
			{
				banReason = null;
			}

			final String playerName = conf.getString("lastAccountName");
			if (playerName != null && playerName.length() > 1 && banReason != null && banReason.length() > 1)
			{
				try
				{
					if (conf.getConfigurationSection("ban").contains("timeout"))
					{
						banTimeout = Long.parseLong(conf.getConfigurationSection("ban").getString("timeout"));
					}
					else
					{
						banTimeout = 0L;
					}
				}
				catch (NumberFormatException n)
				{
					banTimeout = 0L;
				}
				
				if (BanLookup.isBanned(ess, playerName))
				{
					updateBan(playerName, banReason, banTimeout);
				}
			}
			conf.removeProperty("ban");
			conf.save();
		}

		doneFile.setProperty("banFormatChange", true);
		doneFile.save();
		ess.getLogger().info("Ban format update complete.");
	}

	private void updateBan(String playerName, String banReason, Long banTimeout)
	{
		if (banTimeout == 0)
		{
			Bukkit.getBanList(BanList.Type.NAME).addBan(playerName, banReason, null, Console.NAME);
		}
		else
		{
			Bukkit.getBanList(BanList.Type.NAME).addBan(playerName, banReason, new Date(banTimeout), Console.NAME);
		}
	}

	public void beforeSettings()
	{
		if (!ess.getDataFolder().exists())
		{
			ess.getDataFolder().mkdirs();
		}
		moveMotdRulesToFile("motd");
		moveMotdRulesToFile("rules");
	}

	public void afterSettings()
	{
		sanitizeAllUserFilenames();
		updateUsersPowerToolsFormat();
		updateUsersHomesFormat();
		deleteOldItemsCsv();
		updateSpawnsToNewSpawnsConfig();
		updateJailsToNewJailsConfig();
		uuidFileChange();
		banFormatChange();
		warnMetrics();
	}
}
