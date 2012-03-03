package com.earth2me.essentials.xmpp;

import com.earth2me.essentials.api.EssentialsPlugin;
import com.earth2me.essentials.api.ICommandsComponent;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.commands.CommandsComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUserComponent;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;


public class EssentialsXmpp extends EssentialsPlugin implements IEssentialsXmpp
{
	private static EssentialsXmpp instance = null;
	//private transient UserManagerComponent users;
	private transient XmppManagerComponent xmpp;
	private transient ICommandsComponent commands;

	@SuppressWarnings("ReturnOfCollectionOrArrayField")
	public static IEssentialsXmpp getInstance()
	{
		return instance;
	}

	@Override
	public void onEnable()
	{
		// Call FIRST.
		super.onEnable();

		instance = this;

		final PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new EssentialsXmppPlayerListener(getContext()), this);

		commands = new CommandsComponent(EssentialsXmpp.class.getClassLoader(), "com.earth2me.essentials.xmpp.Command", getContext());

		//add(users = new UserManagerComponent(getContext(), getDataFolder()));
		add(xmpp = new XmppManagerComponent(this));

		initialize();
	}

	@Override
	public void onDisable()
	{
		try
		{
			if (xmpp != null)
			{
				xmpp.disconnect();
			}
		}
		finally
		{
			instance = null;
			super.onDisable();
		}
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args)
	{
		return getContext().getCommands().handleCommand(sender, command, commandLabel, args);
	}

	@Override
	public void setAddress(final Player player, final String address)
	{
		final IUserComponent user = getContext().getUser(player);
		user.acquireWriteLock();
		try {
			user.getData().setXmppAddress(address);
		} finally {
			user.unlock();
		}
	}

	@Override
	public String getAddress(final String name)
	{
		final IUserComponent user = getContext().getUser(name);
		if (user == null) {
			return null;
		}
		user.acquireReadLock();
		try {
			return user.getData().getXmppAddress();
		} finally {
			user.unlock();
		}
	}

	@Override
	public IUserComponent getUserByAddress(final String address)
	{
		return null; // TODO: Rewrite me
		//String username = instance.users.getUserByAddress(address);
		//return username == null ? null : getContext().getUser(username);
	}

	@Override
	public boolean toggleSpy(final Player player)
	{
		final IUserComponent user = getContext().getUser(player);
		user.acquireWriteLock();
		try {
			boolean value = !user.getData().isXmppSpy();
			user.getData().setXmppSpy(value);
			return value;
		} finally {
			user.unlock();
		}
	}

	@Override
	public String getAddress(final Player player)
	{
		final IUserComponent user = getContext().getUser(player);
		if (user == null) {
			return null;
		}
		user.acquireReadLock();
		try {
			return user.getData().getXmppAddress();
		} finally {
			user.unlock();
		}
	}

	@Override
	public boolean sendMessage(final Player user, final String message)
	{
		return instance.xmpp.sendMessage(getAddress(user), message);
	}

	@Override
	public boolean sendMessage(final String address, final String message)
	{
		return instance.xmpp.sendMessage(address, message);
	}

	@Override
	public List<String> getSpyUsers()
	{
		return null;
		//TODO fix me
		//return instance.users.getSpyUsers();
	}

	@Override
	public void broadcastMessage(final IUserComponent sender, final String message, final String xmppAddress)
	{
		getContext().getMessager().broadcastMessage(sender, message);
		try
		{
			for (String address : getSpyUsers())
			{
				if (!address.equalsIgnoreCase(xmppAddress))
				{
					sendMessage(address, message);
				}
			}
		}
		catch (Exception ex)
		{
			// Ignore exceptions
		}
	}
}
