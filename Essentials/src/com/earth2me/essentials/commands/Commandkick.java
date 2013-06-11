package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.logging.Level;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandkick extends EssentialsCommand
{
	public Commandkick()
	{
		super("kick");
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final User target = getPlayer(server, args, 0, true, false);
		if (sender instanceof Player)
		{
			User user = ess.getUser(sender);
			if (target.isHidden() && !user.isAuthorized("essentials.vanish.interact"))
			{
				throw new PlayerNotFoundException();
			}

			if (target.isAuthorized("essentials.kick.exempt"))
			{
				throw new Exception(_("kickExempt"));
			}
		}

		String kickReason = args.length > 1 ? getFinalArg(args, 1) : _("kickDefault");
		kickReason = FormatUtil.replaceFormat(kickReason.replace("\\n", "\n").replace("|", "\n"));

		target.kickPlayer(kickReason);
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;

		server.getLogger().log(Level.INFO, _("playerKicked", senderName, target.getName(), kickReason));
		ess.broadcastMessage(sender, "essentials.kick.notify", _("playerKicked", senderName, target.getName(), kickReason));
	}
}
