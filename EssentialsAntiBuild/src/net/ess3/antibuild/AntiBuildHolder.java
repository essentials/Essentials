/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ess3.antibuild;

import java.io.File;
import java.io.IOException;
import net.ess3.api.IEssentials;
import net.ess3.settings.antibuild.AntiBuild;
import net.ess3.storage.AsyncStorageObjectHolder;


/**
 *
 * @author devhome
 */
public class AntiBuildHolder extends AsyncStorageObjectHolder<AntiBuild>
{
	public AntiBuildHolder(final IEssentials ess)
	{
		super(ess, AntiBuild.class);
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(ess.getPlugin().getDataFolder(), "protect.yml");
	}

	@Override
	public void finishRead()
	{
	}

	@Override
	public void finishWrite()
	{
	}
}
