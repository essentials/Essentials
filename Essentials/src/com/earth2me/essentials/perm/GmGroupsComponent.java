package com.earth2me.essentials.perm;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IGroupsComponent;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.Component;
import com.earth2me.essentials.components.users.IUserComponent;
import java.text.MessageFormat;
import lombok.Cleanup;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;


public final class GmGroupsComponent extends Component implements IGroupsComponent
{
	private final transient GroupManager groupManager;

	public GmGroupsComponent(final IContext context, final GroupManager groupManager)
	{
		super(context);

		this.groupManager = groupManager;
	}

	@Override
	public double getHealCooldown(IUserComponent player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player.getBase());
		if (handler == null)
		{
			return 0;
		}
		return handler.getPermissionDouble(player.getName(), "healcooldown");
	}

	@Override
	public double getTeleportCooldown(IUserComponent player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player.getBase());
		if (handler == null)
		{
			return 0;
		}
		return handler.getPermissionDouble(player.getName(), "teleportcooldown");
	}

	@Override
	public double getTeleportDelay(IUserComponent player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player.getBase());
		if (handler == null)
		{
			return 0;
		}
		return handler.getPermissionDouble(player.getName(), "teleportdelay");
	}

	@Override
	public String getPrefix(IUserComponent player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player.getBase());
		if (handler == null)
		{
			return null;
		}
		return handler.getUserPrefix(player.getName());
	}

	@Override
	public String getSuffix(IUserComponent player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player.getBase());
		if (handler == null)
		{
			return null;
		}
		return handler.getUserSuffix(player.getName());
	}

	@Override
	public int getHomeLimit(IUserComponent player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player.getBase());
		if (handler == null)
		{
			return 0;
		}
		return handler.getPermissionInteger(player.getName(), "homes");
	}

	@Override
	public MessageFormat getChatFormat(final IUserComponent player)
	{
		String format = getRawChatFormat(player);
		format = Util.replaceColor(format);
		format = format.replace("{DISPLAYNAME}", "%1$s");
		format = format.replace("{GROUP}", "{0}");
		format = format.replace("{MESSAGE}", "%2$s");
		format = format.replace("{WORLDNAME}", "{1}");
		format = format.replace("{SHORTWORLDNAME}", "{2}");
		format = format.replaceAll("\\{(\\D*)\\}", "\\[$1\\]");
		MessageFormat mFormat = new MessageFormat(format);
		return mFormat;
	}

	private String getRawChatFormat(final IUserComponent player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player.getBase());
		if (handler != null)
		{
			String chatformat = handler.getPermissionString(player.getName(), "chatformat");
			if (chatformat != null && !chatformat.isEmpty())
			{
				return chatformat;
			}
		}

		@Cleanup
		ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public String getMainGroup(IUserComponent player)
	{
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player.getBase());
		if (handler == null)
		{
			return null;
		}
		return handler.getGroup(player.getName());
	}

	@Override
	public boolean isInGroup(IUserComponent player, String groupname)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(player.getBase());
		if (handler == null)
		{
			return false;
		}
		return handler.inGroup(player.getName(), groupname);
	}
}
