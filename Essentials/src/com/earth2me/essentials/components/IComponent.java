package com.earth2me.essentials.components;

import com.earth2me.essentials.api.IClosable;
import com.earth2me.essentials.api.IReloadable;


/**
 * Represents a primary building block of Essentials.
 */
public interface IComponent extends IReloadable, IClosable
{
	/**
	 * Gets the name of the component.
	 * 
	 * @return A descriptive name identifying the component.
	 */
	String getTypeId();
	
	/**
	 * Initialize the component after all components have been instantiated.
	 */
	void initialize();
	
	/**
	 * Enable the component, usually right after instantiating it.
	 */
	void onEnable();
}
