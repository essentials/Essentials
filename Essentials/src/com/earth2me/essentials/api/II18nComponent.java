package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponent;
import java.util.Locale;


public interface II18nComponent extends IComponent
{
	Locale getCurrentLocale();
	
	void updateLocale(final String loc);
	
	void onDisable();
}
