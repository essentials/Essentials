package net.ess3.utils.textreader;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.utils.DescParseTickFormat;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class KeywordReplacer implements IText
{
	private final transient IText input;
	private final transient List<String> replaced;
	private final transient IEssentials ess;
	
	public KeywordReplacer(final IText input, final CommandSender sender, final IEssentials ess)
	{
		this.input = input;
		this.replaced = new ArrayList<String>(this.input.getLines().size());
		this.ess = ess;
		replaceKeywords(sender);
	}
	
	private void replaceKeywords(final CommandSender sender)
	{
		String displayName, ipAddress, balance, mails, world;
		String worlds, online, unique, playerlist, date, time;
		String worldTime12, worldTime24, worldDate, plugins;
		String userName, address, version;
		if (sender instanceof Player)
		{
			final IUser user = ess.getUserMap().getUser((Player)sender);
			user.setDisplayNick();
			displayName = user.getPlayer().getDisplayName();
			userName = user.getPlayer().getName();
			ipAddress = user.getPlayer().getAddress() == null || user.getPlayer().getAddress().getAddress() == null ? "" : user.getPlayer().getAddress().getAddress().toString();
			address = user.getPlayer().getAddress() == null ? "" : user.getPlayer().getAddress().toString();
			balance = Double.toString(user.getMoney());
			mails = Integer.toString(user.getData().getMails() == null ? 0 : user.getData().getMails().size());
			world = user.getPlayer().getLocation() == null || user.getPlayer().getLocation().getWorld() == null ? "" : user.getPlayer().getLocation().getWorld().getName();
			worldTime12 = DescParseTickFormat.format12(user.getPlayer().getWorld() == null ? 0 : user.getPlayer().getWorld().getTime());
			worldTime24 = DescParseTickFormat.format24(user.getPlayer().getWorld() == null ? 0 : user.getPlayer().getWorld().getTime());
			worldDate = DateFormat.getDateInstance(DateFormat.MEDIUM, ess.getI18n().getCurrentLocale()).format(DescParseTickFormat.ticksToDate(user.getPlayer().getWorld() == null ? 0 : user.getPlayer().getWorld().getFullTime()));
		}
		else
		{
			displayName = ipAddress = balance = mails = world = worldTime12 = worldTime24 = worldDate = "";
		}

		int playerHidden = 0;
		for (Player p : ess.getServer().getOnlinePlayers())
		{
			if (ess.getUserMap().getUser(p).isHidden())
			{
				playerHidden++;
			}
		}
		online = Integer.toString(ess.getServer().getOnlinePlayers().length - playerHidden);
		unique = Integer.toString(ess.getUserMap().getUniqueUsers());

		final StringBuilder worldsBuilder = new StringBuilder();
		for (World w : ess.getServer().getWorlds())
		{
			if (worldsBuilder.length() > 0)
			{
				worldsBuilder.append(", ");
			}
			worldsBuilder.append(w.getName());
		}
		worlds = worldsBuilder.toString();

		final StringBuilder playerlistBuilder = new StringBuilder();
		for (Player p : ess.getServer().getOnlinePlayers())
		{
			if (ess.getUserMap().getUser(p).isHidden())
			{
				continue;
			}
			if (playerlistBuilder.length() > 0)
			{
				playerlistBuilder.append(", ");
			}
			playerlistBuilder.append(p.getDisplayName());
		}
		playerlist = playerlistBuilder.toString();

		final StringBuilder pluginlistBuilder = new StringBuilder();
		for (Plugin p : ess.getServer().getPluginManager().getPlugins())
		{
			if (pluginlistBuilder.length() > 0)
			{
				pluginlistBuilder.append(", ");
			}
			pluginlistBuilder.append(p.getDescription().getName());
		}
		plugins = pluginlistBuilder.toString();

		date = DateFormat.getDateInstance(DateFormat.MEDIUM, ess.getI18n().getCurrentLocale()).format(new Date());
		time = DateFormat.getTimeInstance(DateFormat.MEDIUM, ess.getI18n().getCurrentLocale()).format(new Date());

		version = ess.getServer().getVersion();

		for (int i = 0; i < input.getLines().size(); i++)
		{
			String line = input.getLines().get(i);

			line = line.replace("{PLAYER}", displayName);
			line = line.replace("{DISPLAYNAME}", displayName);
			line = line.replace("{USERNAME}", displayName);
			line = line.replace("{IP}", ipAddress);
			line = line.replace("{ADDRESS}", ipAddress);
			line = line.replace("{BALANCE}", balance);
			line = line.replace("{MAILS}", mails);
			line = line.replace("{WORLD}", world);
			line = line.replace("{ONLINE}", online);
			line = line.replace("{UNIQUE}", unique);
			line = line.replace("{WORLDS}", worlds);
			line = line.replace("{PLAYERLIST}", playerlist);
			line = line.replace("{TIME}", time);
			line = line.replace("{DATE}", date);
			line = line.replace("{WORLDTIME12}", worldTime12);
			line = line.replace("{WORLDTIME24}", worldTime24);
			line = line.replace("{WORLDDATE}", worldDate);
			line = line.replace("{PLUGINS}", plugins);
			line = line.replace("{VERSION}", version);
			replaced.add(line);
		}
	}

	@Override
	public List<String> getLines()
	{
		return replaced;
	}

	@Override
	public List<String> getChapters()
	{
		return input.getChapters();
	}

	@Override
	public Map<String, Integer> getBookmarks()
	{
		return input.getBookmarks();
	}
}
