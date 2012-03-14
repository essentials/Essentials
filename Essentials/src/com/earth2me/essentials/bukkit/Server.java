package com.earth2me.essentials.bukkit;

import com.earth2me.essentials.api.server.IServer;
import lombok.Delegate;

public class Server implements IServer {
	@Delegate
	private org.bukkit.Server server;
}
