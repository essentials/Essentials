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
public class Alert implements StorageObject
{
	@Comment("For which block types would you like to be alerted when placed?")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> alertOnPlacement = null;
	@Comment("For which block types would you like to be alerted when used?")	
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> alertOnUse = null;
	@Comment("For which block types would you like to be alerted when broken?")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> alertOnBreak = null;
	public Alert()
	{
		if (alertOnPlacement == null)
		{
			Material[] mat =
			{
				Material.LAVA, Material.STATIONARY_LAVA, Material.TNT, Material.LAVA_BUCKET
			};
			alertOnPlacement = new HashSet<Material>();
			alertOnPlacement.addAll(Arrays.asList(mat));
		}
		
		if (alertOnUse == null)
		{			
			alertOnUse = new HashSet<Material>();
			alertOnUse.add(Material.LAVA_BUCKET);
		}
		
		if (alertOnBreak == null)
		{
			alertOnBreak = new HashSet<Material>();
		}
	}
	
	public Set<Material> getAlertOnPlacement()
	{
		return alertOnPlacement;
	}
	
	public Set<Material> getAlertOnUse()
	{
		return alertOnUse;
	}
	
	public Set<Material> getAlertOnBreak()
	{
		return alertOnBreak;
	}
}