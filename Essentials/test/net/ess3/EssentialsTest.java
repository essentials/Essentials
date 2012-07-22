package net.ess3;

import java.util.logging.Logger;
import junit.framework.TestCase;
import net.ess3.testserver.TestPlugin;
import net.ess3.testserver.TestServer;
import net.ess3.testserver.TestWorld;

public abstract class EssentialsTest extends TestCase {
	protected final transient TestServer server;
	protected final transient TestPlugin plugin;
	protected final transient TestWorld world;
	protected final transient Logger logger;
	protected final transient Essentials ess;
	

	public EssentialsTest(final String testName)
	{
		super(testName);
		logger = Logger.getLogger(this.getName());
		server = new TestServer();
		world = (TestWorld)server.getWorlds().get(0);
		plugin = new TestPlugin();
		ess = new Essentials(server, logger, plugin);
		Essentials.testing = true;
		ess.onEnable();
	}
	
}
