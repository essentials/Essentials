package net.ess3.commands;

import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import static net.ess3.I18n._;

import java.util.Locale;
import lombok.Cleanup;


public class Commandeco extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
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

		if (args[1].contentEquals("**"))
		{
			for (String sUser : ess.getUserMap().getAllUniqueUsers())
			{
				final IUser player = ess.getUser(sUser);
				switch (cmd)
				{
				case GIVE:
					player.giveMoney(amount);
					break;

				case TAKE:
					if (player.canAfford(amount, false))
					{
						player.takeMoney(amount);
					}					
					break;

				case RESET:
					@Cleanup 
					ISettings settings = ess.getSettings();
					settings.acquireReadLock();
					player.setMoney(amount == 0 ? settings.getData().getEconomy().getStartingBalance() : amount);
					break;
				}
			}
		}
		else if (args[1].contentEquals("*"))
		{
			for (Player onlinePlayer : server.getOnlinePlayers())
			{
				final IUser player = onlinePlayer.getUser();
				switch (cmd)
				{
				case GIVE:
					player.giveMoney(amount);
					break;

				case TAKE:
					if (!player.canAfford(amount, false))
					{
						throw new Exception(_("notEnoughMoney"));
					}
					player.takeMoney(amount);
					break;

				case RESET:
					@Cleanup 
					ISettings settings = ess.getSettings();
					settings.acquireReadLock();
					player.setMoney(amount == 0 ? settings.getData().getEconomy().getStartingBalance() : amount);
					break;
				}
			}
		}
		else
		{
			final IUser player = getPlayer(args, 1, true);
			switch (cmd)
			{
			case GIVE:
				player.giveMoney(amount, sender);
				break;

			case TAKE:
				if (!player.canAfford(amount, false))
				{
					throw new Exception(_("notEnoughMoney"));
				}
				player.takeMoney(amount, sender);
				break;

			case RESET:
				@Cleanup ISettings settings = ess.getSettings();
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
