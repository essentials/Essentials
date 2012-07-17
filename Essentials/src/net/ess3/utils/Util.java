package net.ess3.utils;

import de.bananaco.bpermissions.imp.Permissions;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;
import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.api.InvalidNameException;
import net.ess3.utils.gnu.inet.encoding.Punycode;
import net.ess3.utils.gnu.inet.encoding.PunycodeException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;


public final class Util
{
	private Util()
	{
	}
	private final static Pattern INVALIDFILECHARS = Pattern.compile("[^\u0020-\u007E\u0085\u00A0-\uD7FF\uE000-\uFFFC]");
	private final static Pattern INVALIDCHARS = Pattern.compile("[^\t\n\r\u0020-\u007E\u0085\u00A0-\uD7FF\uE000-\uFFFC]");

	public static String sanitizeFileName(String name) throws InvalidNameException
	{
		try
		{
			String r = name.toLowerCase(Locale.ENGLISH);
			r = r.replace('.', (char)('\ue200' + '.'));
			r = r.replace('\\', (char)('\ue200' + '\\'));
			r = r.replace('/', (char)('\ue200' + '/'));
			r = r.replace('"', (char)('\ue200' + '"'));
			r = r.replace('<', (char)('\ue200' + '<'));
			r = r.replace('>', (char)('\ue200' + '>'));
			r = r.replace('|', (char)('\ue200' + '|'));
			r = r.replace('?', (char)('\ue200' + '?'));
			r = r.replace('*', (char)('\ue200' + '*'));
			r = r.replace(':', (char)('\ue200' + ':'));
			r = r.replace('-', (char)('\ue200' + '-'));
			r = INVALIDFILECHARS.matcher(r).replaceAll("");
			return Punycode.encode(r);
		}
		catch (PunycodeException ex)
		{
			throw new InvalidNameException(ex);
		}
	}

	public static String decodeFileName(String name) throws InvalidNameException
	{
		try
		{
			String r = Punycode.decode(name);
			r = r.replace((char)('\ue200' + '.'), '.');
			r = r.replace((char)('\ue200' + '\\'), '\\');
			r = r.replace((char)('\ue200' + '/'), '/');
			r = r.replace((char)('\ue200' + '"'), '"');
			r = r.replace((char)('\ue200' + '<'), '<');
			r = r.replace((char)('\ue200' + '>'), '>');
			r = r.replace((char)('\ue200' + '|'), '|');
			r = r.replace((char)('\ue200' + '?'), '?');
			r = r.replace((char)('\ue200' + '*'), '*');
			r = r.replace((char)('\ue200' + ':'), ':');
			r = r.replace((char)('\ue200' + '-'), '-');
			return r;
		}
		catch (PunycodeException ex)
		{
			throw new InvalidNameException(ex);
		}
	}

	public static String sanitizeKey(String name)
	{
		return INVALIDCHARS.matcher(name.toLowerCase(Locale.ENGLISH)).replaceAll("_");
	}

	public static String sanitizeString(final String string)
	{
		return INVALIDCHARS.matcher(string).replaceAll("");
	}

