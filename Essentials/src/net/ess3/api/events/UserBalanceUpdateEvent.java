package net.ess3.api.events;

import java.math.BigDecimal;

import com.earth2me.essentials.IUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class UserBalanceUpdateEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private final IUser user;
	private BigDecimal newBalance;

	public UserBalanceUpdateEvent(IUser user, BigDecimal newBalance)
	{
		this.user = user;
		this.newBalance = newBalance;
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

	public Player getPlayer()
	{
		return user.getBase();
	}

	public IUser getUser() {
		return user;
	}

	public BigDecimal getNewBalance()
	{
		return newBalance;
	}

	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}

	public BigDecimal getOldBalance()
	{
		return user.getMoney();
	}

	public BigDecimal getChange() {
		return newBalance.subtract(getOldBalance());
	}
}
