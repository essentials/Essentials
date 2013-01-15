package net.ess3.api;


public interface IComponent extends IReload
{
	/**
	 * Enable the component.
	 */
	void onEnable();

	/**
	 * Disable the component.
	 */
	void onDisable();
}
