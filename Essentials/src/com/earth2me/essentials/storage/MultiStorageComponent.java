package com.earth2me.essentials.storage;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.Component;
import java.io.File;
import java.util.Locale;
import org.bukkit.plugin.Plugin;


public abstract class MultiStorageComponent<T extends IStorageObject, U extends Plugin> extends Component implements IMultiStorageComponent<T, U>
{

	protected MultiStorageComponent(final IContext context)
	{
		super(context);
	}

	protected File ensureFolder()
	{
		File folder = new File(getContainerId());
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		{
			if (folder.exists())
			{
				if (folder.isDirectory())
				{
					return folder;
				}
				else
				{
					folder = new File(getContainerId() + i);
				}
			}
			else if (!folder.mkdirs())
			{
				// This should never happen.
				throw new Error("Some sort of weird file access error occurred while attempting to create a directory.  This should not have happened.");
			}
		}
		throw new Error("How the heck did you make that many files?!");
	}

	private String getFileName(final String name)
	{
		return (name.toLowerCase(Locale.ENGLISH) + ".yml").intern();
	}

	protected File getFile(final String name)
	{
		return new File(ensureFolder(), getFileName(name));
	}

	@Override
	public boolean isPersistent(final String name)
	{
		final File file = getFile(name);
		return file.exists() && file.canRead();
	}

	@Override
	public <V extends ISubStorageComponent<T, U>> V loadFile(final V component, final File file)
	{
		if (component == null)
		{
			return null;
		}

		component.close();
		component.setStorageFile(file);
		return component;
	}
}
