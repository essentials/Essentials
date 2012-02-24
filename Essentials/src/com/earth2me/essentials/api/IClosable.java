package com.earth2me.essentials.api;


/**
 * A type that must explicitly free resources.
 */
public interface IClosable
{
	/**
	 * Explicitly free resources held by the object.
	 */
	void close();
}
