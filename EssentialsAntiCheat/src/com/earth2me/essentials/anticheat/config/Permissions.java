package com.earth2me.essentials.anticheat.config;


/**
 * The various permission nodes used by NoCheat
 *
 */
public class Permissions
{
	private static final String NOCHEAT = "nocheat";
	private static final String ADMIN = NOCHEAT + ".admin";
	private static final String CHECKS = NOCHEAT + ".checks";
	private static final String MODS = NOCHEAT + ".mod";
	public static final String MOVING = CHECKS + ".moving";
	public static final String MOVING_RUNFLY = MOVING + ".runfly";
	public static final String MOVING_SWIMMING = MOVING + ".swimming";
	public static final String MOVING_SNEAKING = MOVING + ".sneaking";
	public static final String MOVING_FLYING = MOVING + ".flying";
	public static final String MOVING_NOFALL = MOVING + ".nofall";
	public static final String MOVING_MOREPACKETS = MOVING + ".morepackets";
	public static final String BLOCKBREAK = CHECKS + ".blockbreak";
	public static final String BLOCKBREAK_REACH = BLOCKBREAK + ".reach";
	public static final String BLOCKBREAK_DIRECTION = BLOCKBREAK + ".direction";
	public static final String BLOCKBREAK_NOSWING = BLOCKBREAK + ".noswing";
	public static final String BLOCKPLACE = CHECKS + ".blockplace";
	public static final String BLOCKPLACE_REACH = BLOCKPLACE + ".reach";
	public static final String BLOCKPLACE_DIRECTION = BLOCKPLACE + ".direction";
	public static final String CHAT = CHECKS + ".chat";
	public static final String CHAT_SPAM = CHAT + ".spam";
	public static final String CHAT_COLOR = CHAT + ".color";
	public static final String FIGHT = CHECKS + ".fight";
	public static final String FIGHT_DIRECTION = FIGHT + ".direction";
	public static final String FIGHT_NOSWING = FIGHT + ".noswing";
	public static final String FIGHT_REACH = FIGHT + ".reach";
	public static final String FIGHT_SPEED = FIGHT + ".speed";
	public static final String FIGHT_GODMODE = FIGHT + ".godmode";
	public static final String FIGHT_INSTANTHEAL = FIGHT + ".instantheal";
	public static final String ADMIN_CHATLOG = ADMIN + ".chatlog";
	public static final String ADMIN_COMMANDS = ADMIN + ".commands";
	public static final String ADMIN_RELOAD = ADMIN + ".reload";
	public static final String INVENTORY = CHECKS + ".inventory";
	public static final String INVENTORY_DROP = INVENTORY + ".drop";
	public static final String INVENTORY_INSTANTBOW = INVENTORY + ".instantbow";
	public static final String INVENTORY_INSTANTEAT = INVENTORY + ".instanteat";
	public static final String ZOMBES_CHEAT = MODS + ".zombes.fly";
	public static final String ZOMBES_FLY = MODS + ".zombes.cheat";
	public static final String CJB_FLY = MODS + ".cjb.fly";
	public static final String CJB_XRAY = MODS + ".cjb.xray";
	public static final String CJB_MINIMAP = MODS + ".cjb.minimap";
}
