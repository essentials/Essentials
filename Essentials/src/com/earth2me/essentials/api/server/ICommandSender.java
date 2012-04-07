package com.earth2me.essentials.api.server;

public interface ICommandSender {
	boolean isPlayer();

	void sendMessage(String message);

	boolean isOp();

	boolean hasPermission(Permission bukkitPermission);

	void sendMessage(String[] string);
}
