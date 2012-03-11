package com.earth2me.essentials.anticheat.checks.chat;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;


/**
 * The SpamCheck will count messages and commands over a short timeframe to see if the player tried to send too many of
 * them
 *
 */
public class SpamCheck extends ChatCheck
{
	public SpamCheck(NoCheat plugin)
	{
		super(plugin, "chat.spam");
	}

	public boolean check(NoCheatPlayer player, ChatData data, ChatConfig cc)
	{

		boolean cancel = false;
		// Maybe it's a command and on the whitelist
		for (String s : cc.spamWhitelist)
		{
			if (data.message.startsWith(s))
			{
				// It is
				return false;
			}
		}

		int commandLimit = cc.spamCommandLimit;
		int messageLimit = cc.spamMessageLimit;
		long timeframe = cc.spamTimeframe;

		final long time = System.currentTimeMillis();

		// Has enough time passed? Then reset the counters
		if (data.spamLastTime + timeframe <= time)
		{
			data.spamLastTime = time;
			data.messageCount = 0;
			data.commandCount = 0;
		}
		// Security check, if the system time changes
		else if (data.spamLastTime > time)
		{
			data.spamLastTime = Integer.MIN_VALUE;
		}

		// Increment appropriate counter
		if (data.message.startsWith("/"))
		{
			data.commandCount++;
		}
		else
		{
			data.messageCount++;
		}

		// Did the player go over the limit on at least one of the counters?
		if (data.messageCount > messageLimit || data.commandCount > commandLimit)
		{

			// Set the vl as the number of messages above the limit and
			// increment statistics
			data.spamVL = Math.max(0, data.messageCount - messageLimit);
			data.spamVL += Math.max(0, data.commandCount - commandLimit);
			incrementStatistics(player, Id.CHAT_SPAM, 1);

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancel = executeActions(player, cc.spamActions, data.spamVL);
		}

		return cancel;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", getData(player).spamVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
