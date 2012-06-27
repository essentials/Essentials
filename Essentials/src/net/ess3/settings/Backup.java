package net.ess3.settings;

import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Backup implements StorageObject
{
	@Comment("Interval in minutes")
	private long interval = 60;
	@Comment("Add a command that backups your data, e.g. 'rdiff-backup World1 backups/World1'")
	private String command;
}
