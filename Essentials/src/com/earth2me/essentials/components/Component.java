package com.earth2me.essentials.components;

import com.earth2me.essentials.api.IContext;


/**
 * An abstract component implementation that can be added to Essentials.
 */
public class Component implements IComponent
{
	private final transient IContext context;

	/**
	 * Instantiate a new Component with the specified parent.
	 *
	 * @param context The instance of Essentials hosting the component.
	 */
	protected Component(IContext context)
	{
		this.context = context;
	}
	
	/**
	 * Gets a unique ID for this type of component.
	 * 
	 * @return The simple name of the bottom-most class in the inheritance tree.
	 */
	@Override
	public String getTypeId()
	{
		return getClass().getSimpleName();
	}

	/**
	 * Initialize the component.
	 */
	@Override
	public void initialize()
	{
	}

	/**
	 * Reload the component.
	 */
	@Override
	public void reload()
	{
	}
	
	/**
	 * Enable the component.
	 */
	@Override
	public void onEnable()
	{
	}

	/**
	 * Release all resources held by the component.
	 */
	@Override
	public void close()
	{
	}

	protected IContext getContext()
	{
		return context;
	}
}
