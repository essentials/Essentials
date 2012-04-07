package com.earth2me.essentials.api.server;


public abstract class Permission
{
	public enum Default
	{
		TRUE, FALSE, OP, NOT_OP;
	}


	public interface PermissionFactory
	{
		Permission create(String name, Permission.Default defaultPermission);
	}
	private static PermissionFactory factory;

	public static void setFactory(final PermissionFactory factory)
	{
		Permission.factory = factory;
	}

	public static Permission create(final String name, Permission.Default defaultPermission)
	{
		return factory.create(name, defaultPermission);
	}
}
