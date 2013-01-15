package net.ess3.commands;

import java.util.Map;
import static net.ess3.I18n._;
import org.bukkit.command.CommandSender;


public class Commandessentials extends EssentialsCommand
{
	private int taskid; // TODO: Needed?

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length == 0)
		{
			run_disabled(sender, args);
		}
		else if (args[0].equalsIgnoreCase("debug"))
		{
			run_debug(sender, args);
		}
		else
		{
			run_reload(sender, args);
		}
	}

	private void run_disabled(final CommandSender sender, final String[] args) throws Exception
	{
		sender.sendMessage("Essentials " + ess.getPlugin().getVersion());
		sender.sendMessage("/<command> <reload/debug>");
		sender.sendMessage(_("blockList"));
		final StringBuilder disabledCommands = new StringBuilder();
		for (Map.Entry<String, String> entry : ess.getCommandHandler().disabledCommands().entrySet())
		{
			if (disabledCommands.length() > 0)
			{
				disabledCommands.append(", ");
			}
			disabledCommands.append(entry.getKey()).append(" => ").append(entry.getValue());
		}
		sender.sendMessage(disabledCommands.toString());
	}

	private void run_debug(final CommandSender sender, final String[] args) throws Exception
	{
		ess.getSettings().setDebug(!ess.getSettings().isDebug());
		sender.sendMessage(_("debugToggle", ess.getPlugin().getVersion(), _(ess.getSettings().isDebug() ? "enabled" : "disabled")));
	}

	private void run_reload(final CommandSender sender, final String[] args) throws Exception
	{
		ess.onReload();
		sender.sendMessage(_("essentialsReload", ess.getPlugin().getVersion()));
	}
}
