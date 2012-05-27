package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import java.util.ArrayList;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
public class Commandfreeze extends EssentialsCommand
{
	private boolean frozen;

	public Commandfreeze()
	{
		super("freeze");
	}
	
	public static ArrayList<String> toFreeze = new ArrayList<String>();
	
	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{

		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final User player = getPlayer(server, args, 0, true);
		if (!player.isFrozen() && player.isAuthorized("essentials.freeze.exempt"))
		{
			throw new Exception(_("freezeExempt"));
		}
		long freezeTimestamp = 0;
		if (args.length > 1)
		{
			String time = getFinalArg(args, 1);
			freezeTimestamp = Util.parseDateDiff(time, true);
		}
		player.setFreezeTimeout(freezeTimestamp);
		final boolean frozen = player.toggleFrozen();
		sender.sendMessage(
				frozen
				? (freezeTimestamp > 0
				   ? _("frozePlayerFor", player.getDisplayName(), Util.formatDateDiff(freezeTimestamp))
				   : _("frozePlayer", player.getDisplayName()))
				: _("unfrozePlayer", player.getDisplayName()));
		player.sendMessage(
				frozen
				? (freezeTimestamp > 0
				   ? _("playerFrozeFor", Util.formatDateDiff(freezeTimestamp))
				   : _("playerFrozen"))
				: _("playerUnfrozen"));
	}
}