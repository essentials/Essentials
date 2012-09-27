package net.ess3.settings.antibuild;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.ListType;
import net.ess3.storage.StorageObject;
import org.bukkit.Material;


@Data
@EqualsAndHashCode(callSuper = false)
public class Alert implements StorageObject
{
	@Comment("For which block types would you like to be alerted?")
	@ListType(Material.class)
	private Set<Material> alertOnPlacement = new HashSet<Material>();
	@ListType(Material.class)
	private Set<Material> alertOnUse = new HashSet<Material>();
	@ListType(Material.class)
	private Set<Material> alertOnBreak = new HashSet<Material>();
	
	public Alert()
	{
		//todo full default list
		alertOnPlacement.add(Material.GLASS);
		alertOnUse.add(Material.LAVA);
		alertOnBreak.add(Material.OBSIDIAN);
	}
}