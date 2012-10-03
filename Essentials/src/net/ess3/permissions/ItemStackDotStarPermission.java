package net.ess3.permissions;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TexturedMaterial;
import org.bukkit.permissions.PermissionDefault;

public class ItemStackDotStarPermission extends DotStarPermission {

	public ItemStackDotStarPermission(String base)
	{
		super(base);
	}

	public ItemStackDotStarPermission(String base, PermissionDefault defaultPerm)
	{
		super(base, defaultPerm);
	}

	public boolean isAuthorized(CommandSender sender, ItemStack item)
	{
		MaterialData data;
		Material material = item.getType();
		String materialName = material.name();
		String materialId = String.valueOf(material.getId());
		if (material.getMaxDurability() == 0 && (data = item.getData()) != null) {
			String durName = materialName + ":" + item.getDurability();
			String durId = materialId + ":" + item.getDurability();
			if (data instanceof Colorable) {
				return super.isAuthorized(sender, materialName+":"+((Colorable)data).getColor().name(), durName, durId, materialName, materialId);
			}
			if (data instanceof TexturedMaterial) {
				return super.isAuthorized(sender, materialName+":"+((TexturedMaterial)data).getMaterial().name(), durName, durId, materialName, materialId);
			}
			return super.isAuthorized(sender, durName, durId, materialName, materialId);
		}
		return super.isAuthorized(sender, materialName, materialId);
	}
	
	
}
