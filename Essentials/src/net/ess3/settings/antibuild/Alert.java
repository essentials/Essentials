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
public class Alert implements StorageObject
{
	@Comment("For which block types would you like to be alerted when placed?")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> alertOnPlacement = new HashSet<Material>();
	@Comment("For which block types would you like to be alerted when used?")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> alertOnUse = new HashSet<Material>();
	@Comment("For which block types would you like to be alerted when broken?")
	@ListType(Material.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Set<Material> alertOnBreak = new HashSet<Material>();

	public void setupDefaults()
	{
		Material[] mat =
		{
			Material.LAVA, Material.STATIONARY_LAVA, Material.TNT, Material.LAVA_BUCKET
		};
		alertOnPlacement.addAll(Arrays.asList(mat));
		alertOnUse.add(Material.LAVA_BUCKET);
	}

	public boolean getAlertOnPlacementContains(Material mat)
	{
		return alertOnPlacement != null && alertOnPlacement.contains(mat);
	}

	public boolean getAlertOnUseContains(Material mat)
	{
		return alertOnUse != null && alertOnUse.contains(mat);
	}

	public boolean getAlertOnBreakContains(Material mat)
	{
		return alertOnBreak != null && alertOnBreak.contains(mat);
	}
}
