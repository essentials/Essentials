package com.earth2me.essentials;

import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.users.User;
import com.earth2me.essentials.craftbukkit.DummyOfflinePlayer;
import java.io.IOException;
import junit.framework.TestCase;
import org.bukkit.World.Environment;
import org.bukkit.plugin.InvalidDescriptionException;


public class UserTest extends TestCase
{
	private final IUser base1;
	private final Essentials essentials;
	private final FakeServer server;

	public UserTest(String testName)
	{
		super(testName);
		essentials = new Essentials();
		server = new FakeServer();
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
		base1 = new User(new DummyOfflinePlayer("testPlayer1"), essentials.getContext());
		server.addPlayer(base1);
		essentials.getContext().getUser(base1);
	}

	private void should(String what)
	{
		System.out.println(getName() + " should " + what);
	}

	/*public void testUpdate()
	{
		OfflinePlayer base1alt = server.createPlayer(base1.getName(), ess);
		assertEquals(base1alt, ess.getUser(base1alt).getBase());
	}

	public void testHome()
	{
		IUser user = ess.getUser(base1);
		Location loc = base1.getLocation();
		user.setHome();
		OfflinePlayer base2 = server.createPlayer(base1.getName(), ess);
		IUser user2 = ess.getUser(base2);

		Location home = user2.getHome(loc);
		assertNotNull(home);
		assertEquals(loc.getWorld().getName(), home.getWorld().getName());
		assertEquals(loc.getX(), home.getX());
		assertEquals(loc.getY(), home.getY());
		assertEquals(loc.getZ(), home.getZ());
		assertEquals(loc.getYaw(), home.getYaw());
		assertEquals(loc.getPitch(), home.getPitch());
	}*/

	/*public void testMoney()
	{
		should("properly set, take, give, and get money");
		IUser user = ess.getUser(base1);
		double i;
		user.setMoney(i = 100.5);
		user.takeMoney(50);
		i -= 50;
		user.giveMoney(25);
		i += 25;
		assertEquals(user.getMoney(), i);
	}*/

	/*public void testGetGroup()
	{
		should("return the default group");
		IUser user = ess.getUser(base1);
		//assertEquals(user.getGroup(), "default");
	}*/

	public void testNoop()
	{
		assertTrue(true);
	}
}
