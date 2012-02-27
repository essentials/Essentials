package com.earth2me.essentials.components.settings.warps;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.storage.IStorageObjectHolder;


public interface IWarpsComponent extends IStorageObjectHolder<Warps>, IComponent
{
	Warp getWarp(final String name);

	void setWarp(final String name, final Warp warp);

	Warp removeWarp(final String name);

	boolean containsWarp(final String name);
}
