package com.earth2me.essentials.perm;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IGroupsComponent;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.Component;
import com.earth2me.essentials.components.users.IUserComponent;
import java.text.MessageFormat;
import lombok.Cleanup;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;


public final class VaultGroupsComponent extends Component implements IGroupsComponent
{
	public VaultGroupsComponent(final IContext context)
	{
		super(context);
	}

	@Override
	public double getHealCooldown(IUserComponent player)
	{
		final RegisteredServiceProvider<Chat> rsp = getContext().getServer().getServicesManager().getRegistration(Chat.class);
		final Chat chat = rsp.getProvider();
		return chat.getPlayerInfoDouble(player.getBase(), "healcooldown", 0);
	}

	@Override
	public double getTeleportCooldown(IUserComponent player)
	{
		RegisteredServiceProvider<Chat> rsp = getContext().getServer().getServicesManager().getRegistration(Chat.class);
		Chat chat = rsp.getProvider();
		return chat.getPlayerInfoDouble(player.getBase(), "teleportcooldown", 0);
	}

	@Override
	public double getTeleportDelay(IUserComponent player)
	{
		RegisteredServiceProvider<Chat> rsp = getContext().getServer().getServicesManager().getRegistration(Chat.class);
		Chat chat = rsp.getProvider();
		return chat.getPlayerInfoDouble(player.getBase(), "teleportdelay", 0);
	}

	@Override
	public String getPrefix(IUserComponent player)
	{
		RegisteredServiceProvider<Chat> rsp = getContext().getServer().getServicesManager().getRegistration(Chat.class);
		Chat chat = rsp.getProvider();
		return chat.getPlayerPrefix(player.getBase());
	}

	@Override
	public String getSuffix(IUserComponent player)
	{
		RegisteredServiceProvider<Chat> rsp = getContext().getServer().getServicesManager().getRegistration(Chat.class);
		Chat chat = rsp.getProvider();
		return chat.getPlayerSuffix(player.getBase());
	}

	@Override
	public int getHomeLimit(IUserComponent player)
	{
		RegisteredServiceProvider<Chat> rsp = getContext().getServer().getServicesManager().getRegistration(Chat.class);
		Chat chat = rsp.getProvider();
		return chat.getPlayerInfoInteger(player.getBase(), "homes", 0);
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
		RegisteredServiceProvider<Chat> rsp = getContext().getServer().getServicesManager().getRegistration(Chat.class);
		Chat chat = rsp.getProvider();
		String chatformat = chat.getPlayerInfoString(player.getBase(), "chatformat", "");
		if (chatformat != null && !chatformat.isEmpty())
		{
			return chatformat;
		}

		@Cleanup
		ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public String getMainGroup(IUserComponent player)
	{
		RegisteredServiceProvider<Chat> rsp = getContext().getServer().getServicesManager().getRegistration(Chat.class);
		Chat chat = rsp.getProvider();
		return chat.getPrimaryGroup(player.getBase());
	}

	@Override
	public boolean isInGroup(IUserComponent player, String groupname)
	{
		RegisteredServiceProvider<Chat> rsp = getContext().getServer().getServicesManager().getRegistration(Chat.class);
		Chat chat = rsp.getProvider();
		for (String group : chat.getPlayerGroups(player.getBase()))
		{
			if (group.equalsIgnoreCase(groupname))
			{
				return true;
			}
		}
		return false;
	}
}
