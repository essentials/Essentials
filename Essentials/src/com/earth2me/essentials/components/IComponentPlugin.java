/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.earth2me.essentials.components;

import com.earth2me.essentials.api.IReloadable;
import java.util.List;


/**
 *
 * @author paul
 */
public interface IComponentPlugin extends IReloadable, List<IComponent>
{

	boolean add(final IComponent e);

	/**
	 * Initialize all components.
	 */
	void initialize() throws IllegalStateException;

	boolean remove(final IComponent component);

}
