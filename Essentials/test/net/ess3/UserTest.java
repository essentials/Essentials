package net.ess3;

import net.ess3.api.IUser;


public class UserTest extends EssentialsTest
{
	private final IUser base1;

	public UserTest(String testName)
	{
		super(testName);

		addPlayer("testPlayer1");
		base1 = ess.getUserMap().getUser("testPlayer1");
	}

	private void should(String what)
	{
		System.out.println(getName() + " should " + what);
	}

	/*public void testUpdate()
	{
		OfflinePlayer base1alt = server.createPlayer(base1.getName(), ess);
		assertEquals(base1alt, ess.getUserMap().getUser(base1alt).getBase());
	}

	public void testHome()
	{
		IUser user = ess.getUserMap().getUser(base1);
		Location loc = base1.getLocation();
		user.setHome();
		OfflinePlayer base2 = server.createPlayer(base1.getName(), ess);
		IUser user2 = ess.getUserMap().getUser(base2);

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
		IUser user = ess.getUserMap().getUser(base1);
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
		IUser user = ess.getUserMap().getUser(base1);
		//assertEquals(user.getGroup(), "default");
	}*/

	public void testNoop()
	{
		assertTrue(true);
	}
}
