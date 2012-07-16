package net.ess3.craftbukkit;


public class SetExpFix
{
	public static void setTotalExperience(final IPlayer player, final int exp)
	{
		if (exp < 0)
		{
			throw new IllegalArgumentException("Experience is negative!");
		}
		player.setExp(0);
		player.setLevel(0);
		player.setTotalExperience(0);
		int amount = exp;
		while (amount > 0)
		{
			final int expToLevel = getExpToLevel(player);
			amount -= expToLevel;
			if (amount >= 0)
			{
				// give until next level
				player.giveExp(expToLevel);
			}
			else
			{
				// give the rest
				amount += expToLevel;
				player.giveExp(amount);
				amount = 0;
			}
		}
	}

	private static int getExpToLevel(final IPlayer player)
	{		
		return getExpToLevel(player.getLevel());
	}
	
	private static int getExpToLevel(final int level)
	{		
		return 7 + (level * 7 >> 1);
	}
	
	public static int getTotalExperience(final IPlayer player)
	{
		int exp = (int) (getExpToLevel(player) * player.getExp());
		int currentLevel = player.getLevel();
		
		while (currentLevel > 0) {			
			currentLevel--;
			exp += getExpToLevel(currentLevel);
		}
		return exp;
	}
}
