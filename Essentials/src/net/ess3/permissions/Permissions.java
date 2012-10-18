package net.ess3.permissions;

import java.util.Locale;
import net.ess3.api.IPermission;
import net.ess3.bukkit.PermissionFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;


public enum Permissions implements IPermission
{
	AFK,
	AFK_KICKEXEMPT,
	AFK_OTHERS,
	BACK_ONDEATH,
	BALANCE_OTHERS,
	BALANCETOP_FORCE,
	BALANCETOP_HIDE,
	BALANCETOP_HIDE_OTHERS,
	BAN_EXEMPT,
	BAN_NOTIFY,
	BAN_OFFLINE,
	BREAK_BEDROCK,
	CHAT_COLOR,
	CHAT_IGNORE_EXEMPT,
	CHAT_SPY,
	CLEARINVENTORY_OTHERS,
	DELHOME_OTHERS,
	ECO_LOAN(PermissionDefault.FALSE),
	ENCHANT_UNSAFE(PermissionDefault.FALSE),
	ENDERCHEST_OTHERS,
	ESSENTIALS,
	EXP_GIVE,
	EXP_GIVE_OTHERS,
	EXP_SET,
	EXP_SET_OTHERS,
	EXP_OTHERS,
	FEED_OTHERS,
	FLY_EXEMPT,
	FLY_OTHERS,
	GAMEMODE_EXEMPT,
	GAMEMODE_OTHERS,
	GEOIP_HIDE(PermissionDefault.FALSE),
	GEOIP_SHOW(PermissionDefault.TRUE),
	GETPOS_OTHERS,
	GOD_EXEMPT,
	GOD_OFFLINE,
	GOD_OTHERS,
	GIVE_ENCHANTED,
	GIVE_ENCHANTED_UNSAFE(PermissionDefault.FALSE),
	HEAL_COOLDOWN_BYPASS,
	HEAL_OTHERS,
	HELPOP_RECEIVE,
	HOME_OTHERS,
	JAIL_EXEMPT,
	JOINFULLSERVER,
	INVSEE_MODIFY,
	INVSEE_PREVENT_MODIFY,
	ITEM_ENCHANTED,
	ITEM_ENCHANTED_UNSAFE(PermissionDefault.FALSE),
	KEEPXP,
	KICK_EXEMPT,
	KICK_NOTIFY,
	LIST_HIDDEN,
	LIGHTNING_OTHERS,
	MAIL,
	MAIL_SEND,
	MAIL_SENDALL,
	MOTD,
	MSG_COLOR,
	MUTE_EXEMPT,
	NEAR_OTHERS,
	NICK_COLOR,
	NICK_OTHERS,
	NOGOD_OVERRIDE,
	OVERSIZEDSTACKS(PermissionDefault.FALSE),
	POWERTOOL_APPEND,
	PTIME_OTHERS,
	PVPDELAY_EXEMPT,
	REPAIR_ARMOR,
	REPAIR_ENCHANTED,
	SEEN_BANREASON,
	SEEN_EXTRA,
	SETHOME_MULTIPLE,
	SETHOME_OTHERS,
	SLEEPINGIGNORED,
	SPAWN_OTHERS,
	SPEED_BYPASS,
	SPEED_OTHERS,
	SOCIALSPY_EXEMPT,
	SOCIALSPY_OFFLINE,
	SOCIALSPY_OTHERS,
	SUDO_EXEMPT,
	TELEPORT_COOLDOWN_BYPASS,
	TELEPORT_HIDDEN,
	TELEPORT_OTHERS,
	TELEPORT_TIMER_BYPASS,
	TEMPBAN_EXEMPT,
	TEMPBAN_OFFLINE,
	TIME_SET,
	TOGGLEJAIL_OFFLINE,
	TPA,
	TPAALL,
	TPAHERE,
	TPOHERE,
	UNLIMITED_OTHERS,
	WARP_LIST(PermissionDefault.TRUE),
	WARP_HIDDEN,
	WARP_OTHERS,
	VANISH_SEE_OTHERS, 
	VANISH_PVP;
	private static final String base = "essentials.";
	private final String permission;
	private final PermissionDefault defaultPerm;
	private transient String parent = null;

	private Permissions()
	{
		this(PermissionDefault.OP);
	}

	private Permissions(final PermissionDefault defaultPerm)
	{
		permission = base + toString().toLowerCase(Locale.ENGLISH).replace('_', '.');
		this.defaultPerm = defaultPerm;
	}

	@Override
	public String getPermissionName()
	{
		return permission;
	}

	@Override
	public String getParentPermission()
	{
		if (parent != null)
		{
			return parent;
		}
		else
		{
			return PermissionFactory.registerParentPermission(getPermissionName());
		}
	}

	@Override
	public PermissionDefault getPermissionDefault()
	{
		return this.defaultPerm;
	}

	@Override
	public boolean isAuthorized(CommandSender sender)
	{
		return PermissionFactory.checkPermission(sender, this);
	}
	public static DotStarPermission ENCHANT = new DotStarPermission("essentials.enchant");
	public static MaterialDotStarPermission GIVE = new MaterialDotStarPermission("essentials.give", PermissionDefault.TRUE);
	public static DotStarPermission RANKS = new DotStarPermission("essentials.ranks");
	public static DotStarPermission HELP = new DotStarPermission("essentials.help");
	public static MaterialDotStarPermission ITEMSPAWN = new MaterialDotStarPermission("essentials.itemspawn", PermissionDefault.TRUE);
	public static DotStarPermission KITS = new DotStarPermission("essentials.kits", PermissionDefault.TRUE);
	public static DotStarPermission NOCOMMANDCOST = new DotStarPermission("essentials.nocommandcost");
	public static DotStarPermission SPAWNER = new DotStarPermission("essentials.spawner");
	public static DotStarPermission SPAWNMOB = new DotStarPermission("essentials.spawnmob");
	public static MaterialDotStarPermission UNLIMITED = new MaterialDotStarPermission("essentials.unlimited.items");
	public static DotStarPermission WARPS = new DotStarPermission("essentials.warps", PermissionDefault.TRUE);
	public static DotStarPermission WARP_OVERWRITE = new DotStarPermission("essentials.warp.overwrite");
	public static DotStarPermission WORLD = new DotStarPermission("essentials.world");
	public static DotStarPermission NICK = new DotStarPermission("essentials.nick");
	public static DotStarPermission SIGNS = new DotStarPermission("essentials.signs");
	public static DotStarPermission CHAT = new DotStarPermission("essentials.chat");
	public static DotStarPermission TIME_WORLDS = new DotStarPermission("essentials.time.worlds", PermissionDefault.TRUE);
}
