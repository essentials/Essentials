package com.earth2me.essentials.components.economy;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.components.settings.Money;
import com.earth2me.essentials.storage.StorageComponent;


public class MoneyComponent extends StorageComponent<Money, IEssentials>
{
	public MoneyComponent(IContext context, IEssentials plugin)
	{
		super(context, Money.class, plugin);
	}
}
