package com.earth2me.essentials.commands;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.CommandSource;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import java.util.Locale;
import org.bukkit.Server;


public class Commandeco extends EssentialsLoopCommand
{
	Commandeco.EcoCommands cmd;
	BigDecimal amount;

	public Commandeco()
	{
		super("eco");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		BigDecimal startingBalance = ess.getSettings().getStartingBalance();

		try
		{
			cmd = Commandeco.EcoCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH));
			amount = (cmd == Commandeco.EcoCommands.RESET) ? startingBalance : new BigDecimal(args[2].replaceAll("[^0-9\\.]", ""));
		}
		catch (Exception ex)
		{
			throw new NotEnoughArgumentsException(ex);
		}

		loopOfflinePlayers(server, sender, false, args[1], args);

		if (cmd == Commandeco.EcoCommands.RESET || cmd == Commandeco.EcoCommands.SET)
		{
			if (args[1].contentEquals("**"))
			{
				server.broadcastMessage(_("resetBalAll", NumberUtil.displayCurrency(amount, ess)));
			}
			else if (args[1].contentEquals("*"))
			{
				server.broadcastMessage(_("resetBal", NumberUtil.displayCurrency(amount, ess)));
			}
		}
	}

	@Override
	protected void updatePlayer(final Server server, final CommandSource sender, final User player, final String[] args) throws NotEnoughArgumentsException, ChargeException
	{
		switch (cmd)
		{
		case GIVE:
			player.giveMoney(amount, sender);
			break;

		case TAKE:
			take(amount, player, sender);
			break;

		case RESET:
		case SET:
			set(amount, player, sender);
			break;
		}
	}

	private void take(BigDecimal amount, final User player, final CommandSource sender) throws ChargeException
	{
		BigDecimal money = player.getMoney();
		BigDecimal minBalance = ess.getSettings().getMinMoney();
		if (money.subtract(amount).compareTo(minBalance) > 0)
		{
			player.takeMoney(amount, sender);
		}
		else if (sender == null)
		{
			player.setMoney(minBalance);
			player.sendMessage(_("takenFromAccount", NumberUtil.displayCurrency(player.getMoney(), ess)));
		}
		else
		{
			throw new ChargeException(_("insufficientFunds"));
		}
	}

	private void set(BigDecimal amount, final User player, final CommandSource sender)
	{
		BigDecimal minBalance = ess.getSettings().getMinMoney();
		boolean underMinimum = (amount.compareTo(minBalance) < 0);
		player.setMoney(underMinimum ? minBalance : amount);
		player.sendMessage(_("setBal", NumberUtil.displayCurrency(player.getMoney(), ess)));
		if (sender != null)
		{
			sender.sendMessage(_("setBalOthers", player.getDisplayName(), NumberUtil.displayCurrency(player.getMoney(), ess)));
		}
	}


	private enum EcoCommands
	{
		GIVE, TAKE, SET, RESET
	}
}