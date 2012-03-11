package com.earth2me.essentials.anticheat.checks.chat;

import java.util.Locale;
import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.data.Statistics.Id;


public class ColorCheck extends ChatCheck
{
	public ColorCheck(NoCheat plugin)
	{
		super(plugin, "chat.color");
	}

	public boolean check(NoCheatPlayer player, ChatData data, ChatConfig cc)
	{

		if (data.message.contains("\247"))
		{

			data.colorVL += 1;
			incrementStatistics(player, Id.CHAT_COLOR, 1);

			boolean filter = executeActions(player, cc.colorActions, data.colorVL);

			if (filter)
			{
				// Remove color codes
				data.message = data.message.replaceAll("\302\247.", "").replaceAll("\247.", "");
			}
		}

		return false;
	}

	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", getData(player).colorVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
