package net.ess3.update.states;

import java.util.LinkedHashMap;


public class StateMap extends LinkedHashMap<Class<? extends AbstractState>, AbstractState>
{
	public StateMap()
	{
		super(50);
	}
}
