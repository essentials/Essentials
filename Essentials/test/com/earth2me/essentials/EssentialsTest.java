package com.earth2me.essentials;

import com.earth2me.essentials.testserver.Plugin;
import com.earth2me.essentials.testserver.Server;
import java.util.logging.Logger;
import junit.framework.TestCase;
import net.ess3.Essentials;

public abstract class EssentialsTest extends TestCase {
	protected final transient Server server;
	protected final transient Plugin plugin;
	protected final transient Logger logger;
	protected final transient Essentials ess;
	

	public EssentialsTest(final String testName)
	{
		super(testName);
		logger = Logger.getLogger(this.getName());
		server = new Server();
		plugin = new Plugin();
		ess = new Essentials(server, logger, plugin);
		Essentials.testing = true;
		ess.onEnable();
	}
	
}
