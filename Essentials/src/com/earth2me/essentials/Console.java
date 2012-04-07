package com.earth2me.essentials;

import com.earth2me.essentials.api.IReplyTo;
import com.earth2me.essentials.api.server.ICommandSender;


public final class Console implements IReplyTo
{
	private static Console instance = new Console();
	private ICommandSender replyTo;
	public final static String NAME = "Console";

	private Console()
	{
	}

	@Override
	public void setReplyTo(final ICommandSender user)
	{
		replyTo = user;
	}

	@Override
	public ICommandSender getReplyTo()
	{
		return replyTo;
	}

	public static Console getConsoleReplyTo()
	{
		return instance;
	}
}
