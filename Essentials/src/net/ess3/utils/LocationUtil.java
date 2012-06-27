package net.ess3.utils;

import static net.ess3.I18n._;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class LocationUtil {
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
}
