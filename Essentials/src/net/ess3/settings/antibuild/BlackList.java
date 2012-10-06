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
		//todo defaults
	}
}
