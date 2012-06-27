package net.ess3.api.server;

public interface IPermissionsManager {
	
	IPermission registerPermission();
	
	boolean checkPermission(IPlayer player, IPermission perm);	
}
