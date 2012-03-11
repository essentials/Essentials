package com.earth2me.essentials.anticheat.actions.types;

import com.earth2me.essentials.anticheat.actions.Action;
import java.util.*;


/**
 * A list of actions, that associates actions to tresholds. It allows to retrieve all actions that match a certain
 * treshold
 *
 */
public class ActionList
{
	// This is a very bad design decision, but it's also really
	// convenient to define this here
	public final String permissionSilent;

	public ActionList(String permission)
	{
		this.permissionSilent = permission + ".silent";
	}
	// If there are no actions registered, we still return an Array. It's
	// just empty/size=0
	private final static Action[] emptyArray = new Action[0];
	// The actions of this ActionList, "bundled" by treshold (violation level)
	private final Map<Integer, Action[]> actions = new HashMap<Integer, Action[]>();
	// The tresholds of this list
	private final List<Integer> tresholds = new ArrayList<Integer>();

	/**
	 * Add an entry to this actionList. The list will be sorted by tresholds automatically after the insertion.
	 *
	 * @param treshold The minimum violation level a player needs to have to be suspected to the given actions
	 * @param actions The actions that will be used if the player reached the accompanying treshold/violation level
	 */
	public void setActions(Integer treshold, Action[] actions)
	{

		if (!this.tresholds.contains(treshold))
		{
			this.tresholds.add(treshold);
			Collections.sort(this.tresholds);
		}

		this.actions.put(treshold, actions);
	}

	/**
	 * Get a list of actions that match the violation level. The only method that has to be called by a check
	 *
	 * @param violationLevel The violation level that should be matched.
	 * @return The array of actions whose treshold was closest to the violationLevel but not bigger
	 */
	public Action[] getActions(double violationLevel)
	{

		Integer result = null;

		for (Integer treshold : tresholds)
		{
			if (treshold <= violationLevel)
			{
				result = treshold;
			}
		}

		if (result != null)
		{
			return actions.get(result);
		}
		else
		{
			return emptyArray;
		}
	}

	/**
	 * Get a sorted list of the tresholds/violation levels that were used in this list
	 *
	 * @return The sorted list of tresholds
	 */
	public List<Integer> getTresholds()
	{
		return tresholds;
	}
}
