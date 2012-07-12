package net.ess3.commands;

import java.io.IOException;
import java.util.Map;
import static net.ess3.I18n._;
import net.ess3.metrics.Metrics;
import org.bukkit.command.CommandSender;


public class Commandessentials extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length == 0)
		{
			run_disabled(sender, commandLabel, args);
		}
		else if (args[0].equalsIgnoreCase("debug"))
		{
			run_debug(sender, commandLabel, args);
		}
		else if (args[0].equalsIgnoreCase("opt-out"))
		{
			run_optout(sender, commandLabel, args);
		}
		else
		{
			run_reload(sender, commandLabel, args);
		}
	}

	private void run_disabled(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage("Essentials " + ess.getDescription().getVersion());
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

		if (disabledCommands.length() > 0)
		{
			sender.sendMessage(_("blockList"));
			sender.sendMessage(disabledCommands.toString());
		}
	}

	private void run_debug(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		ess.getSettings().setDebug(!ess.getSettings().isDebug());
		sender.sendMessage("Essentials " + ess.getDescription().getVersion() + " debug mode " + (ess.getSettings().isDebug() ? "enabled" : "disabled"));
	}

	private void run_reload(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		ess.reload();
		sender.sendMessage(_("essentialsReload", ess.getDescription().getVersion()));
	}

	private void run_optout(final CommandSender sender, final String command, final String args[])
	{
		final Metrics metrics = ess.getMetrics();
		try
		{
			sender.sendMessage("Essentials collects simple metrics to highlight which features to concentrate work on in the future.");
			if (metrics.isOptOut())
			{
				metrics.enable();
			}
			else
			{
				metrics.disable();
			}
			sender.sendMessage("Anonymous Metrics are now: " + (metrics.isOptOut() ? "disabled" : "enabled"));
		}
		catch (IOException ex)
		{
			sender.sendMessage("Unable to modify 'plugins/PluginMetrics/config.yml': " + ex.getMessage());
		}
	}
}