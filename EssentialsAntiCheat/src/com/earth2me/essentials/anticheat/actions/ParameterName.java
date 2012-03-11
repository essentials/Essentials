package com.earth2me.essentials.anticheat.actions;


/**
 * Some wildcards that are used in commands and log messages
 */
public enum ParameterName
{
	PLAYER("player"), LOCATION("location"), WORLD("world"),
	VIOLATIONS("violations"), MOVEDISTANCE("movedistance"),
	REACHDISTANCE("reachdistance"), FALLDISTANCE("falldistance"),
	LOCATION_TO("locationto"), CHECK("check"), PACKETS("packets"),
	TEXT("text"), PLACE_LOCATION("placelocation"),
	PLACE_AGAINST("placeagainst"), BLOCK_TYPE("blocktype"), LIMIT("limit"),
	FOOD("food"), SERVERS("servers");
	private final String s;

	private ParameterName(String s)
	{
		this.s = s;
	}

	public static ParameterName get(String s)
	{
		for (ParameterName c : ParameterName.values())
		{
			if (c.s.equals(s))
			{
				return c;
			}
		}

		return null;
	}
}
