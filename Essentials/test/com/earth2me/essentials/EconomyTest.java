package com.earth2me.essentials;

import com.earth2me.essentials.components.economy.NoLoanPermittedException;
import com.earth2me.essentials.components.users.UserComponent;
import com.earth2me.essentials.components.users.UserDoesNotExistException;
import com.earth2me.essentials.craftbukkit.DummyOfflinePlayer;
import java.io.IOException;
import junit.framework.TestCase;
import org.bukkit.World.Environment;
import org.bukkit.plugin.InvalidDescriptionException;
import org.junit.Test;


public class EconomyTest extends TestCase
{
	private final transient Essentials essentials;
	private final static String NPCNAME = "npc1";
	private final static String PLAYERNAME = "TestPlayer1";

	public EconomyTest(final String testName)
	{
		super(testName);
		essentials = new Essentials();
		final FakeServer server = new FakeServer();
		server.createWorld("testWorld", Environment.NORMAL);
		try
		{
			essentials.setupForTesting(server);
		}
		catch (InvalidDescriptionException ex)
		{
			fail("InvalidDescriptionException");
		}
		catch (IOException ex)
		{
			fail("IOException");
		}
		server.addPlayer(new UserComponent(new DummyOfflinePlayer(PLAYERNAME), essentials.getContext()));
	}

	// only one big test, since we use static instances
	@Test
	public void testEconomy()
	{
		// test NPC
		assertFalse("NPC does not exists", essentials.getContext().getEconomy().playerExists(NPCNAME));
		assertTrue("Create NPC", essentials.getContext().getEconomy().createNpc(NPCNAME));
		assertTrue("NPC exists", essentials.getContext().getEconomy().playerExists(NPCNAME));
		assertNull("NPC can not be accessed", essentials.getContext().getUser(NPCNAME));
		try
		{
			essentials.getContext().getEconomy().removeNpc(NPCNAME);
		}
		catch (UserDoesNotExistException ex)
		{
			fail(ex.getMessage());
		}
		assertFalse("NPC can be removed",essentials.getContext().getEconomy().playerExists(NPCNAME));

		//test Math
		try
		{

			assertTrue("Player exists", essentials.getContext().getEconomy().playerExists(PLAYERNAME));
			essentials.getContext().getEconomy().resetBalance(PLAYERNAME);
			assertEquals("Player has no money", 0.0, essentials.getContext().getEconomy().getMoney(PLAYERNAME));
			essentials.getContext().getEconomy().setMoney(PLAYERNAME, 10.0);
			assertEquals("Set money", 10.0, essentials.getContext().getEconomy().getMoney(PLAYERNAME));
		}
		catch (NoLoanPermittedException ex)
		{
			fail(ex.getMessage());
		}
		catch (UserDoesNotExistException ex)
		{
			fail(ex.getMessage());
		}

		//test Format
		assertEquals("Format $1000", "$1000", essentials.getContext().getEconomy().format(1000.0));
		assertEquals("Format $10", "$10", essentials.getContext().getEconomy().format(10.0));
		assertEquals("Format $10.10", "$10.10", essentials.getContext().getEconomy().format(10.10));
		assertEquals("Format $10.10", "$10.10", essentials.getContext().getEconomy().format(10.102));
		assertEquals("Format $10.11", "$10.11", essentials.getContext().getEconomy().format(10.109));


		//test Exceptions
		try
		{
			assertTrue("Player exists", essentials.getContext().getEconomy().playerExists(PLAYERNAME));
			essentials.getContext().getEconomy().resetBalance(PLAYERNAME);
			assertEquals("Reset balance", 0.0, essentials.getContext().getEconomy().getMoney(PLAYERNAME));
			essentials.getContext().getEconomy().setMoney(PLAYERNAME, -5.0);
			fail("Did not throw exception");
		}
		catch (NoLoanPermittedException ex)
		{
		}
		catch (UserDoesNotExistException ex)
		{
			fail(ex.getMessage());
		}

		try
		{
			essentials.getContext().getEconomy().resetBalance("UnknownPlayer");
			fail("Did not throw exception");
		}
		catch (NoLoanPermittedException ex)
		{
			fail(ex.getMessage());
		}
		catch (UserDoesNotExistException ex)
		{
		}
	}
}
