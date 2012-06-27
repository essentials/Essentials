package net.ess3.api;

import java.util.Collection;
import org.bukkit.Location;


public interface IJails extends IReload
{
	Location getJail(String jailName) throws Exception;

	Collection<String> getList() throws Exception;

	int getCount();

	void removeJail(String jail) throws Exception;

	void sendToJail(IUser user, String jail) throws Exception;

	void setJail(String jailName, Location loc) throws Exception;
}
