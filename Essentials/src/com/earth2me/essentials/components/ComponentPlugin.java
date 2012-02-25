package com.earth2me.essentials.components;

import com.earth2me.essentials.api.IReloadable;
import java.util.*;
import org.bukkit.plugin.java.JavaPlugin;


public abstract class ComponentPlugin extends JavaPlugin implements IComponentPlugin
{
	private transient final List<IComponent> components = new ArrayList<IComponent>();
	private transient volatile boolean initialized;

	/**
	 * Reload all components.
	 */
	@Override
	public void reload()
	{
		for (IReloadable reloadable : this)
		{
			reloadable.reload();
		}
	}

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

		for (IComponent component : components)
		{
			component.initialize();
		}
	}

	@Override
	public void onDisable()
	{
		clear();
	}

	@Override
	public final int size()
	{
		return components.size();
	}

	@Override
	public final boolean isEmpty()
	{
		return components.isEmpty();
	}

	@Override
	public final boolean contains(final Object o)
	{
		return components.contains(o);
	}

	@Override
	public final Iterator<IComponent> iterator()
	{
		return components.iterator();
	}

	@Override
	public final Object[] toArray()
	{
		return components.toArray();
	}

	@Override
	public final <T> T[] toArray(final T[] ts)
	{
		return components.toArray(ts);
	}

	@Override
	public boolean add(final IComponent e)
	{
		if (components.add(e))
		{
			e.onEnable();
			if (initialized)
			{
				e.initialize();
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean remove(final Object o)
	{
		return components.remove(o);
	}

	@Override
	public boolean remove(final IComponent component)
	{
		if (remove(component))
		{
			component.close();
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public final boolean containsAll(final Collection<?> clctn)
	{
		return components.containsAll(clctn);
	}

	@Override
	public final boolean addAll(final Collection<? extends IComponent> clctn)
	{
		return components.addAll(clctn);
	}

	@Override
	public final boolean addAll(final int i, final Collection<? extends IComponent> clctn)
	{
		return components.addAll(i, clctn);
	}

	@Override
	public final boolean removeAll(final Collection<?> clctn)
	{
		return components.removeAll(clctn);
	}

	@Override
	public final boolean retainAll(final Collection<?> clctn)
	{
		return components.retainAll(clctn);
	}

	@Override
	public void clear()
	{
		Throwable ex = null;
		for (IComponent component : components)
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

		components.clear();

		if (ex != null)
		{
			throw new Error(ex);
		}
	}

	@Override
	public IComponent get(final int i)
	{
		return components.get(i);
	}

	@Override
	public final IComponent set(final int i, final IComponent e)
	{
		return components.set(i, e);
	}

	@Override
	public final void add(final int i, final IComponent e)
	{
		components.add(i, e);
	}

	@Override
	public final IComponent remove(final int i)
	{
		return components.remove(i);
	}

	@Override
	public final int indexOf(final Object o)
	{
		return components.indexOf(o);
	}

	@Override
	public final int lastIndexOf(final Object o)
	{
		return components.lastIndexOf(o);
	}

	@Override
	public final ListIterator<IComponent> listIterator()
	{
		return components.listIterator();
	}

	@Override
	public final ListIterator<IComponent> listIterator(final int i)
	{
		return components.listIterator(i);
	}

	@Override
	public final List<IComponent> subList(final int i, final int i1)
	{
		return components.subList(i, i1);
	}
}
