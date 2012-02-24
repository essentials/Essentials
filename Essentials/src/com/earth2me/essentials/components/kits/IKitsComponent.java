package com.earth2me.essentials.components.kits;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.settings.Kit;
import java.util.Collection;


public interface IKitsComponent extends IComponent
{
	Kit getKit(String kit) throws Exception;

	void sendKit(IUser user, String kit) throws Exception;

	void sendKit(IUser user, Kit kit) throws Exception;

	Collection<String> getList() throws Exception;

	boolean isEmpty();
}
