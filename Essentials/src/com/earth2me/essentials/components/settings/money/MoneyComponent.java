package com.earth2me.essentials.components.settings.money;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.StorageComponent;


public class MoneyComponent extends StorageComponent<Money>
{
	public MoneyComponent(IContext context)
	{
		super(context, Money.class);
	}

	@Override
	public String getContainerId()
	{
		return "bank-accounts";
	}
}
