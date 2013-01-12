package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IBackup;
import org.bukkit.command.CommandSender;


public class Commandbackup extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		final IBackup backup = ess.getBackup();
		backup.run();
		sender.sendMessage(_("backupStarted"));
	}
}
