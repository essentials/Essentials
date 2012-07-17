package net.ess3.settings.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class God implements StorageObject
{
	@Comment("Turn off god mode when people exit")
	private boolean removeOnDisconnect = false;
}
