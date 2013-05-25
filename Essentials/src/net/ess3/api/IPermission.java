package net.ess3.api;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;


public interface IPermission
{
	String getPermissionName();

	/**
	 * Checks to see if a user can use this permission
	 *
	 * @param sender - CommandSender to check on
	 * @return - True if they have that permission
	 */
	boolean isAuthorized(CommandSender sender);

	/**
	 *
	 * @return
	 */
	String getParentPermission();

	/**
	 *
	 * @return
	 */
	PermissionDefault getPermissionDefault();
}
