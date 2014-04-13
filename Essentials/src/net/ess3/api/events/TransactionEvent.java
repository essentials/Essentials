package net.ess3.api.events;

import com.earth2me.essentials.Trade;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class TransactionEvent extends Event implements Cancellable
{

	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();
	private final IUser sender;
	private final Trade charge;
	private final IUser receiver;
	private final Trade pay;
	private final Location loc;
	private final IEssentials ess;

	public TransactionEvent( IUser sender, Trade charge, IUser receiver, Trade pay, Location loc, IEssentials ess )
	{
		super();
		this.sender = sender;
		this.charge = charge;
		this.receiver = receiver;
		this.pay = pay;
		this.loc = loc;
		this.ess = ess;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public IUser getSender()
	{
		return sender;
	}

	public Trade getCharge()
	{
		return charge;
	}

	public IUser getReceiver()
	{
		return receiver;
	}

	public Trade getPay()
	{
		return pay;
	}

	public Location getLoc()
	{
		return loc;
	}

	public IEssentials getEss()
	{
		return ess;
	}

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled( boolean cancelled )
	{
		this.cancelled = cancelled;
	}
}
- 
