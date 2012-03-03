package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.users.IUserComponent;
import java.util.Locale;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandeco extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		EcoCommands cmd;
		double amount;
		try
		{
			cmd = EcoCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH));
			amount = Double.parseDouble(args[2].replaceAll("[^0-9\\.]", ""));
		}
		catch (Exception ex)
		{
			throw new NotEnoughArgumentsException(ex);
		}

		if (args[1].contentEquals("*"))
		{
			for (Player onlinePlayer : getServer().getOnlinePlayers())
			{
				final IUserComponent player = getContext().getUser(onlinePlayer);
				switch (cmd)
				{
				case GIVE:
					player.giveMoney(amount);
					break;

				case TAKE:
					player.takeMoney(amount);
					break;

				case RESET:
					@Cleanup
					ISettingsComponent settings = getContext().getSettings();
					settings.acquireReadLock();
					player.setMoney(amount == 0 ? settings.getData().getEconomy().getStartingBalance() : amount);
					break;
				}
			}
		}
		else
		{
			final IUserComponent player = getPlayer(args, 1, true);
			switch (cmd)
			{
			case GIVE:
				player.giveMoney(amount, sender);
				break;

			case TAKE:
				player.takeMoney(amount, sender);
				break;

			case RESET:
				@Cleanup ISettingsComponent settings = getContext().getSettings();
				settings.acquireReadLock();
				player.setMoney(amount == 0 ? settings.getData().getEconomy().getStartingBalance() : amount);
				break;
			}
		}
	}


	private enum EcoCommands
	{
		GIVE, TAKE, RESET
	}
}
