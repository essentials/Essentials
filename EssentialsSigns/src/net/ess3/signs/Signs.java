package net.ess3.signs;

import net.ess3.signs.signs.*;


public enum Signs
{
	BALANCE(new SignBalance()),
	BUY(new SignBuy()),
	DISPOSAL(new SignDisposal()),
	ENCHANT(new SignEnchant()),
	FEED(new SignFeed()),
	FREE(new SignFree()),
	GAMEMODE(new SignGameMode()),
	HEAL(new SignHeal()),
	INFO(new SignInfo()),
	KIT(new SignKit()),
	MAIL(new SignMail()),
	REPAIR(new SignRepair()),
	SELL(new SignSell()),
	SPAWNMOB(new SignSpawnmob()),
	TIME(new SignTime()),
	TRADE(new SignTrade()),
	WARP(new SignWarp()),
	WEATHER(new SignWeather());
	private final EssentialsSign sign;

	private Signs(final EssentialsSign sign)
	{
		this.sign = sign;
	}

	public EssentialsSign getSign()
	{
		return sign;
	}
}
