package net.ess3.settings;

import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorldOptions implements StorageObject
{
	@Comment("Disables godmode for all players if they teleport to this world.")
	private boolean godmode = true;
}
