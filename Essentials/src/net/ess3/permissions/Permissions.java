package net.ess3.permissions;

import java.util.Locale;
import net.ess3.api.IPermission;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Permission;


public enum Permissions implements IPermission
{
	AFK,
	AFK_KICKEXEMPT,
	AFK_OTHERS,
	BACK_ONDEATH,
	BALANCE_OTHERS,
	BAN_EXEMPT,
	BAN_NOTIFY,
	BAN_OFFLINE,
	BREAK_BEDROCK,
	CHAT_COLOR,
	CHAT_IGNORE_EXEMPT,
	CHAT_SPY,
	CLEARINVENTORY_OTHERS,
	DELHOME_OTHERS,
	ECO_LOAN(Permission.Default.FALSE),
	EXP_GIVE,
	EXP_GIVE_OTHERS,
	EXP_SET,
	EXP_SET_OTHERS,
	EXP_OTHERS,
	FEED_OTHERS,
	FLY_OTHERS,
	GAMEMODE_OTHERS,
	GEOIP_HIDE(Permission.Default.FALSE),
	GEOIP_SHOW(Permission.Default.TRUE),
	GETPOS_OTHERS,
	GOD_OTHERS,
	HEAL_COOLDOWN_BYPASS,
	HEAL_OTHERS,
	HELPOP_RECEIVE,
	HOME_OTHERS,
	JAIL_EXEMPT,
	JOINFULLSERVER,
	INVSEE_MODIFY,
	INVSEE_PREVENT_MODIFY,
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
	OVERSIZEDSTACKS(Permission.Default.FALSE),
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
	WARP_LIST(Permission.Default.TRUE),
	WARP_HIDDEN,
	WARP_OTHERS,
	VANISH_SEE_OTHERS;
	private static final String base = "essentials.";
	private final String permission;
	private final Permission.Default defaultPerm;
	private transient Permission bukkitPerm = null;

	private Permissions()
	{
		this(Permission.Default.OP);
	}

	private Permissions(final Permission.Default defaultPerm)
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
	public Permission getPermission()
	{
		if (bukkitPerm != null)
		{
			return bukkitPerm;
		}
		else
		{
			return Permission.create(getPermissionName(), getPermissionDefault());
		}
	}


	@Override
	public Permission.Default getPermissionDefault()
	{
		return this.defaultPerm;
	}


	@Override
	public boolean isAuthorized(CommandSender sender)
	{
		return sender.hasPermission(getPermission());
	}
}

