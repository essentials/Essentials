package net.ess3.settings.commands;

import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Lightning implements StorageObject
{
	@Comment("Shall we notify users when using /lightning")
	private boolean warnPlayer = true;
}
