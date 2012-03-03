package com.earth2me.essentials.components;

public interface IComponentList extends Iterable<IComponent>
{
	
	/**
	 * Initialize all components.
	 */
	void initialize() throws IllegalStateException;

	boolean add(final IComponent component);
	
	boolean remove(final IComponent component);
	
	void clear();
}
