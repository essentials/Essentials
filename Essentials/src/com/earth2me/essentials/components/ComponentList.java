package com.earth2me.essentials.components;

import java.util.ArrayList;

public class ComponentList extends ArrayList<IComponent> implements IComponentList
{
	private transient volatile boolean initialized;
	
		/**
	 *Initialize all components.
	 */
	@Override
	public void initialize() throws IllegalStateException
	{
		if (initialized == true)
		{
			throw new IllegalStateException("The components have already been initialized.");
		}
		initialized = true;

		for (IComponent component : this)
		{
			component.initialize();
		}
	}
	
	@Override
	public boolean add(final IComponent component)
	{
		if (super.add(component))
		{
			component.onEnable();
			if (initialized)
			{
				component.initialize();
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public boolean remove(final Object component)
	{
		if (!(component instanceof IComponent)) {
			return false;
		}
		return remove((IComponent)component);
	}
	
	@Override
	public boolean remove(final IComponent component)
	{
		if (!(component instanceof IComponent)) {
			return false;
		}
		if (remove(component))
		{
			((IComponent)component).close();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void clear()
	{
		Throwable ex = null;
		for (IComponent component : this)
		{
			try
			{
				component.close();
			}
			catch (Throwable iterationEx)
			{
				ex = iterationEx;
			}
		}

		super.clear();

		if (ex != null)
		{
			throw new Error(ex);
		}
	}
}
