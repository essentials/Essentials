package com.earth2me.essentials.utils;

import com.earth2me.essentials.Essentials;
import static com.earth2me.essentials.I18n.tl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.ess3.api.IUser;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;


public class LocationUtil
{
	// The player can stand inside these materials
	public static final Set<Material> HOLLOW_MATERIALS = new HashSet<Material>();
	private static final HashSet<Byte> TRANSPARENT_MATERIALS = new HashSet<Byte>();

	static
	{
		HOLLOW_MATERIALS.add(Material.AIR);
		HOLLOW_MATERIALS.add(Material.SAPLING);
		HOLLOW_MATERIALS.add(Material.POWERED_RAIL);
		HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL);
		HOLLOW_MATERIALS.add(Material.LONG_GRASS);
		HOLLOW_MATERIALS.add(Material.DEAD_BUSH);
		HOLLOW_MATERIALS.add(Material.YELLOW_FLOWER);
		HOLLOW_MATERIALS.add(Material.RED_ROSE);
		HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM);
		HOLLOW_MATERIALS.add(Material.RED_MUSHROOM);
		HOLLOW_MATERIALS.add(Material.TORCH);
		HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE);
		HOLLOW_MATERIALS.add(Material.SEEDS);
		HOLLOW_MATERIALS.add(Material.SIGN_POST);
		HOLLOW_MATERIALS.add(Material.WOODEN_DOOR);
		HOLLOW_MATERIALS.add(Material.LADDER);
		HOLLOW_MATERIALS.add(Material.RAILS);
		HOLLOW_MATERIALS.add(Material.WALL_SIGN);
		HOLLOW_MATERIALS.add(Material.LEVER);
		HOLLOW_MATERIALS.add(Material.STONE_PLATE);
		HOLLOW_MATERIALS.add(Material.IRON_DOOR_BLOCK);
		HOLLOW_MATERIALS.add(Material.WOOD_PLATE);
		HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_OFF);
		HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_ON);
		HOLLOW_MATERIALS.add(Material.STONE_BUTTON);
		HOLLOW_MATERIALS.add(Material.SNOW);
		HOLLOW_MATERIALS.add(Material.SUGAR_CANE_BLOCK);
		HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_OFF);
		HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_ON);
		HOLLOW_MATERIALS.add(Material.COMPARATOR_OFF);
		HOLLOW_MATERIALS.add(Material.COMPARATOR_ON);
		HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM);
		HOLLOW_MATERIALS.add(Material.MELON_STEM);
		HOLLOW_MATERIALS.add(Material.VINE);
		HOLLOW_MATERIALS.add(Material.FENCE_GATE);
		HOLLOW_MATERIALS.add(Material.NETHER_WARTS);
		HOLLOW_MATERIALS.add(Material.CARPET);
		HOLLOW_MATERIALS.add(Material.DOUBLE_PLANT);
		HOLLOW_MATERIALS.add(Material.IRON_PLATE);
		HOLLOW_MATERIALS.add(Material.GOLD_PLATE);

		for (Integer integer : HOLLOW_MATERIALS)
		{
			TRANSPARENT_MATERIALS.add(integer.byteValue());
		}
		TRANSPARENT_MATERIALS.add((byte)Material.WATER.getId());
		TRANSPARENT_MATERIALS.add((byte)Material.STATIONARY_WATER.getId());
	}
	public static final int RADIUS = 3;
	public static final Vector3D[] VOLUME;

	public static ItemStack convertBlockToItem(final Block block)
	{
		final ItemStack is = new ItemStack(block.getType(), 1, (short)block.getData());
		switch (is.getType())
		{
		case MONSTER_EGGS:
			switch(is.getDurability())
			{
			case 5:
			case 4:
			case 3:
			case 2:
				is.setType(Material.SMOOTH_BRICK);
				is.setDurability(is.getDurability() - 2);
				break;
			case 1:
				is.setType(Material.COBBLE_STONE);
				is.setDurability((short)0);
				break;
			default:
				is.setType(Material.STONE);
				is.setDurability((short)0);
				break;
			}
			break;
		case CROPS:
			if (is.getDurability() == 7)
			{
				is.setType(Material.WHEAT);
			}
			else
			{
				is.setType(Material.SEEDS);
			}
			is.setDurability((short)0);
			break;
		case POTATO:
			if (is.getDurability() == 7)
			{
				is.setType(Material.POTATO_ITEM);
				is.setDurability((short)0);
			}
			return null;
		case CARROT:
			if (is.getDurability() == 7)
			{
				is.setType(Material.CARROT_ITEM);
				is.setDurability((short)0);
			}
			return null;
		case BREWING_STAND:
			is.setType(Material.BREWING_STAND_ITEM);
			is.setDurability((short)0);
			break;
		case DIAMOND_ORE:
			is.setType(Material.DIAMOND);
			is.setDurability((short)0);
			break;
		case QUARTZ_ORE:
			is.setType(Material.QUARTZ);
			is.setDurability((short)0);
			break;
		case REDSTONE_ORE:
		case GLOWING_REDSTONE_ORE:
			is.setType(Material.REDSTONE);
			is.setDurability((short)0);
			break;
		case SUGAR_CANE_BLOCK:
			is.setType(Material.SUGAR_CANE);
			is.setDurability((short)0);
			break;
		case SNOW:
			is.setType(Material.SNOW_BALL);
			is.setDurability((short)0);
			break;
		case NETHER_WARTS:
			is.setType(Material.NETHER_STALK);
			is.setDurability((short)0);
			break;
		case SOIL:
		case MYCEL:
			is.setType(Material.DIRT);
			is.setDurability((short)0);
			break;
		case BURNING_FURNACE:
			is.setType(Material.FURNACE);
			is.setDurability((short)0);
			break;
		case TRIPWIRE:
			is.setType(Material.STRING);
			is.setDurability((short)0);
			break;
		case CAULDRON:
			is.setType(Material.CAULDRON_ITEM);
			is.setDurability((short)0);
			break;
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
		case COMPARATOR_OFF:
		case COMPARATOR_ON:
			is.setType(Material.COMPARATOR);
			is.setDurability((short)0);
			break;
		case PUMPKIN_STEM:
			is.setType(Material.PUMPKIN_SEEDS);
			is.setDurability((short)0);
			break;
		case MELON_STEM:
			is.setType(Material.MELON_SEEDS);
			is.setDurability((short)0);
			break;
		case DOUBLE_STEP:
			is.setType(Material.STEP);
			break;
		case WOODEN_DOUBLE_STEP:
			is.setType(Material.WOODEN_STEP);
			break;
		case ANVIL:
			is.setDurability((short)is.getDurability / 4);
		case TORCH:
		case RAILS:
		case POWERED_RAILS:
		case DETECTOR_RAILS:
		case ACTIVATOR_RAIL:
		case LADDER:
		case WOOD_STAIRS:
		case SPURCE_WOOD_STAIRS:
		case BIRCH_WOOD_STAIRS:
		case JUNGLE_WOOD_STAIRS:
		case COBBLESTONE_STAIRS:
		case QUARTZ_STAIRS:
		case COBBLESTONE_STAIRS:
		case BRICK_STAIRS:
		case SMOOTH_STAIRS:
		case NETHER_BRICK_STAIRS:
		case SANDSTONE_STAIRS:
		case ACACIA_STAIRS:
		case DARK_OAK_STAIRS:
		case LEVER:
		case STONE_BUTTON:
		case FURNACE:
		case DISPENSER:
		case PUMPKIN:
		case JACK_O_LANTERN:
		case WOOD_PLATE:
		case STONE_PLATE:
		case IRON_PLATE:
		case GOLD_PLATE:
		case PISTON_STICKY_BASE:
		case PISTON_BASE:
		case IRON_FENCE:
		case THIN_GLASS:
		case TRAP_DOOR:
		case FENCE:
		case FENCE_GATE:
		case NETHER_FENCE:
		case COBBLE_WALL:
		case JUKEBOX:
		case TRIPWIRE_HOOK:
		case WOOD_BUTTON:
		case DAYLIGHT_DETECTOR:
		case HOPPER:
		case DROPPER:
			is.setDurability((short)0);
			break;
		case FIRE:
		case PORTAL:
		case END_PORTAL:
		case END_PORTAL_FRAME:
		case WATER:
		case STATIONARY_WATER:
		case LAVA:
		case STATIONARY_LAVA:
		case LONG_GRASS:
		case DEAD_BUSH:
		case PISTON_EXTENSION:
		case PISTON_MOVING_PIECE:
		case VINE:
		case COMMAND:
		case TRAPPED_CHEST:
		case DAYLIGHT_DETECTOR:
		case DOUBLE_PLANT:
			return null;
		}
		return is;
	}


	public static class Vector3D
	{
		public int x;
		public int y;
		public int z;

		public Vector3D(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	static
	{
		List<Vector3D> pos = new ArrayList<Vector3D>();
		for (int x = -RADIUS; x <= RADIUS; x++)
		{
			for (int y = -RADIUS; y <= RADIUS; y++)
			{
				for (int z = -RADIUS; z <= RADIUS; z++)
				{
					pos.add(new Vector3D(x, y, z));
				}
			}
		}
		Collections.sort(
				pos, new Comparator<Vector3D>()
		{
			@Override
			public int compare(Vector3D a, Vector3D b)
			{
				return (a.x * a.x + a.y * a.y + a.z * a.z) - (b.x * b.x + b.y * b.y + b.z * b.z);
			}
		});
		VOLUME = pos.toArray(new Vector3D[0]);
	}

	public static Location getTarget(final LivingEntity entity) throws Exception
	{
		final Block block = entity.getTargetBlock(TRANSPARENT_MATERIALS, 300);
		if (block == null)
		{
			throw new Exception("Not targeting a block");
		}
		return block.getLocation();
	}

	static boolean isBlockAboveAir(final World world, final int x, final int y, final int z)
	{
		if (y > world.getMaxHeight())
		{
			return true;
		}
		return HOLLOW_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType());
	}

	public static boolean isBlockUnsafeForUser(final IUser user, final World world, final int x, final int y, final int z)
	{
		if (user.getBase().isOnline() && world.equals(user.getBase().getWorld())
			&& (user.getBase().getGameMode() == GameMode.CREATIVE || user.isGodModeEnabled())
			&& user.getBase().getAllowFlight())
		{
			return false;
		}

		if (isBlockDamaging(world, x, y, z))
		{
			return true;
		}
		return isBlockAboveAir(world, x, y, z);
	}

	public static boolean isBlockUnsafe(final World world, final int x, final int y, final int z)
	{
		if (isBlockDamaging(world, x, y, z))
		{
			return true;
		}
		return isBlockAboveAir(world, x, y, z);
	}

	public static boolean isBlockDamaging(final World world, final int x, final int y, final int z)
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
		if (below.getType() == Material.BED_BLOCK)
		{
			return true;
		}
		if (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y, z).getType()) || !HOLLOW_MATERIALS.contains(world.getBlockAt(x, y + 1, z).getType()))
		{
			return true;
		}
		return false;
	}

	// Not needed if using getSafeDestination(loc)
	public static Location getRoundedDestination(final Location loc)
	{
		final World world = loc.getWorld();
		int x = loc.getBlockX();
		int y = (int)Math.round(loc.getY());
		int z = loc.getBlockZ();
		return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
	}

	public static Location getSafeDestination(final IUser user, final Location loc) throws Exception
	{
		if (user.getBase().isOnline() && loc.getWorld().equals(user.getBase().getWorld())
			&& (user.getBase().getGameMode() == GameMode.CREATIVE || user.isGodModeEnabled())
			&& user.getBase().getAllowFlight())
		{
			if (shouldFly(loc))
			{
				user.getBase().setFlying(true);
			}
			return getRoundedDestination(loc);
		}
		return getSafeDestination(loc);
	}

	public static Location getSafeDestination(final Location loc) throws Exception
	{
		if (loc == null || loc.getWorld() == null)
		{
			throw new Exception(tl("destinationNotSet"));
		}
		final World world = loc.getWorld();
		int x = loc.getBlockX();
		int y = (int)Math.round(loc.getY());
		int z = loc.getBlockZ();
		final int origX = x;
		final int origY = y;
		final int origZ = z;
		while (isBlockAboveAir(world, x, y, z))
		{
			y -= 1;
			if (y < 0)
			{
				y = origY;
				break;
			}
		}
		if (isBlockUnsafe(world, x, y, z))
		{
			x = Math.round(loc.getX()) == origX ? x - 1 : x + 1;
			z = Math.round(loc.getZ()) == origZ ? z - 1 : z + 1;
		}
		int i = 0;
		while (isBlockUnsafe(world, x, y, z))
		{
			i++;
			if (i >= VOLUME.length)
			{
				x = origX;
				y = origY + RADIUS;
				z = origZ;
				break;
			}
			x = origX + VOLUME[i].x;
			y = origY + VOLUME[i].y;
			z = origZ + VOLUME[i].z;
		}
		while (isBlockUnsafe(world, x, y, z))
		{
			y += 1;
			if (y >= world.getMaxHeight())
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
				if (x - 48 > loc.getBlockX())
				{
					throw new Exception(tl("holeInFloor"));
				}
			}
		}
		return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
	}

	public static boolean shouldFly(Location loc)
	{
		final World world = loc.getWorld();
		final int x = loc.getBlockX();
		int y = (int)Math.round(loc.getY());
		final int z = loc.getBlockZ();
		int count = 0;
		while (LocationUtil.isBlockUnsafe(world, x, y, z) && y > -1)
		{
			y--;
			count++;
			if (count > 2)
			{
				return true;
			}
		}

		return y < 0 ? true : false;
	}
}
