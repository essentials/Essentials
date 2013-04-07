package net.ess3.settings.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class List implements StorageObject
{
	@Comment("Sort output of /list command by groups")
	private boolean sortByGroups = true;
}
