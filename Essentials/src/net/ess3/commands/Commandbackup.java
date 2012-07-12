package net.ess3.commands;

import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.IBackup;
import net.ess3.api.ISettings;
import org.bukkit.command.CommandSender;


public class Commandbackup extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		final IBackup backup = ess.getBackup();
		if (backup == null)
		{
			throw new Exception(_("backupDisabled"));
		}
		
		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		final String command = settings.getData().getGeneral().getBackup().getCommand();
		if (command == null || "".equals(command) || "save-all".equalsIgnoreCase(command))
		{
			throw new Exception(_("backupDisabled"));
		}
		backup.run();
		sender.sendMessage(_("backupStarted"));
	}
}
