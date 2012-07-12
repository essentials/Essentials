package net.ess3.settings.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Spawnmob implements StorageObject
{
	@Comment("The maximum amount of monsters, a player can spawn with a call of /spawnmob.")
	private int limit = 10;
}
