package net.ess3.update.tasks;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import net.ess3.update.AbstractWorkListener;
import net.ess3.update.GetFile;
import net.ess3.update.ModuleInfo;
import net.ess3.update.VersionInfo;
import org.bukkit.Bukkit;


public class InstallModule implements Runnable, Task
{
	protected final transient AbstractWorkListener listener;
	private final transient String moduleName;
	private final transient String fileName;

	public InstallModule(final AbstractWorkListener listener, final String moduleName)
	{
		this(listener, moduleName, moduleName + ".jar");
	}

	public InstallModule(final AbstractWorkListener listener, final String moduleName, final String fileName)
	{
		this.listener = listener;
		this.moduleName = moduleName;
		this.fileName = fileName;
	}

	@Override
	public void start()
	{
		Bukkit.getScheduler().scheduleAsyncDelayedTask(listener.getPlugin(), this); //Should this be async? (method deprecated)
	}

	@Override
	public void run()
	{
		final VersionInfo info = listener.getNewVersionInfo();
		final ModuleInfo module = info.getModules().get(moduleName);
		if (module == null)
		{
			listener.onWorkAbort("Module " + moduleName + " not found in VersionInfo.");
			return;
		}
		try
		{
			final URL downloadUrl = module.getUrl();
			final GetFile getFile = new GetFile(downloadUrl);
			getFile.saveTo(new File(listener.getPlugin().getServer().getUpdateFolderFile(), fileName), module.getHash());
			listener.onWorkDone("Module " + moduleName + " downloaded.");
		}
		catch (Exception ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Failed to download module " + moduleName + " to " + fileName, ex);
			listener.onWorkAbort("An error occured, please check your server log.");
		}
	}
}
