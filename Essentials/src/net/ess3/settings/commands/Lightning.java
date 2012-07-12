package net.ess3.settings.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Lightning implements StorageObject
{
	@Comment("Shall we notify users when using /lightning")
	private boolean warnPlayer = true;
}
