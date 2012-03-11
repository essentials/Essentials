package com.earth2me.essentials.anticheat.config;


/**
 * These are the default settings for NoCheat. They will be used in addition to/in replacement of configurations given
 * in the config.yml file
 *
 */
public class DefaultConfiguration extends NoCheatConfiguration
{
	public DefaultConfiguration()
	{

		super();

		this.options().header("Main configuration file for NoCheat. Read \"Instructions.txt\"");

		/**
		 * LOGGING *
		 */
		set(ConfPaths.LOGGING_ACTIVE, true);
		set(ConfPaths.LOGGING_SHOWACTIVECHECKS, false);
		set(ConfPaths.LOGGING_DEBUGMESSAGES, false);
		set(ConfPaths.LOGGING_PREFIX, "&4NC&f: ");
		set(ConfPaths.LOGGING_FILENAME, "nocheat.log");
		set(ConfPaths.LOGGING_LOGTOFILE, true);
		set(ConfPaths.LOGGING_LOGTOCONSOLE, true);
		set(ConfPaths.LOGGING_LOGTOINGAMECHAT, true);

		/**
		 * * INVENTORY **
		 */
		set(ConfPaths.INVENTORY_DROP_CHECK, true);
		set(ConfPaths.INVENTORY_DROP_TIMEFRAME, 20);
		set(ConfPaths.INVENTORY_DROP_LIMIT, 100);
		set(ConfPaths.INVENTORY_DROP_ACTIONS, "log:drop:0:1:cif cmd:kick");

		set(ConfPaths.INVENTORY_INSTANTBOW_CHECK, true);
		set(ConfPaths.INVENTORY_INSTANTBOW_ACTIONS, "log:ibow:2:5:if cancel");

		set(ConfPaths.INVENTORY_INSTANTEAT_CHECK, true);
		set(ConfPaths.INVENTORY_INSTANTEAT_ACTIONS, "log:ieat:2:5:if cancel");

		/**
		 * * MOVING **
		 */
		set(ConfPaths.MOVING_RUNFLY_CHECK, true);
		set(ConfPaths.MOVING_RUNFLY_ALLOWFASTSNEAKING, false);
		set(ConfPaths.MOVING_RUNFLY_ACTIONS, "log:moveshort:3:5:f cancel vl>100 log:moveshort:0:5:if cancel vl>400 log:movelong:0:5:cif cancel");

		set(ConfPaths.MOVING_RUNFLY_CHECKNOFALL, true);
		set(ConfPaths.MOVING_RUNFLY_NOFALLAGGRESSIVE, true);
		set(ConfPaths.MOVING_RUNFLY_NOFALLACTIONS, "log:nofall:0:5:cif cancel");

		set(ConfPaths.MOVING_RUNFLY_FLYING_ALLOWALWAYS, false);
		set(ConfPaths.MOVING_RUNFLY_FLYING_ALLOWINCREATIVE, true);
		set(ConfPaths.MOVING_RUNFLY_FLYING_SPEEDLIMITHORIZONTAL, 60);
		set(ConfPaths.MOVING_RUNFLY_FLYING_SPEEDLIMITVERTICAL, 100);
		set(ConfPaths.MOVING_RUNFLY_FLYING_HEIGHTLIMIT, 128);
		set(ConfPaths.MOVING_RUNFLY_FLYING_ACTIONS, "log:moveshort:3:5:f cancel vl>100 log:moveshort:0:5:if cancel vl>400 log:movelong:0:5:cif cancel");

		set(ConfPaths.MOVING_MOREPACKETS_CHECK, true);
		set(ConfPaths.MOVING_MOREPACKETS_ACTIONS, "log:morepackets:3:2:if cancel vl>20 log:morepackets:0:2:if cancel");

		/**
		 * * BLOCKBREAK **
		 */
		set(ConfPaths.BLOCKBREAK_REACH_CHECK, true);
		set(ConfPaths.BLOCKBREAK_REACH_ACTIONS, "cancel vl>5 log:bbreach:0:2:if cancel");

		set(ConfPaths.BLOCKBREAK_DIRECTION_CHECK, true);
		set(ConfPaths.BLOCKBREAK_DIRECTION_PRECISION, 50);
		set(ConfPaths.BLOCKBREAK_DIRECTION_PENALTYTIME, 300);
		set(ConfPaths.BLOCKBREAK_DIRECTION_ACTIONS, "cancel vl>10 log:bbdirection:0:5:if cancel");

		set(ConfPaths.BLOCKBREAK_NOSWING_CHECK, true);
		set(ConfPaths.BLOCKBREAK_NOSWING_ACTIONS, "log:bbnoswing:3:2:if cancel");

		/**
		 * * BLOCKPLACE **
		 */
		set(ConfPaths.BLOCKPLACE_REACH_CHECK, true);
		set(ConfPaths.BLOCKPLACE_REACH_ACTIONS, "cancel vl>5 log:bpreach:0:2:if cancel");

		set(ConfPaths.BLOCKPLACE_DIRECTION_CHECK, true);
		set(ConfPaths.BLOCKPLACE_DIRECTION_PRECISION, 75);
		set(ConfPaths.BLOCKPLACE_DIRECTION_PENALTYTIME, 100);
		set(ConfPaths.BLOCKPLACE_DIRECTION_ACTIONS, "cancel vl>10 log:bpdirection:0:3:if cancel");

		/**
		 * * CHAT **
		 */
		set(ConfPaths.CHAT_COLOR_CHECK, true);
		set(ConfPaths.CHAT_COLOR_ACTIONS, "log:color:0:1:if cancel");

		set(ConfPaths.CHAT_SPAM_CHECK, true);
		set(ConfPaths.CHAT_SPAM_WHITELIST, "");
		set(ConfPaths.CHAT_SPAM_TIMEFRAME, 3);
		set(ConfPaths.CHAT_SPAM_MESSAGELIMIT, 3);
		set(ConfPaths.CHAT_SPAM_COMMANDLIMIT, 12);
		set(ConfPaths.CHAT_SPAM_ACTIONS, "log:spam:0:3:if cancel vl>30 log:spam:0:3:cif cancel cmd:kick");

		/**
		 * * FIGHT **
		 */
		set(ConfPaths.FIGHT_DIRECTION_CHECK, true);
		set(ConfPaths.FIGHT_DIRECTION_PRECISION, 75);
		set(ConfPaths.FIGHT_DIRECTION_PENALTYTIME, 500);
		set(ConfPaths.FIGHT_DIRECTION_ACTIONS, "cancel vl>5 log:fdirection:3:5:f cancel vl>20 log:fdirection:0:5:if cancel vl>50 log:fdirection:0:5:cif cancel");

		set(ConfPaths.FIGHT_NOSWING_CHECK, true);
		set(ConfPaths.FIGHT_NOSWING_ACTIONS, "log:fnoswing:0:5:cif cancel");

		set(ConfPaths.FIGHT_REACH_CHECK, true);
		set(ConfPaths.FIGHT_REACH_LIMIT, 400);
		set(ConfPaths.FIGHT_REACH_PENALTYTIME, 500);
		set(ConfPaths.FIGHT_REACH_ACTIONS, "cancel vl>10 log:freach:2:5:if cancel");

		set(ConfPaths.FIGHT_SPEED_CHECK, true);
		set(ConfPaths.FIGHT_SPEED_ATTACKLIMIT, 15);
		set(ConfPaths.FIGHT_SPEED_ACTIONS, "log:fspeed:0:5:if cancel");

		set(ConfPaths.FIGHT_GODMODE_CHECK, true);
		set(ConfPaths.FIGHT_GODMODE_ACTIONS, "log:fgod:2:5:if cancel");

		set(ConfPaths.FIGHT_INSTANTHEAL_CHECK, true);
		set(ConfPaths.FIGHT_INSTANTHEAL_ACTIONS, "log:fheal:1:1:if cancel");

		set(ConfPaths.STRINGS + ".drop", "[player] failed [check]: Tried to drop more items than allowed. VL [violations]");
		set(ConfPaths.STRINGS + ".moveshort", "[player] failed [check]. VL [violations]");
		set(ConfPaths.STRINGS + ".movelong", "[player] in [world] at [location] moving to [locationto] over distance [movedistance] failed check [check]. Total violation level so far [violations]");
		set(ConfPaths.STRINGS + ".nofall", "[player] failed [check]: tried to avoid fall damage for ~[falldistance] blocks. VL [violations]");
		set(ConfPaths.STRINGS + ".morepackets", "[player] failed [check]: Sent [packets] more packets than expected. Total violation level [violations]");
		set(ConfPaths.STRINGS + ".bbreach", "[player] failed [check]: tried to interact with a block over distance [reachdistance]. VL [violations]");
		set(ConfPaths.STRINGS + ".bbdirection", "[player] failed [check]: tried to interact with a block out of line of sight. VL [violations]");
		set(ConfPaths.STRINGS + ".bbnoswing", "[player] failed [check]: Didn't swing arm. VL [violations]");
		set(ConfPaths.STRINGS + ".bpreach", "[player] failed [check]: tried to interact with a block over distance [reachdistance]. VL [violations]");
		set(ConfPaths.STRINGS + ".bpdirection", "[player] failed [check]: tried to interact with a block out of line of sight. VL [violations]");
		set(ConfPaths.STRINGS + ".color", "[player] failed [check]: Sent colored chat message '[text]'. VL [violations]");
		set(ConfPaths.STRINGS + ".spam", "[player] failed [check]: Last sent message '[text]'. VL [violations]");
		set(ConfPaths.STRINGS + ".fdirection", "[player] failed [check]: tried to interact with a block out of line of sight. VL [violations]");
		set(ConfPaths.STRINGS + ".freach", "[player] failed [check]: tried to attack entity out of reach. VL [violations]");
		set(ConfPaths.STRINGS + ".fspeed", "[player] failed [check]: tried to attack more than [limit] times per second. VL [violations]");
		set(ConfPaths.STRINGS + ".fnoswing", "[player] failed [check]: Didn't swing arm. VL [violations]");
		set(ConfPaths.STRINGS + ".fgod", "[player] failed [check]: Avoided taking damage or lagging. VL [violations]");
		set(ConfPaths.STRINGS + ".fheal", "[player] failed [check]: Tried to regenerate health faster than normal. VL [violations]");
		set(ConfPaths.STRINGS + ".ibow", "[player] failed [check]: Fires bow to fast. VL [violations]");
		set(ConfPaths.STRINGS + ".ieat", "[player] failed [check]: Eats food [food] too fast. VL [violations]");
		set(ConfPaths.STRINGS + ".kick", "kick [player]");

		// Update internal factory based on all the new entries to the "actions" section
		regenerateActionLists();
	}
}
