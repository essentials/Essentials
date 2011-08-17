package com.earth2me.essentials;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.config.Configuration;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.reader.ReaderException;


public class EssentialsConf extends Configuration
{
	private static final Logger logger = Logger.getLogger("Minecraft");
	private File configFile;
	private String templateName = null;
	private Class<?> resourceClass = EssentialsConf.class;

	public EssentialsConf(File configFile)
	{
		super(configFile);
		this.configFile = configFile;
		if (this.root == null)
		{
			this.root = new HashMap<String, Object>();
		}
	}

	@Override
	public void load()
	{
		configFile = configFile.getAbsoluteFile();
		if (!configFile.getParentFile().exists())
		{
			if (!configFile.getParentFile().mkdirs())
			{
				logger.log(Level.SEVERE, Util.format("failedToCreateConfig", configFile.toString()));
			}
		}
		if (!configFile.exists())
		{
			if (templateName != null)
			{
				logger.log(Level.INFO, Util.format("creatingConfigFromTemplate", configFile.toString()));
				createFromTemplate();
			}
			else
			{
				try
				{
					logger.log(Level.INFO, Util.format("creatingEmptyConfig", configFile.toString()));
					if (!configFile.createNewFile())
					{
						logger.log(Level.SEVERE, Util.format("failedToCreateConfig", configFile.toString()));
					}
				}
				catch (IOException ex)
				{
					logger.log(Level.SEVERE, Util.format("failedToCreateConfig", configFile.toString()), ex);
				}
			}
		}
		try {
		super.load();
		}
		catch (ReaderException e) {
			logger.log(Level.SEVERE, "You have an encoding error in your Essentials settings.yml file.  Resave the file as UTF-8.\nError: " + e.toString());
			logger.log(Level.SEVERE, "The essentials settings file could not be loaded! Running on defaults!");
		}
		catch (YAMLException e) {
			logger.log(Level.SEVERE, "You have an YAML error in your Essentials settings.yml file.\nTry running your file through http://ess.khhq.net/yaml/\nError:" + e.toString());
			logger.log(Level.SEVERE, "The essentials settings file could not be loaded! Running on defaults!");

		}
		if (this.root == null)
		{
			this.root = new HashMap<String, Object>();
		}
	}

	private void createFromTemplate()
	{
		InputStream istr = null;
		OutputStream ostr = null;
		try
		{
			istr = resourceClass.getResourceAsStream(templateName);
			if (istr == null)
			{
				logger.log(Level.SEVERE, Util.format("couldNotFindTemplate", templateName));
				return;
			}
			ostr = new FileOutputStream(configFile);
			byte[] buffer = new byte[1024];
			int length = 0;
			length = istr.read(buffer);
			while (length > 0)
			{
				ostr.write(buffer, 0, length);
				length = istr.read(buffer);
			}
		}
		catch (IOException ex)
		{
			logger.log(Level.SEVERE, Util.format("failedToWriteConfig", configFile.toString()), ex);
			return;
		}
		finally
		{
			try
			{
				if (istr != null)
				{
					istr.close();
				}
			}
			catch (IOException ex)
			{
				Logger.getLogger(EssentialsConf.class.getName()).log(Level.SEVERE, null, ex);
			}
			try
			{
				if (ostr != null)
				{
					ostr.close();
				}
			}
			catch (IOException ex)
			{
				logger.log(Level.SEVERE, Util.format("failedToCloseConfig", configFile.toString()), ex);
			}
		}
	}

	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}

	public File getFile()
	{
		return configFile;
	}

	public void setTemplateName(String templateName, Class<?> resClass)
	{
		this.templateName = templateName;
		this.resourceClass = resClass;
	}

	public boolean hasProperty(String path)
	{
		return getProperty(path) != null;
	}

	public Location getLocation(String path, Server server)
	{
		String worldName = getString((path != null ? path + "." : "") + "world");
		if (worldName == null || worldName.isEmpty())
		{
			return null;
		}
		World world = server.getWorld(worldName);
		if (world == null)
		{
			return null;
		}
		return new Location(world,
							getDouble((path != null ? path + "." : "") + "x", 0),
							getDouble((path != null ? path + "." : "") + "y", 0),
							getDouble((path != null ? path + "." : "") + "z", 0),
							(float)getDouble((path != null ? path + "." : "") + "yaw", 0),
							(float)getDouble((path != null ? path + "." : "") + "pitch", 0));
	}

	public void setProperty(String path, Location loc)
	{
		setProperty((path != null ? path + "." : "") + "world", loc.getWorld().getName());
		setProperty((path != null ? path + "." : "") + "x", loc.getX());
		setProperty((path != null ? path + "." : "") + "y", loc.getY());
		setProperty((path != null ? path + "." : "") + "z", loc.getZ());
		setProperty((path != null ? path + "." : "") + "yaw", loc.getYaw());
		setProperty((path != null ? path + "." : "") + "pitch", loc.getPitch());
	}

	public ItemStack getItemStack(String path)
	{
		return new ItemStack(
				Material.valueOf(getString(path + ".type", "AIR")),
				getInt(path + ".amount", 1),
				(short)getInt(path + ".damage", 0)/*,
				(byte)getInt(path + ".data", 0)*/);
	}

	public void setProperty(String path, ItemStack stack)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", stack.getType().toString());
		map.put("amount", stack.getAmount());
		map.put("damage", stack.getDurability());
		// getData().getData() is broken
		//map.put("data", stack.getDurability());
		setProperty(path, map);
	}

	public long getLong(String path, long def)
	{
		Number num;
		try
		{
			num = (Number)getProperty(path);
		}
		catch(ClassCastException ex)
		{
			return def;
		}
		if (num == null)
		{
			return def;
		}
		return num.longValue();
	}

	@Override
	public double getDouble(String path, double def)
	{
		Number num;
		try
		{
			num = (Number)getProperty(path);
		}
		catch(ClassCastException ex)
		{
			return def;
		}
		if (num == null)
		{
			return def;
		}
		return num.doubleValue();
	}
}
