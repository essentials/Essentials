package net.ess3.bukkit;

import java.util.EnumMap;
import lombok.Delegate;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.Material;


public class BukkitMaterial extends Material
{
	private static EnumMap<org.bukkit.Material, BukkitMaterial> materials = new EnumMap<org.bukkit.Material, BukkitMaterial>(org.bukkit.Material.class);

	static
	{
		for (org.bukkit.Material material : org.bukkit.Material.values())
		{
			materials.put(material, new BukkitMaterial(material));
		}
	}

	@Override
	public int getId()
	{
		return this.material.getId();
	}

	@Override
	public int getMaxStackSize()
	{
		return this.material.getMaxStackSize();
	}

	private interface Excludes {
		short getMaxDurability();
	}
	@Delegate(excludes={Excludes.class})
	private final org.bukkit.Material material;

	private BukkitMaterial(final org.bukkit.Material material)
	{
		this.material = material;
	}

	@Override
	protected Material getMaterialByName(final String name)
	{
		final org.bukkit.Material mat = org.bukkit.Material.getMaterial(name);
		return materials.get(mat);
	}

	@Override
	protected Material getMaterialById(final int id)
	{
		final org.bukkit.Material mat = org.bukkit.Material.getMaterial(id);
		return materials.get(mat);
	}

	@Override
	protected Material matchMaterial(String string)
	{
		final org.bukkit.Material mat = org.bukkit.Material.matchMaterial(string);
		return materials.get(mat);
	}

	@Override
	public String getName()
	{
		return this.material.toString();
	}

	@Override
	public int getMaxDurability()
	{
		return (short)this.material.getMaxDurability();
	}
	
	@Override
	public ItemStack convertToItem(ItemStack is)
	{
		switch (org.bukkit.Material.getMaterial(is.getType().getId()))
		{
		case WOODEN_DOOR:
			is.setType(org.bukkit.Material.WOOD_DOOR.getId());
			is.setDurability((short)0);
			break;
		case IRON_DOOR_BLOCK:
			is.setType(org.bukkit.Material.IRON_DOOR.getId());
			is.setDurability((short)0);
			break;
		case SIGN_POST:
		case WALL_SIGN:
			is.setType(org.bukkit.Material.SIGN.getId());
			is.setDurability((short)0);
			break;
		case CROPS:
			is.setType(org.bukkit.Material.SEEDS.getId());
			is.setDurability((short)0);
			break;
		case CAKE_BLOCK:
			is.setType(org.bukkit.Material.CAKE.getId());
			is.setDurability((short)0);
			break;
		case BED_BLOCK:
			is.setType(org.bukkit.Material.BED.getId());
			is.setDurability((short)0);
			break;
		case REDSTONE_WIRE:
			is.setType(org.bukkit.Material.REDSTONE.getId());
			is.setDurability((short)0);
			break;
		case REDSTONE_TORCH_OFF:
		case REDSTONE_TORCH_ON:
			is.setType(org.bukkit.Material.REDSTONE_TORCH_ON.getId());
			is.setDurability((short)0);
			break;
		case DIODE_BLOCK_OFF:
		case DIODE_BLOCK_ON:
			is.setType(org.bukkit.Material.DIODE.getId());
			is.setDurability((short)0);
			break;
		case DOUBLE_STEP:
			is.setType(org.bukkit.Material.STEP.getId());
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
			is.setType(org.bukkit.Material.PUMPKIN_SEEDS.getId());
			break;
		case MELON_STEM:
			is.setType(org.bukkit.Material.MELON_SEEDS.getId());
			break;
		}
		return is;
	}
}
