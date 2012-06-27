package net.ess3.signs;

import net.ess3.api.IEssentialsModule;


public interface ISignsPlugin extends IEssentialsModule
{
	SignsConfigHolder getSettings();
}
