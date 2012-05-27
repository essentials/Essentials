package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import static com.earth2me.essentials.I18n._;
import java.util.ArrayList;
import org.bukkit.Server;
import org.bukkit.entity.Player;
public class Commandfreeze extends EssentialsCommand
{
	public Commandfreeze()
	{
		super("freeze");
	}
	
	
	
	@Override
	public void run (final Server server, final User user, final String commandLabel, final String[] args) throws Exception
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
			String time getFinalArg(args, 1);
			freezeTimestamp = Util.parseDateDiff(time, true);
		}
		player.setFreezeTimeout(freezeTimestamp);
		final boolean muted = player.toggleFreeze();

	}
}
