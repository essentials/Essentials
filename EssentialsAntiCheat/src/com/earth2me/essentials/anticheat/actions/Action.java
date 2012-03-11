package com.earth2me.essentials.anticheat.actions;


/**
 * An action gets executed as the result of a failed check. If it 'really' gets executed depends on how many executions
 * have occurred within the last 60 seconds and how much time was between this and the previous execution
 *
 */
public abstract class Action
{
	/**
	 * Delay in violations. An "ExecutionHistory" will use this info to make sure that there were at least "delay"
	 * attempts to execute this action before it really gets executed.
	 */
	public final int delay;
	/**
	 * Repeat only every "repeat" seconds. An "ExecutionHistory" will use this info to make sure that there were at
	 * least "repeat" seconds between the last execution of this action and this execution.
	 */
	public final int repeat;
	/**
	 * The name of the action, to identify it, e.g. in the config file
	 */
	public final String name;

	public Action(String name, int delay, int repeat)
	{
		this.name = name;
		this.delay = delay;
		this.repeat = repeat;
	}
}
