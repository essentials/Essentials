package net.ess3.settings.protect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Protect implements StorageObject
{
	@Comment("General physics/behavior modifications")
	private Prevent prevent = new Prevent();
	@Comment(
	{
		"Maximum height the creeper should explode. -1 allows them to explode everywhere.",
		"Set prevent.creeper-explosion to true, if you want to disable creeper explosions."
	})
	private int creeperMaxHeight = -1;
	@Comment("Disable weather options")
	private boolean disableStorm = false;
	private boolean disableThunder = false;
	private boolean disableLighting = false;
}
