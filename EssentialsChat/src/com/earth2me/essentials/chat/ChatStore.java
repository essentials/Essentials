package com.earth2me.essentials.chat;

import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUser;


public class ChatStore
{
	private final transient IUser user;
	private final transient String type;
	private final transient Trade charge;

	public ChatStore(final IContext ess, final IUser user, final String type)
	{
		this.user = user;
		this.type = type;
		this.charge = new Trade(getLongType(), ess);
	}

	public IUser getUser()
	{
		return user;
	}

	public Trade getCharge()
	{
		return charge;
	}

	public String getType()
	{
		return type;
	}

	public final String getLongType()
	{
		return type.length() == 0 ? "chat" : "chat-" + type;
	}
}
