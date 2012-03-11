package com.earth2me.essentials.anticheat.checks.chat;

import com.earth2me.essentials.anticheat.DataItem;


/**
 * Player specific data for the chat checks
 *
 */
public class ChatData implements DataItem
{
	// Keep track of the violation levels for the two checks
	public int spamVL;
	public int colorVL;
	// Count messages and commands
	public int messageCount = 0;
	public int commandCount = 0;
	// Remember when the last check time period started
	public long spamLastTime = 0;
	// Remember the last chat message or command for logging purposes
	public String message = "";
}
