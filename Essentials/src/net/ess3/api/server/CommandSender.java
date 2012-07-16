package net.ess3.api.server;

public interface CommandSender {
	boolean isPlayer();

	void sendMessage(String message);

	boolean isOp();

	boolean hasPermission(Permission bukkitPermission);

	void sendMessage(String[] string);
}
