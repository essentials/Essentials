package net.ess3.permissions;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TexturedMaterial;
import org.bukkit.permissions.PermissionDefault;


public class MaterialDotStarPermission extends DotStarPermission
{
	public MaterialDotStarPermission(final String base)
	{
		super(base);
	}

	public MaterialDotStarPermission(final String base, final PermissionDefault defaultPerm)
	{
		super(base, defaultPerm);
	}

	public boolean isAuthorized(final CommandSender sender, final ItemStack item)
	{
		return isAuthorized(sender, item.getType(), item.getData());
	}

	public boolean isAuthorized(final CommandSender sender, final Block block)
	{
		final Material mat = block.getType();
		return isAuthorized(sender, mat, mat.getData() == null ? null : mat.getNewData(block.getData()));
	}

	public boolean isAuthorized(final CommandSender sender, final Material material, final MaterialData data)
	{
		final String materialName = material.name();
		final String materialId = String.valueOf(material.getId());
		if (data != null)
		{
			final String durName = materialName + ":" + data.getData();
			final String durId = materialId + ":" + data.getData();
			if (data instanceof Colorable)
			{
				return super.isAuthorized(sender, materialName + ":" + ((Colorable)data).getColor().name(), durName, durId, materialName, materialId);
			}
			if (data instanceof TexturedMaterial)
			{
				return super.isAuthorized(sender, materialName + ":" + ((TexturedMaterial)data).getMaterial().name(), durName, durId, materialName, materialId);
			}
			return super.isAuthorized(sender, durName, durId, materialName, materialId);
		}
		return super.isAuthorized(sender, materialName, materialId);
	}
}
