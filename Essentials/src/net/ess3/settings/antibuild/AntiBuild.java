package net.ess3.settings.antibuild;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class AntiBuild implements StorageObject
{
	@Comment(
	{
		"Should people with build: false in permissions be allowed to build", "Set true to disable building for those people",
		"Setting to false means EssentialsAntiBuild will never prevent you from building"
	})
	private boolean disableBuild = true;
	@Comment(
	{
		"Should people with build: false in permissions be allowed to use items", "Set true to disable using for those people",
		"Setting to false means EssentialsAntiBuild will never prevent you from using"
	})
	private boolean disableUse = true;
	@Comment(
	{
		"Should we tell people they are not allowed to build"
	})
	private boolean warnOnBuildDisallow = true;
	private Alert alert = new Alert();
	private BlackList blacklist = new BlackList();
}
