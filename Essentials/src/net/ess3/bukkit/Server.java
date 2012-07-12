package net.ess3.bukkit;

import lombok.Delegate;
import net.ess3.api.server.IServer;

public class Server implements IServer {
	@Delegate
	private org.bukkit.Server server;
}
