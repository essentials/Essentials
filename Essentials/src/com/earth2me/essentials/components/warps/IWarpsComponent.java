package com.earth2me.essentials.components.warps;

import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.IMultiStorageComponent;
import java.util.List;


public interface IWarpsComponent extends IMultiStorageComponent<Warp, IEssentials>
{
	Warp getWarp(final String name);

	void setWarp(final String name, final Warp warp);

	boolean removeWarp(final String name);

	boolean containsWarp(final String name);

	List<String> getList();

	boolean isEmpty();
}
