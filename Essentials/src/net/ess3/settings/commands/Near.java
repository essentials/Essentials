package net.ess3.settings.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Near implements StorageObject
{
	@Comment("Radius of the near command, if no number is given.")
	private int defaultRadius = 200;
}
