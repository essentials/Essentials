package com.earth2me.essentials.anticheat.data;

import com.earth2me.essentials.anticheat.actions.Action;
import java.util.HashMap;
import java.util.Map;


/**
 * Store amount of action executions for last 60 seconds for various actions
 *
 */
public class ExecutionHistory
{
	private static class ExecutionHistoryEntry
	{
		private final int executionTimes[];
		private long lastExecution = 0;
		private int totalEntries = 0;
		private long lastClearedTime = 0;

		private ExecutionHistoryEntry(int monitoredTimeFrame)
		{
			this.executionTimes = new int[monitoredTimeFrame];
		}

		/**
		 * Remember an execution at the specific time
		 */
		private void addCounter(long time)
		{
			// clear out now outdated values from the array
			if (time - lastClearedTime > 0)
			{
				// Clear the next few fields of the array
				clearTimes(lastClearedTime + 1, time - lastClearedTime);
				lastClearedTime = time + 1;
			}

			executionTimes[(int)(time % executionTimes.length)]++;
			totalEntries++;
		}

		/**
		 * Clean parts of the array
		 *
		 * @param start
		 * @param length
		 */
		private void clearTimes(long start, long length)
		{

			if (length <= 0)
			{
				return; // nothing to do (yet)
			}
			if (length > executionTimes.length)
			{
				length = executionTimes.length;
			}

			int j = (int)start % executionTimes.length;

			for (int i = 0; i < length; i++)
			{
				if (j == executionTimes.length)
				{
					j = 0;
				}

				totalEntries -= executionTimes[j];
				executionTimes[j] = 0;

				j++;
			}
		}

		public int getCounter()
		{
			return totalEntries;
		}

		public long getLastExecution()
		{
			return lastExecution;
		}

		public void setLastExecution(long time)
		{
			this.lastExecution = time;
		}
	}
	// Store data between Events
	// time + action + action-counter
	private final Map<String, Map<Action, ExecutionHistoryEntry>> executionHistories;

	public ExecutionHistory()
	{
		executionHistories = new HashMap<String, Map<Action, ExecutionHistoryEntry>>();
	}

	/**
	 * Returns true, if the action should be executed, because all time criteria have been met. Will add a entry with
	 * the time to a list which will influence further requests, so only use once and remember the result
	 *
	 * @param check
	 * @param action
	 * @param time a time IN SECONDS
	 * @return
	 */
	public boolean executeAction(String check, Action action, long time)
	{

		Map<Action, ExecutionHistoryEntry> executionHistory = executionHistories.get(check);

		if (executionHistory == null)
		{
			executionHistory = new HashMap<Action, ExecutionHistoryEntry>();
			executionHistories.put(check, executionHistory);
		}

		ExecutionHistoryEntry entry = executionHistory.get(action);

		if (entry == null)
		{
			entry = new ExecutionHistoryEntry(60);
			executionHistory.put(action, entry);
		}

		// update entry
		entry.addCounter(time);

		if (entry.getCounter() > action.delay)
		{
			// Execute action?
			if (entry.getLastExecution() <= time - action.repeat)
			{
				// Execute action!
				entry.setLastExecution(time);
				return true;
			}
		}

		return false;
	}
}