	public static ItemStack convertBlockToItem(final Block block)
	{
		final ItemStack is = new ItemStack(block.getType(), 1, (short)0, block.getData());
		switch (is.getType())
		{
		case WOODEN_DOOR:
			is.setType(Material.WOOD_DOOR);
			is.setDurability((short)0);
			break;
		case IRON_DOOR_BLOCK:
			is.setType(Material.IRON_DOOR);
			is.setDurability((short)0);
			break;
		case SIGN_POST:
		case WALL_SIGN:
			is.setType(Material.SIGN);
			is.setDurability((short)0);
			break;
		case CROPS:
			is.setType(Material.SEEDS);
			is.setDurability((short)0);
			break;
		case CAKE_BLOCK:
			is.setType(Material.CAKE);
			is.setDurability((short)0);
			break;
		case BED_BLOCK:
			is.setType(Material.BED);
			is.setDurability((short)0);
			break;
		case REDSTONE_WIRE:
			is.setType(Material.REDSTONE);
			is.setDurability((short)0);
			break;
		case REDSTONE_TORCH_OFF:
		case REDSTONE_TORCH_ON:
			is.setType(Material.REDSTONE_TORCH_ON);
			is.setDurability((short)0);
			break;
		case DIODE_BLOCK_OFF:
		case DIODE_BLOCK_ON:
			is.setType(Material.DIODE);
			is.setDurability((short)0);
			break;
		case DOUBLE_STEP:
			is.setType(Material.STEP);
			break;
		case TORCH:
		case RAILS:
		case LADDER:
		case WOOD_STAIRS:
		case COBBLESTONE_STAIRS:
		case LEVER:
		case STONE_BUTTON:
		case FURNACE:
		case DISPENSER:
		case PUMPKIN:
		case JACK_O_LANTERN:
		case WOOD_PLATE:
		case STONE_PLATE:
		case PISTON_STICKY_BASE:
		case PISTON_BASE:
		case IRON_FENCE:
		case THIN_GLASS:
		case TRAP_DOOR:
		case FENCE:
		case FENCE_GATE:
		case NETHER_FENCE:
			is.setDurability((short)0);
			break;
		case FIRE:
			return null;
		case PUMPKIN_STEM:
			is.setType(Material.PUMPKIN_SEEDS);
			break;
		case MELON_STEM:
			is.setType(Material.MELON_SEEDS);
			break;
		}
		return is;
	}
	private static DecimalFormat dFormat = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));

	public static String formatAsCurrency(final double value)
	{

		String str = dFormat.format(value);
		if (str.endsWith(".00"))
		{
			str = str.substring(0, str.length() - 3);
		}
		return str;
	}

	public static String displayCurrency(final double value, final IEssentials ess)
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		return _("currency", settings.getData().getEconomy().getCurrencySymbol(), formatAsCurrency(value));
	}

	public static String shortCurrency(final double value, final IEssentials ess)
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		return settings.getData().getEconomy().getCurrencySymbol() + formatAsCurrency(value);
	}

	public static double roundDouble(final double d)
	{
		return Math.round(d * 100.0) / 100.0;
	}

	public static boolean isInt(final String sInt)
	{
		try
		{
			Integer.parseInt(sInt);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	public static String joinList(Object... list)
	{
		return joinList(", ", list);
	}

	public static String joinList(String seperator, Object... list)
	{
		StringBuilder buf = new StringBuilder();
		for (Object each : list)
		{
			if (buf.length() > 0)
			{
				buf.append(seperator);
			}

			if (each instanceof Collection)
			{
				buf.append(joinList(seperator, ((Collection)each).toArray()));
			}
			else
			{
				try
				{
					buf.append(each.toString());
				}
				catch (Exception e)
				{
					buf.append(each.toString());
				}
			}
		}
		return buf.toString();
	}

	public static void registerPermissions(String path, Collection<String> nodes, boolean hasDefault, IEssentials ess)
	{
		if (nodes == null || nodes.isEmpty())
		{
			return;
		}
		final PluginManager pluginManager = ess.getServer().getPluginManager();
		Permission basePerm = pluginManager.getPermission(path + ".*");
		if (basePerm != null && !basePerm.getChildren().isEmpty())
		{
			basePerm.getChildren().clear();
		}
		if (basePerm == null)
		{
			basePerm = new Permission(path + ".*", PermissionDefault.OP);
			pluginManager.addPermission(basePerm);
			Permission mainPerm = pluginManager.getPermission("essentials.*");
			if (mainPerm == null)
			{
				mainPerm = new Permission("essentials.*", PermissionDefault.OP);
				pluginManager.addPermission(mainPerm);
			}
			mainPerm.getChildren().put(basePerm.getName(), Boolean.TRUE);
		}

		for (String nodeName : nodes)
		{
			final String permissionName = path + "." + nodeName;
			Permission perm = pluginManager.getPermission(permissionName);
			if (perm == null)
			{
				final PermissionDefault defaultPerm = hasDefault && nodeName.equalsIgnoreCase("default") ? PermissionDefault.TRUE : PermissionDefault.OP;
				perm = new Permission(permissionName, defaultPerm);
				pluginManager.addPermission(perm);
			}
			basePerm.getChildren().put(permissionName, Boolean.TRUE);
		}
		basePerm.recalculatePermissibles();
	}
	private static transient final Pattern DOT_PATTERN = Pattern.compile("\\.");

	public static Permission registerPermission(String permission, PermissionDefault defaultPerm)
	{
		final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		final String[] parts = DOT_PATTERN.split(permission);
		final StringBuilder builder = new StringBuilder(permission.length());
		Permission parent = null;
		for (int i = 0; i < parts.length - 1; i++)
		{
			builder.append(parts[i]).append(".*");
			String permString = builder.toString();
			Permission perm = pluginManager.getPermission(permString);
			if (perm == null)
			{
				perm = new Permission(permString, PermissionDefault.FALSE);
				pluginManager.addPermission(perm);
				if (parent != null)
				{
					parent.getChildren().put(perm.getName(), Boolean.TRUE);
				}
				parent = perm;
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		Permission perm = pluginManager.getPermission(permission);
		if (perm == null)
		{
			perm = new Permission(permission, defaultPerm);
			pluginManager.addPermission(perm);
			if (parent != null)
			{
				parent.getChildren().put(perm.getName(), Boolean.TRUE);
			}
			parent = perm;
		}
		perm.recalculatePermissibles();
		return perm;
	}
	private static transient final Pattern URL_PATTERN = Pattern.compile("((?:(?:https?)://)?[\\w-_\\.]{2,})\\.([a-z]{2,3}(?:/\\S+)?)");
	private static transient final Pattern VANILLA_PATTERN = Pattern.compile("\u00A7+[0-9A-FK-ORa-fk-or]");
	private static transient final Pattern REPLACE_PATTERN = Pattern.compile("&([0-9a-fk-or])");
	private static transient final Pattern VANILLA_COLOR_PATTERN = Pattern.compile("\u00A7+[0-9A-Fa-f]");
	private static transient final Pattern VANILLA_MAGIC_PATTERN = Pattern.compile("\u00A7+[Kk]");
	private static transient final Pattern VANILLA_FORMAT_PATTERN = Pattern.compile("\u00A7+[L-ORl-or]");
	private static transient final Pattern REPLACE_COLOR_PATTERN = Pattern.compile("&([0-9a-f])");
	private static transient final Pattern REPLACE_MAGIC_PATTERN = Pattern.compile("&(k)");
	private static transient final Pattern REPLACE_FORMAT_PATTERN = Pattern.compile("&([l-or])");

	public static String stripFormat(final String input)
	{
		if (input == null)
		{
			return null;
		}
		return VANILLA_PATTERN.matcher(input).replaceAll("");
	}

	public static String replaceFormat(final String input)
	{
		if (input == null)
		{
			return null;
		}
		return REPLACE_PATTERN.matcher(input).replaceAll("\u00a7$1");
	}

	public static String blockURL(final String input)
	{
		if (input == null)
		{
			return null;
		}
		String text = URL_PATTERN.matcher(input).replaceAll("$1 $2");
		while (URL_PATTERN.matcher(text).find())
		{
			text = URL_PATTERN.matcher(text).replaceAll("$1 $2");
		}
		return text;
	}

	public static String formatString(final IUser user, final String permBase, final String input)
	{
		if (input == null)
		{
			return null;
		}
		String message;
		if (Permissions.hasPermission(user.getBase(), permBase + ".color"))
		{
			message = Util.replaceColor(input, REPLACE_COLOR_PATTERN);
		}
		else
		{
			message = Util.stripColor(input, VANILLA_COLOR_PATTERN);
		}
		if (Permissions.hasPermission(user.getBase(), permBase + ".magic"))
		{
			message = Util.replaceColor(message, REPLACE_MAGIC_PATTERN);
		}
		else
		{
			message = Util.stripColor(message, VANILLA_MAGIC_PATTERN);
		}
		if (Permissions.hasPermission(user.getBase(), permBase + ".format"))
		{
			message = Util.replaceColor(message, REPLACE_FORMAT_PATTERN);
		}
		else
		{
			message = Util.stripColor(message, VANILLA_FORMAT_PATTERN);
		}
		return message;
	}

	public static String formatMessage(final IUser user, final String permBase, final String input)
	{
		if (input == null)
		{
			return null;
		}
		String message = formatString(user, permBase, input);
		if (!Permissions.hasPermission(user.getBase(), permBase + ".url"))
		{
			message = Util.blockURL(message);
		}
		return message;
	}

	public static String stripColor(final String input)
	{
		if (input == null)
		{
			return null;
		}

		return VANILLA_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	private static String stripColor(final String input, final Pattern pattern)
	{
		return pattern.matcher(input).replaceAll("");
	}

	private static String replaceColor(final String input, final Pattern pattern)
	{
		return pattern.matcher(input).replaceAll("\u00a7$1");
	}
}
