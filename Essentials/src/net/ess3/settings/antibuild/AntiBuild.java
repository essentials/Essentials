package net.ess3.settings.antibuild;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.*;


@Data
@EqualsAndHashCode(callSuper = false)
public class AntiBuild implements StorageObject
{
	@Comment(
	{
		"Should people with build: false in permissions be allowed to build",
		"Set true to disable building for those people",
		"Setting to false means EssentialsAntiBuild will never prevent you from building"
	})
	private boolean build = true;
	@Comment(
	{
		"Should people with build: false in permissions be allowed to use items",
		"Set true to disable using for those people",
		"Setting to false means EssentialsAntiBuild will never prevent you from using"
	})
	private boolean use = true;
	@Comment(
	{
		"Should we tell people they are not allowed to build"
	})
	private boolean warnOnBuildDisallow = true;
	Alert alert = new Alert();
	BlackList blacklist = new BlackList();
}
