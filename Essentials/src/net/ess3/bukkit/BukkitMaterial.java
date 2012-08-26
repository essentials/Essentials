package net.ess3.bukkit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class BukkitMaterial
{
	public static ItemStack convertBlockToItem(Material mat, short data)
	{
		final ItemStack is = new ItemStack(mat, 1, data);
		switch (is.getType())
		{
		case WOODEN_DOOR:
			is.setType(org.bukkit.Material.WOOD_DOOR);
			is.setDurability((short)0);
			break;
		case IRON_DOOR_BLOCK:
			is.setType(org.bukkit.Material.IRON_DOOR);
			is.setDurability((short)0);
			break;
		case SIGN_POST:
		case WALL_SIGN:
			is.setType(org.bukkit.Material.SIGN);
			is.setDurability((short)0);
			break;
		case CROPS:
			is.setType(org.bukkit.Material.SEEDS);
			is.setDurability((short)0);
			break;
		case CAKE_BLOCK:
			is.setType(org.bukkit.Material.CAKE);
			is.setDurability((short)0);
			break;
		case BED_BLOCK:
			is.setType(org.bukkit.Material.BED);
			is.setDurability((short)0);
			break;
		case REDSTONE_WIRE:
			is.setType(org.bukkit.Material.REDSTONE);
			is.setDurability((short)0);
			break;
		case REDSTONE_TORCH_OFF:
		case REDSTONE_TORCH_ON:
			is.setType(org.bukkit.Material.REDSTONE_TORCH_ON);
			is.setDurability((short)0);
			break;
		case DIODE_BLOCK_OFF:
		case DIODE_BLOCK_ON:
			is.setType(org.bukkit.Material.DIODE);
			is.setDurability((short)0);
			break;
		case DOUBLE_STEP:
			is.setType(org.bukkit.Material.STEP);
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
			is.setType(org.bukkit.Material.PUMPKIN_SEEDS);
			break;
		case MELON_STEM:
			is.setType(org.bukkit.Material.MELON_SEEDS);
			break;
		}
		return is;
	}
}
