package net.ess3.utils.textreader;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.utils.DescParseTickFormat;
import net.ess3.utils.FormatUtil;
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
		String userName, address, version; //TODO: unused?
		if (sender instanceof IUser)
		{
			final IUser user = (IUser)sender;
			final Player player = user.getPlayer();
			displayName = player.getDisplayName();
			userName = player.getName();
			ipAddress = player.getAddress() == null || player.getAddress().getAddress() == null ? "" : player.getAddress().getAddress().toString();
			address = player.getAddress() == null ? "" : player.getAddress().toString();
			balance = FormatUtil.displayCurrency(user.getMoney(), ess);
			mails = Integer.toString(user.getData().getMails() == null ? 0 : user.getData().getMails().size());
			world = player.getLocation() == null || player.getLocation().getWorld() == null ? "" : player.getLocation().getWorld().getName();
			worldTime12 = DescParseTickFormat.format12(player.getWorld() == null ? 0 : player.getWorld().getTime());
			worldTime24 = DescParseTickFormat.format24(player.getWorld() == null ? 0 : player.getWorld().getTime());
			worldDate = DateFormat.getDateInstance(DateFormat.MEDIUM, ess.getI18n().getCurrentLocale()).format(
					DescParseTickFormat.ticksToDate(player.getWorld() == null ? 0 : player.getWorld().getFullTime()));
		}
		else
		{
			displayName = ipAddress = balance = mails = world = worldTime12 = worldTime24 = worldDate = "";
		}

		int playerHidden = 0;
		if (sender instanceof IUser)
		{
			final Player playerUser = ((IUser)sender).getPlayer();
			for (Player p : ess.getServer().getOnlinePlayers())
			{
				if (!p.canSee(playerUser))
				{
					playerHidden++;
				}
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
		if (sender instanceof IUser)
		{
			final Player playerUser = ((IUser)sender).getPlayer();
			for (Player p : ess.getServer().getOnlinePlayers())
			{
				if (!p.canSee(playerUser))
				{
					continue;
				}
				if (playerlistBuilder.length() > 0)
				{
					playerlistBuilder.append(", ");
				}
				playerlistBuilder.append(p.getDisplayName());
			}
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
			// TODO: Would it be nice to let plugins register with essentials and add these?
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
