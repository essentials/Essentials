package com.earth2me.essentials.anticheat;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class NoCheatLogEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private String message;
	private String prefix;
	private boolean toConsole, toChat, toFile;

	public NoCheatLogEvent(String prefix, String message, boolean toConsole, boolean toChat, boolean toFile)
	{
		this.prefix = prefix;
		this.message = message;
		this.toConsole = toConsole;
		this.toChat = toChat;
		this.toFile = toFile;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public boolean toFile()
	{
		return toFile;
	}

	public void setToFile(boolean toFile)
	{
		this.toFile = toFile;
	}

	public boolean toChat()
	{
		return toChat;
	}

	public void setToChat(boolean toChat)
	{
		this.toChat = toChat;
	}

	public boolean toConsole()
	{
		return toConsole;
	}

	public void setToConsole(boolean toConsole)
	{
		this.toConsole = toConsole;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
