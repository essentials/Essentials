package com.earth2me.essentials.api;

import com.earth2me.essentials.IUser;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;


public interface IJails extends IReload
{
	Location getJail(String jailName) throws Exception;

	Collection<String> getList() throws Exception;

	int getCount();

	void removeJail(String jail) throws Exception;

	void sendToJail(com.earth2me.essentials.IUser user, String jail) throws Exception;

	void setJail(String jailName, Location loc) throws Exception;
	
	void jailtimeSelf(final com.earth2me.essentials.IUser player);
	
	void jailtimeOther(final CommandSender sender, final IUser player);
	
	void checkAutoBan(final CommandSender sender, final IUser player);
	
	void jailhistory(final CommandSender sender, final IUser player);
		
}
