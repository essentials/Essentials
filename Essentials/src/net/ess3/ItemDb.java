package net.ess3;

import static net.ess3.I18n._;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import net.ess3.api.ISettings;
import net.ess3.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.ess3.api.IEssentials;
import net.ess3.api.IItemDb;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.storage.ManagedFile;


public class ItemDb implements IItemDb
{
	private final transient IEssentials ess;

	public ItemDb(final IEssentials ess)
	{
		this.ess = ess;
		file = new ManagedFile("items.csv", ess);
	}

	private final transient Map<String, Long> items = new HashMap<String, Long>();
	private final transient ManagedFile file;
	private static final Pattern SPLIT = Pattern.compile("[^a-zA-Z0-9]");

	@Override
	public void onReload()
	{
		final List<String> lines = file.getLines();

		if (lines.isEmpty())
		{
			return;
		}

		items.clear();

		for (String line : lines)
		{
			line = line.trim();
			if (line.length() > 0 && line.charAt(0) == '#')
			{
				continue;
			}

			final String[] parts = SPLIT.split(line);
			if (parts.length < 2)
			{
				continue;
			}

			final long numeric = Integer.parseInt(parts[1]);

			final long durability = parts.length > 2 && !(parts[2].length() == 1 && parts[2].charAt(0) == '0') ? Short.parseShort(parts[2]) : 0;
			items.put(parts[0].toLowerCase(Locale.ENGLISH), numeric | (durability << 32));
		}
	}

	public ItemStack get(final String id, final IUser user) throws Exception
	{
		final ItemStack stack = get(id.toLowerCase(Locale.ENGLISH));

		ISettings settings = ess.getSettings();

		final int defaultStackSize = settings.getData().getGeneral().getDefaultStacksize();

		if (defaultStackSize > 0)
		{
			stack.setAmount(defaultStackSize);
		}
		else
		{
			final int oversizedStackSize = settings.getData().getGeneral().getOversizedStacksize();
			if (oversizedStackSize > 0 && Permissions.OVERSIZEDSTACKS.isAuthorized(user))
			{
				stack.setAmount(oversizedStackSize);
			}
		}
		return stack;
	}

	@Override
	public ItemStack get(final String id, final int quantity) throws Exception
	{
		final ItemStack retval = get(id.toLowerCase(Locale.ENGLISH));
		retval.setAmount(quantity);
		return retval;
	}

	private final Pattern idMatch = Pattern.compile("^\\d+[:+',;.]\\d+$");
	private final Pattern metaSplit = Pattern.compile("[:+',;.]");
	private final Pattern conjoined = Pattern.compile("^[^:+',;.]+[:+',;.]\\d+$");

	@Override
	public ItemStack get(final String id) throws Exception
	{
		int itemid = 0;
		String itemname = null;
		short metaData = 0;
		if (idMatch.matcher(id).matches())
		{
			String[] split = metaSplit.split(id);
			itemid = Integer.parseInt(split[0]);
			metaData = Short.parseShort(split[1]);
		}
		else if (Util.isInt(id))
		{
			itemid = Integer.parseInt(id);
		}
		else if (conjoined.matcher(id).matches())
		{
			String[] split = metaSplit.split(id);
			itemname = split[0].toLowerCase(Locale.ENGLISH);
			metaData = Short.parseShort(split[1]);
		}
		else
		{
			itemname = id.toLowerCase(Locale.ENGLISH);
		}

		if (itemname != null)
		{
			if (items.containsKey(itemname))
			{
				long item = items.get(itemname);
				itemid = (int)(item & 0xffffffffL);
				if (metaData == 0)
				{
					metaData = (short)((item >> 32) & 0xffffL);
				}
			}
			else if (Material.matchMaterial(itemname) != null)
			{
				itemid = Material.matchMaterial(itemname).getId();
				metaData = 0;
			}
			else
			{
				throw new Exception(_("unknownItemName", id));
			}
		}

		final Material mat = Material.getMaterial(itemid);
		if (mat == null)
		{
			throw new Exception(_("unknownItemId", itemid));
		}
		final ItemStack retval = new ItemStack(mat, mat.getMaxStackSize(), metaData);
		return retval;
	}
}
