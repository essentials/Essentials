package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.backup.IBackupComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import org.bukkit.command.CommandSender;


public class Commandbackup extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		final IBackupComponent backup = getContext().getBackup();
		backup.run();
		sender.sendMessage($("backupStarted"));
	}
}
