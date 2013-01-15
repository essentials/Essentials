package net.ess3.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Backup implements StorageObject
{
	@Comment("Interval in minutes")
	private long interval = 60;
	@Comment("Add a command that backups your data, e.g. 'rdiff-backup World1 backups/World1'")
	private String command;
	@Comment(
	{
		"Runs these commands before a backup.", "This will run every time time (in minutes) you specify in the interval setting.",
		"ex: say \"Hello World\" will make the server say Hello World"
	})
	private List<String> commandsBeforeBackup;
	@Comment(
	{
		"Runs these commands after a backup.", "This will run every time time (in minutes) you specify in the interval setting.",
		"ex: say \"Hello World\" will make the server say Hello World"
	})
	private List<String> commandsAfterBackup;

	public Backup()
	{
		commandsBeforeBackup = new ArrayList<String>();
		commandsBeforeBackup.add("save-all");
		commandsBeforeBackup.add("save-off");
		commandsAfterBackup = new ArrayList<String>();
		commandsAfterBackup.add("save-on");
	}

	public List<String> getCommandsBeforeBackup()
	{
		return commandsBeforeBackup == null ? Collections.<String>emptyList() : Collections.unmodifiableList(commandsBeforeBackup);
	}

	public List<String> getCommandsAfterBackup()
	{
		return commandsAfterBackup == null ? Collections.<String>emptyList() : Collections.unmodifiableList(commandsAfterBackup);
	}
}
