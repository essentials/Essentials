package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
			Boolean ecoResetAll = false;
			ISettings settings = ess.getSettings();
			for (String sUser : ess.getUserMap().getAllUniqueUsers())
			{
				final IUser player = ess.getUserMap().getUser(sUser);
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
					player.setMoney(amount == 0 ? settings.getData().getEconomy().getStartingBalance() : amount);
					ecoResetAll = true;
					break;
				}
			}
			if (ecoResetAll == true)
			{
				ess.broadcastMessage(null, _("ecoResetAll", settings.getData().getEconomy().getCurrencySymbol() + amount));
			}


		}
		else if (args[1].contentEquals("*"))
		{
			Boolean ecoResetAllOnline = false;
			ISettings settings = ess.getSettings();
			for (Player onlinePlayer : server.getOnlinePlayers())
			{
				final IUser player = ess.getUserMap().getUser(onlinePlayer);
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
					player.setMoney(amount == 0 ? settings.getData().getEconomy().getStartingBalance() : amount);
					ecoResetAllOnline = true;
					break;
				}
			}
			if (ecoResetAllOnline == true)
			{
				ess.broadcastMessage(null, _("ecoResetAllOnline", settings.getData().getEconomy().getCurrencySymbol() + amount));
			}
		}
		else
		{
			final IUser player = ess.getUserMap().matchUser(args[1], true);
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
				ISettings settings = ess.getSettings();
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
