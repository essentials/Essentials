package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponent;


public interface IBackupComponent extends Runnable, IComponent
{
	public void startTask();
}
