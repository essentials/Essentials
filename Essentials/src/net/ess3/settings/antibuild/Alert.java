package net.ess3.settings.antibuild;

import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> alertOnPlacement;
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> alertOnUse;
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> alertOnBreak;
	
	public Alert()
	{
		//todo full default list
		alertOnPlacement = new HashSet<Material>();
		alertOnPlacement.add(Material.GLASS);
		alertOnUse = new HashSet<Material>();
		alertOnUse.add(Material.LAVA);
		alertOnBreak = new HashSet<Material>();
		alertOnBreak.add(Material.OBSIDIAN);
	}
}