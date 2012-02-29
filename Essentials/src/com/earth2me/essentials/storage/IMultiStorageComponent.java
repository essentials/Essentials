package com.earth2me.essentials.storage;

import com.earth2me.essentials.components.IComponent;
import java.io.File;
import org.bukkit.plugin.Plugin;


public interface IMultiStorageComponent<T extends IStorageObject, U extends Plugin> extends IComponent
{
	String getContainerId();

	boolean isPersistent(final String name);

	<V extends ISubStorageComponent<T, U>> V loadFile(final V component, final File file);
}
