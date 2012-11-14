package net.ess3.signs;

import net.ess3.signs.signs.SignBalance;
import net.ess3.signs.signs.SignBuy;
import net.ess3.signs.signs.SignDisposal;
import net.ess3.signs.signs.SignEnchant;
import net.ess3.signs.signs.SignFeed;
import net.ess3.signs.signs.SignFree;
import net.ess3.signs.signs.SignGameMode;
import net.ess3.signs.signs.SignHeal;
import net.ess3.signs.signs.SignInfo;
import net.ess3.signs.signs.SignKit;
import net.ess3.signs.signs.SignMail;
import net.ess3.signs.signs.SignRepair;
import net.ess3.signs.signs.SignSell;
import net.ess3.signs.signs.SignSpawnmob;
import net.ess3.signs.signs.SignTime;
import net.ess3.signs.signs.SignTrade;
import net.ess3.signs.signs.SignWarp;
import net.ess3.signs.signs.SignWeather;


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
