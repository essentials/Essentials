package net.ess3.permissions;

import java.util.Locale;
import net.ess3.api.IPermission;
import net.ess3.bukkit.PermissionFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;


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
	ECO_LOAN(PermissionDefault.FALSE),
	EXP_GIVE,
	EXP_GIVE_OTHERS,
	EXP_SET,
	EXP_SET_OTHERS,
	EXP_OTHERS,
	FEED_OTHERS,
	FLY_OTHERS,
	GAMEMODE_OTHERS,
	GEOIP_HIDE(PermissionDefault.FALSE),
	GEOIP_SHOW(PermissionDefault.TRUE),
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
	VANISH_SEE_OTHERS;
	private static final String base = "essentials.";
	private final String permission;
	private final PermissionDefault defaultPerm;
	private transient Permission bukkitPerm = null;

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
	public Permission getPermission()
	{
		if (bukkitPerm != null)
		{
			return bukkitPerm;
		}
		else
		{
			return PermissionFactory.registerPermission(getPermissionName(), getPermissionDefault());
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
		return sender.hasPermission(getPermission());
	}
}

