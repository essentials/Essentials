package net.ess3.bukkit.events;

import net.ess3.api.ondemand.UserOnDemand;
import net.ess3.api.server.Block;
import net.ess3.api.server.Server;
import net.ess3.api.server.events.EventListener;
import net.ess3.bukkit.BukkitBlockFactory;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class PlaceBlockExecutor extends BaseEventExecutor
{
	private final Server server;
	public PlaceBlockExecutor(EventListener listener, Server server)
	{
		super(listener);
		this.server = server;
	}

	@Override
	public void execute(Listener ll, Event event) throws EventException
	{
		org.bukkit.block.Block bukkitBlock = ((BlockPlaceEvent)event).getBlockPlaced();
		Block block = BukkitBlockFactory.convert(bukkitBlock);
		String playername = ((BlockPlaceEvent)event).getPlayer().getName();
		listener.onBlockPlace(block, new UserOnDemand(playername, server));
	}
}
