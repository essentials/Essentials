package net.ess3.settings.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;

@Data
@EqualsAndHashCode(callSuper = false)
public class Tempban implements StorageObject
{
	@Comment({
			"Set to the maximum time in seconds a player can be tempbanned for.",
			"Set to -1 to disable, and override with essentials.tempban.unlimited"
	})
	private long maxTempbanTime = -1;
}
