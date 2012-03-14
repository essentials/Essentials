package com.earth2me.essentials.api.server;

public interface IPermissionsManager {
	
	IPermission registerPermission();
	
	boolean checkPermission(IPlayer player, IPermission perm);	
}
