package net.ess3.utils;

import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;
import net.ess3.api.InvalidNameException;
import net.ess3.utils.gnu.inet.encoding.Punycode;
import net.ess3.utils.gnu.inet.encoding.PunycodeException;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;


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
		final ItemStack is = new ItemStack(block.getType(), 1, block.getData());
		
		final short durability = 0;

		switch (is.getType())
		{
		case WOODEN_DOOR:
			is.setType(Material.WOOD_DOOR);
			is.setDurability(durability);
			break;
		case IRON_DOOR_BLOCK:
			is.setType(Material.IRON_DOOR);
			is.setDurability(durability);
			break;
		case SIGN_POST:
		case WALL_SIGN:
			is.setType(Material.SIGN);
			is.setDurability(durability);
			break;
		case CROPS:
			is.setType(Material.SEEDS);
			is.setDurability(durability);
			break;
		case CAKE_BLOCK:
			is.setType(Material.CAKE);
			is.setDurability(durability);
			break;
		case BED_BLOCK:
			is.setType(Material.BED);
			is.setDurability(durability);
			break;
		case REDSTONE_WIRE:
			is.setType(Material.REDSTONE);
			is.setDurability(durability);
			break;
		case REDSTONE_TORCH_OFF:
		case REDSTONE_TORCH_ON:
			is.setType(Material.REDSTONE_TORCH_ON);
			is.setDurability(durability);
			break;
		case DIODE_BLOCK_OFF:
		case DIODE_BLOCK_ON:
			is.setType(Material.DIODE);
			is.setDurability(durability);
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
			is.setDurability(durability);
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
				buf.append(joinList(seperator, ((Collection<?>)each).toArray()));
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

}
