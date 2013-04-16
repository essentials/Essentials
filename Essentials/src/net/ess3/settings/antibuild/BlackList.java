package net.ess3.settings.antibuild;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import net.ess3.storage.Comment;
import net.ess3.storage.ListType;
import net.ess3.storage.StorageObject;
import org.bukkit.Material;


@Data
@EqualsAndHashCode(callSuper = false)
public class BlackList implements StorageObject
{
	@Comment("Which blocks should people be prevented from placing")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> placement = new HashSet<Material>();
	@Comment("Which items should people be prevented from using")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> usage = new HashSet<Material>();
	@Comment("Which blocks should people be prevented from breaking")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> breaking = new HashSet<Material>();
	@Comment("Which blocks should not be pushed by pistons")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> piston = new HashSet<Material>();
	@Comment("Which blocks should not be dispensed by dispensers")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> dispenser = new HashSet<Material>();

	public void setupDefaults()
	{
		Material[] mat =
		{
			Material.LAVA, Material.STATIONARY_LAVA, Material.TNT, Material.LAVA_BUCKET
		};
		placement.addAll(Arrays.asList(mat));
	}

	public boolean getPlacement(Material mat)
	{
		return placement != null && placement.contains(mat);
	}

	public boolean getUsage(Material mat)
	{
		return usage != null && usage.contains(mat);
	}

	public boolean getBreaking(Material mat)
	{
		return breaking != null && breaking.contains(mat);
	}

	public boolean getPiston(Material mat)
	{
		return piston != null && piston.contains(mat);
	}
	
	public boolean getDispenser(Material mat)
	{
		return dispenser != null && dispenser.contains(mat);
	}
}
