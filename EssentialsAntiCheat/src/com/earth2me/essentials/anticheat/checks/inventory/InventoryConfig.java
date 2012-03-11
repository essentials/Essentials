package com.earth2me.essentials.anticheat.checks.inventory;

import com.earth2me.essentials.anticheat.ConfigItem;
import com.earth2me.essentials.anticheat.actions.types.ActionList;
import com.earth2me.essentials.anticheat.config.ConfPaths;
import com.earth2me.essentials.anticheat.config.NoCheatConfiguration;
import com.earth2me.essentials.anticheat.config.Permissions;


/**
 * Configurations specific for the "Inventory" checks Every world gets one of these assigned to it, or if a world
 * doesn't get it's own, it will use the "global" version
 *
 */
public class InventoryConfig implements ConfigItem
{
	public final boolean dropCheck;
	public final long dropTimeFrame;
	public final int dropLimit;
	public final ActionList dropActions;
	public final boolean bowCheck;
	public final ActionList bowActions;
	public final boolean eatCheck;
	public final ActionList eatActions;

	public InventoryConfig(NoCheatConfiguration data)
	{

		dropCheck = data.getBoolean(ConfPaths.INVENTORY_DROP_CHECK);
		dropTimeFrame = data.getInt(ConfPaths.INVENTORY_DROP_TIMEFRAME) * 1000;
		dropLimit = data.getInt(ConfPaths.INVENTORY_DROP_LIMIT);
		dropActions = data.getActionList(ConfPaths.INVENTORY_DROP_ACTIONS, Permissions.INVENTORY_DROP);

		bowCheck = data.getBoolean(ConfPaths.INVENTORY_INSTANTBOW_CHECK);
		bowActions = data.getActionList(ConfPaths.INVENTORY_INSTANTBOW_ACTIONS, Permissions.INVENTORY_INSTANTBOW);

		eatCheck = data.getBoolean(ConfPaths.INVENTORY_INSTANTEAT_CHECK);
		eatActions = data.getActionList(ConfPaths.INVENTORY_INSTANTEAT_ACTIONS, Permissions.INVENTORY_INSTANTEAT);
	}
}
