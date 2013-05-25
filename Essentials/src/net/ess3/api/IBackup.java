package net.ess3.api;


public interface IBackup extends Runnable
{
	/**
	 * Used to start the backup task
	 */
	void startTask();
}
