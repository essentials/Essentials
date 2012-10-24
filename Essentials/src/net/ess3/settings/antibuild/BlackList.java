package net.ess3.settings.antibuild;

import java.util.Arrays;
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
public class BlackList implements StorageObject
{
	@Comment(
	{
		"Which blocks should people be prevented from placing"
	})
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> placement = new HashSet<Material>();
	@Comment(
	{
		"Which items should people be prevented from using"
	})
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> usage = new HashSet<Material>();
	@Comment(
	{
		"Which blocks should people be prevented from breaking"
	})
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> breaking = new HashSet<Material>();
	@Comment(
	{
		"Which blocks should not be pushed by pistons"
	})
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> piston = new HashSet<Material>();
	
	public BlackList()
	{
		if(placement.isEmpty())
		{
			Material[] mat =
			{
				Material.LAVA, Material.STATIONARY_LAVA, Material.TNT, Material.LAVA_BUCKET
			};
			
			placement.addAll(Arrays.asList(mat));
		}
		
		
		if (usage.isEmpty())
		{
			usage.add(Material.LAVA_BUCKET);
		}			
	}
	
	public boolean getPlacement(Material mat)
	{
		if(placement == null)
		{
		return false;
		}
		return placement.contains(mat);
	}
	
	public boolean getUsage(Material mat)
	{
		if(usage == null)
		{
			return false;
		}
		return usage.contains(mat);
	}
	
	public boolean getBreaking(Material mat)
	{
		if(breaking == null)
		{
			return false;
		}
		return breaking.contains(mat);
	}
	
	public boolean getPiston(Material mat)
	{
		if(piston == null)
		{
			return false;
		}
		return piston.contains(mat);
	}
}
