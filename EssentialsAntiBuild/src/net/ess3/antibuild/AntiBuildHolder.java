package net.ess3.antibuild;

import java.io.File;
import net.ess3.api.IEssentials;
import net.ess3.settings.antibuild.Alert;
import net.ess3.settings.antibuild.AntiBuild;
import net.ess3.settings.antibuild.BlackList;
import net.ess3.storage.AsyncStorageObjectHolder;


public class AntiBuildHolder extends AsyncStorageObjectHolder<AntiBuild>
{
	public AntiBuildHolder(final IEssentials ess)
	{
		super(ess, AntiBuild.class, new File(ess.getPlugin().getDataFolder(), "antibuild.yml"));
		onReload();
	}

	@Override
	public void fillWithDefaults()
	{
		Alert alert = new Alert();
		BlackList blacklist = new BlackList();
		blacklist.setupDefaults();
		alert.setupDefaults();
		getData().setAlert(alert);
		getData().setBlacklist(blacklist);
	}
}
