package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandext extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		extinguishPlayers(sender, args[0]);
	}

	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			user.setFireTicks(0);
			user.sendMessage($("extinguish"));
			return;
		}

		extinguishPlayers(user, args[0]);
	}

	private void extinguishPlayers(final CommandSender sender, final String name) throws Exception
	{
		for (Player matchPlayer : getServer().matchPlayer(name))
		{
			matchPlayer.setFireTicks(0);
			sender.sendMessage($("extinguishOthers", matchPlayer.getDisplayName()));
		}
	}
}
