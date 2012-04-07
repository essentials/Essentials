package com.earth2me.essentials.bukkit;

import com.earth2me.essentials.api.server.Material;
import java.util.EnumMap;
import lombok.Delegate;


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
}
