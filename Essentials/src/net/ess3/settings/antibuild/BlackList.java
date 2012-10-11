package net.ess3.settings.antibuild;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.ess3.settings.WorldOptions;
import net.ess3.storage.Comment;
import net.ess3.storage.ListType;
import net.ess3.storage.StorageObject;
import org.bukkit.Material;


@Data
@EqualsAndHashCode(callSuper = false)
public class BlackList implements StorageObject
{
	@Comment(
	{
		"Which blocks should people be prevented from placing"
	})
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> placement;
	@Comment(
	{
		"Which items should people be prevented from using"
	})
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> usage;
	@Comment(
	{
		"Which blocks should people be prevented from breaking"
	})
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> breaking;
	@Comment(
	{
		"Which blocks should not be pushed by pistons"
	})
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> piston;
	
	public BlackList()
	{
		if (placement == null)
		{
			Material[] mat =
			{
				Material.LAVA, Material.STATIONARY_LAVA, Material.TNT, Material.LAVA_BUCKET
			};
			placement = new HashSet<Material>();
			placement.addAll(Arrays.asList(mat));
		}
		
		if (usage == null)
		{
			usage = new HashSet<Material>();
			usage.add(Material.LAVA_BUCKET);
		}
		
		if (breaking == null)
		{
			breaking = new HashSet<Material>();
		}
		
		if (piston == null)
		{
			piston = new HashSet<Material>();
		}
		
		
	}
	
	public Set<Material> getPlacement()
	{
		return placement;
	}
	
	public Set<Material> getUsage()
	{
		return usage;
	}
	
	public Set<Material> getBreaking()
	{
		return breaking;
	}
	
	public Set<Material> getPiston()
	{
		return piston;
	}
}
