package net.ess3.api;

import java.util.Collection;
import net.ess3.commands.NoChargeException;
import net.ess3.settings.Kit;


public interface IKits extends IReload
{
	/**
	 *
	 * @param kit
	 * @return
	 * @throws Exception
	 */
	Kit getKit(String kit) throws Exception;

	/**
	 *
	 * @param user
	 * @param kit
	 * @throws Exception
	 */
	void sendKit(IUser user, String kit) throws Exception;

	/**
	 *
	 * @param user
	 * @param kit
	 * @throws Exception
	 */
	void sendKit(IUser user, Kit kit) throws Exception;

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	Collection<String> getList() throws Exception;

	/**
	 *
	 * @return
	 */
	boolean isEmpty();

	/**
	 *
	 * @param user
	 * @param kit
	 * @throws NoChargeException
	 */
	void checkTime(final IUser user, Kit kit) throws NoChargeException;
}
