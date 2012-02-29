package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import org.bukkit.inventory.ItemStack;


public class Commandsetworth extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		ItemStack stack = getContext().getItems().get(args[0]);
		getContext().getWorths().setPrice(stack, Double.parseDouble(args[1]));
		user.sendMessage($("worthSet"));
	}
}
