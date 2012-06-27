package net.ess3.bukkit;

import net.ess3.api.server.IServer;
import lombok.Delegate;

public class Server implements IServer {
	@Delegate
	private org.bukkit.Server server;
}
