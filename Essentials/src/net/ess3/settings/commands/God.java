package net.ess3.settings.commands;

import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class God implements StorageObject
{
	@Comment("Turn off god mode when people exit")
	private boolean removeOnDisconnect = false;
}
