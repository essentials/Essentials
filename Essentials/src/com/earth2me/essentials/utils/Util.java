package com.earth2me.essentials.utils;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.external.gnu.inet.encoding.Punycode;
import com.earth2me.essentials.external.gnu.inet.encoding.PunycodeException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.Cleanup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
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

	
	// The player can stand inside these materials 
	private static final Set<Integer> AIR_MATERIALS = new HashSet<Integer>();
	private static final HashSet<Byte> AIR_MATERIALS_TARGET = new HashSet<Byte>();

	static
	{
		AIR_MATERIALS.add(Material.AIR.getId());
		AIR_MATERIALS.add(Material.SAPLING.getId());
		AIR_MATERIALS.add(Material.POWERED_RAIL.getId());
		AIR_MATERIALS.add(Material.DETECTOR_RAIL.getId());
		AIR_MATERIALS.add(Material.LONG_GRASS.getId());
		AIR_MATERIALS.add(Material.DEAD_BUSH.getId());
		AIR_MATERIALS.add(Material.YELLOW_FLOWER.getId());
		AIR_MATERIALS.add(Material.RED_ROSE.getId());
		AIR_MATERIALS.add(Material.BROWN_MUSHROOM.getId());
		AIR_MATERIALS.add(Material.RED_MUSHROOM.getId());
		AIR_MATERIALS.add(Material.TORCH.getId());
		AIR_MATERIALS.add(Material.REDSTONE_WIRE.getId());
		AIR_MATERIALS.add(Material.SEEDS.getId());
		AIR_MATERIALS.add(Material.SIGN_POST.getId());
		AIR_MATERIALS.add(Material.WOODEN_DOOR.getId());
		AIR_MATERIALS.add(Material.LADDER.getId());
		AIR_MATERIALS.add(Material.RAILS.getId());
		AIR_MATERIALS.add(Material.WALL_SIGN.getId());
		AIR_MATERIALS.add(Material.LEVER.getId());
		AIR_MATERIALS.add(Material.STONE_PLATE.getId());
		AIR_MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
		AIR_MATERIALS.add(Material.WOOD_PLATE.getId());
		AIR_MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
		AIR_MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
		AIR_MATERIALS.add(Material.STONE_BUTTON.getId());
		AIR_MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
		AIR_MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
		AIR_MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
		AIR_MATERIALS.add(Material.TRAP_DOOR.getId());
		AIR_MATERIALS.add(Material.PUMPKIN_STEM.getId());
		AIR_MATERIALS.add(Material.MELON_STEM.getId());
		AIR_MATERIALS.add(Material.VINE.getId());
		AIR_MATERIALS.add(Material.NETHER_WARTS.getId());
		AIR_MATERIALS.add(Material.WATER_LILY.getId());

		for (Integer integer : AIR_MATERIALS)
		{
			AIR_MATERIALS_TARGET.add(integer.byteValue());
		}
		AIR_MATERIALS_TARGET.add((byte)Material.WATER.getId());
		AIR_MATERIALS_TARGET.add((byte)Material.STATIONARY_WATER.getId());
	}

	public static Location getTarget(final LivingEntity entity) throws Exception
	{
		final Block block = entity.getTargetBlock(AIR_MATERIALS_TARGET, 300);
		if (block == null)
		{
			throw new Exception("Not targeting a block");
		}
		return block.getLocation();
	}

	public static Location getSafeDestination(final Location loc) throws Exception
	{
		if (loc == null || loc.getWorld() == null)
		{
			throw new Exception(_("destinationNotSet"));
		}
		final World world = loc.getWorld();
		int x = loc.getBlockX();
		int y = (int)Math.round(loc.getY());
		int z = loc.getBlockZ();

		while (isBlockAboveAir(world, x, y, z))
		{
			y -= 1;
			if (y < 0)
			{
				break;
			}
		}

		while (isBlockUnsafe(world, x, y, z))
		{
			y += 1;
			if (y >= world.getHighestBlockYAt(x, z))
			{
				x += 1;
				break;
			}
		}
		while (isBlockUnsafe(world, x, y, z))
		{
			y -= 1;
			if (y <= 1)
			{
				x += 1;
				y = world.getHighestBlockYAt(x, z);
				if (x - 32 > loc.getBlockX())
				{
					throw new Exception(_("holeInFloor"));
				}
			}
		}
		return new Location(world, x + 0.5D, y, z + 0.5D, loc.getYaw(), loc.getPitch());
	}

	private static boolean isBlockAboveAir(final World world, final int x, final int y, final int z)
	{
		return AIR_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType().getId());
	}

	public static boolean isBlockUnsafe(final World world, final int x, final int y, final int z)
	{
		final Block below = world.getBlockAt(x, y - 1, z);
		if (below.getType() == Material.LAVA || below.getType() == Material.STATIONARY_LAVA)
		{
			return true;
		}

		if (below.getType() == Material.FIRE)
		{
			return true;
		}

		if ((!AIR_MATERIALS.contains(world.getBlockAt(x, y, z).getType().getId()))
			|| (!AIR_MATERIALS.contains(world.getBlockAt(x, y + 1, z).getType().getId())))
		{
			return true;
		}
		return isBlockAboveAir(world, x, y, z);
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
	private static transient final Pattern VANILLA_COLOR_PATTERN = Pattern.compile("\u00A7+[0-9A-FKa-fk]");
	private static transient final Pattern EASY_COLOR_PATTERN = Pattern.compile("&([0-9a-fk])");

	public static String stripColor(final String input)
	{
		if (input == null)
		{
			return null;
		}

		return VANILLA_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static String replaceColor(final String input)
	{
		if (input == null)
		{
			return null;
		}

		return EASY_COLOR_PATTERN.matcher(input).replaceAll("\u00a7$1");
	}
}
