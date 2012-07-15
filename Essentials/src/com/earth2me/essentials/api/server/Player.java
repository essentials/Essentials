package com.earth2me.essentials.api.server;

import com.earth2me.essentials.api.IUser;


public interface Player extends CommandSender
{
	IUser getUser();

	String getName();

	String getDisplayName();

	boolean isOnline();

	void setBanned(boolean bool);

	void kickPlayer(String reason);

	World getWorld();

	Location getLocation();

	Location getEyeLocation();

	void setFireTicks(int value);

	void setFoodLevel(int value);

	void setSaturation(float value);

	int getHealth();
	
	void setHealth(int value);

	void updateInventory();

	ItemStack getItemInHand();
	
	Location getBedSpawnLocation();
	
	boolean hasPlayedBefore();
	
	IInventory getInventory();
	
	void setTotalExperience(final int exp);
	
	int getTotalExperience();
	
	void setDisplayName(String name);
	
	void setPlayerListName(String name);
	
	void setSleepingIgnored(boolean b);
	
	boolean isBanned();
	
	void setCompassTarget(Location loc);
	
	void damage(int value);
}
