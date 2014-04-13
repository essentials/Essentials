package net.ess3.api.events;

import com.earth2me.essentials.Trade;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;


public class SignTransactionEvent extends TransactionEvent
{


	private static final HandlerList handlers = new HandlerList();


	// if /pay, sender and receiver not null.
	// if /sell, sender null, receiver not null

	/**
	 *
	 * @param sender Sender of the transaction. Null if not a user. (sell signs)
	 * @param charge The charge of the transaction.
	 * @param receiver Receiver of the transaction. Null if not a user. (buy signs)
	 * @param pay The received pay in the transaction.
	 * @param loc Location of the sender. If sender is null, location of receiver.
	 * @param ess IEssentials
	 */
	public SignTransactionEvent( IUser sender, Trade charge, IUser receiver, Trade pay, Location loc, IEssentials ess )
	{
		super(sender, charge, receiver, pay, loc, ess);
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
