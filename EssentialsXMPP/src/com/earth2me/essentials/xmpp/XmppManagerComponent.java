package com.earth2me.essentials.xmpp;

import com.earth2me.essentials.components.Component;
import com.earth2me.essentials.components.users.IUserComponent;
import java.io.File;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;


public class XmppManagerComponent extends Component implements MessageListener, ChatManagerListener
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private final transient XmppSettingsComponent settings;
	private transient XMPPConnection connection;
	private transient ChatManager chatManager;
	private final transient Map<String, Chat> chats = Collections.synchronizedMap(new HashMap<String, Chat>());
	private final transient Set<LogRecord> logrecords = Collections.synchronizedSet(new HashSet<LogRecord>());
	private final transient IEssentialsXmpp parent;
	private transient List<String> logUsers;
	private transient Level logLevel;
	private transient boolean ignoreLagMessages = true;
	private transient Thread loggerThread;
	private transient boolean threadrunning = true;
	private final transient LogHandler logHandler = new LogHandler();

	public XmppManagerComponent(final IEssentialsXmpp parent)
	{
		super(parent.getContext());

		this.parent = parent;
		this.settings = new XmppSettingsComponent(getContext());
	}

	@Override
	public void onEnable()
	{
		parent.add(settings);
	}

	public boolean sendMessage(final String address, final String message)
	{
		if (address != null && !address.isEmpty())
		{
			try
			{
				startChat(address);
				final Chat chat;
				synchronized (chats)
				{
					chat = chats.get(address);
				}
				if (chat != null)
				{
					if (!connection.isConnected())
					{
						disconnect();
						connect();
					}
					chat.sendMessage(message.replaceAll("§[0-9a-f]", ""));
					return true;
				}
			}
			catch (XMPPException ex)
			{
				disableChat(address);
			}
		}
		return false;
	}

	@Override
	public void processMessage(final Chat chat, final Message msg)
	{
		// Normally we should log the error message
		// But we would create a loop if the connection to a log-user fails.
		if (msg.getType() != Message.Type.error && msg.getBody().length() > 0)
		{
			final String message = msg.getBody();
			switch (message.charAt(0))
			{
			case '@':
				sendPrivateMessage(chat, message);
				break;
			case '/':
				sendCommand(chat, message);
				break;
			default:
				final IUserComponent sender = parent.getUserByAddress(StringUtils.parseBareAddress(chat.getParticipant()));
				parent.broadcastMessage(sender, "=" + sender.getDisplayName() + ": " + message, StringUtils.parseBareAddress(chat.getParticipant()));
			}
		}
	}

	private boolean connect()
	{
		settings.acquireReadLock();
		try
		{
			ServerSettings serverSettings = settings.getData().getXmpp();
			final String server = serverSettings.getServer();
			if (server == null || server.equals("example.com"))
			{
				LOGGER.log(Level.WARNING, "config broken for xmpp");
				return false;
			}
			final int port = serverSettings.getPort();
			final String serviceName = serverSettings.getServicename() == null ? server : serverSettings.getServicename();
			final String xmppuser = serverSettings.getUser();
			final String password = serverSettings.getPassword();
			final ConnectionConfiguration connConf = new ConnectionConfiguration(server, port, serviceName);
			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Connecting to xmpp server ").append(server).append(":").append(port);
			stringBuilder.append(" as user ").append(xmppuser).append(".");
			LOGGER.log(Level.INFO, stringBuilder.toString());
			connConf.setSASLAuthenticationEnabled(serverSettings.isSaslEnabled());
			connConf.setSendPresence(true);
			connConf.setReconnectionAllowed(true);
			connection = new XMPPConnection(connConf);
			try
			{
				connection.connect();
				connection.login(xmppuser, password);
				connection.getRoster().setSubscriptionMode(SubscriptionMode.accept_all);
				chatManager = connection.getChatManager();
				chatManager.addChatListener(this);
				return true;
			}
			catch (XMPPException ex)
			{
				LOGGER.log(Level.WARNING, "Failed to connect to server: " + server, ex);
				return false;
			}
		}
		finally
		{
			settings.unlock();
		}
	}

	public final void disconnect()
	{
		if (loggerThread != null)
		{
			loggerThread.interrupt();
		}
		if (chatManager != null)
		{
			chatManager.removeChatListener(this);
			chatManager = null;
		}
		if (connection != null)
		{
			connection.disconnect(new Presence(Presence.Type.unavailable));
		}

	}

	@Override
	public void chatCreated(final Chat chat, final boolean createdLocally)
	{
		if (!createdLocally)
		{
			chat.addMessageListener(this);
			final Chat old = chats.put(StringUtils.parseBareAddress(chat.getParticipant()), chat);
			if (old != null)
			{
				old.removeMessageListener(this);
			}
		}
	}

	@Override
	public final void reload()
	{
		LOGGER.removeHandler(logHandler);
		settings.reload();
		synchronized (chats)
		{
			disconnect();
			chats.clear();
			if (!connect())
			{
				return;
			}
			startLoggerThread();
		}
		settings.acquireReadLock();
		try
		{
			if (settings.getData().isLogEnabled())
			{
				LOGGER.addHandler(logHandler);
				logUsers = settings.getData().getLogUsers();
				logLevel = settings.getData().getLogLevel();
				ignoreLagMessages = settings.getData().isIgnoreLagMessages();
			}
		}
		finally
		{
			settings.unlock();
		}
	}

	private void startLoggerThread()
	{
		loggerThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				final Set<LogRecord> copy = new HashSet<LogRecord>();
				final Set<String> failedUsers = new HashSet<String>();
				while (threadrunning)
				{
					synchronized (logrecords)
					{
						if (!logrecords.isEmpty())
						{
							copy.addAll(logrecords);
							logrecords.clear();
						}
					}
					if (!copy.isEmpty())
					{
						for (String user : logUsers)
						{
							try
							{
								XmppManagerComponent.this.startChat(user);
								for (LogRecord logRecord : copy)
								{
									final String message = String.format("[" + logRecord.getLevel().getLocalizedName() + "] " + logRecord.getMessage(), logRecord.getParameters());
									if (!XmppManagerComponent.this.sendMessage(user, message))
									{
										failedUsers.add(user);
										break;
									}

								}
							}
							catch (XMPPException ex)
							{
								failedUsers.add(user);
								LOGGER.removeHandler(logHandler);
								LOGGER.log(Level.SEVERE, "Failed to deliver log message! Disabling logging to XMPP.", ex);
							}
						}
						logUsers.removeAll(failedUsers);
						if (logUsers.isEmpty())
						{
							LOGGER.removeHandler(logHandler);
							threadrunning = false;
						}
						copy.clear();
					}
					try
					{
						Thread.sleep(2000);
					}
					catch (InterruptedException ex)
					{
						threadrunning = false;
					}
				}
				LOGGER.removeHandler(logHandler);
			}
		});
		loggerThread.start();
	}

	private void startChat(final String address) throws XMPPException
	{
		if (chatManager == null)
		{
			return;
		}
		synchronized (chats)
		{
			if (!chats.containsKey(address))
			{
				final Chat chat = chatManager.createChat(address, this);
				if (chat == null)
				{
					throw new XMPPException("Could not start Chat with " + address);
				}
				chats.put(address, chat);
			}
		}
	}

	private void sendPrivateMessage(final Chat chat, final String message)
	{
		final String[] parts = message.split(" ", 2);
		if (parts.length == 2)
		{
			final List<Player> matches = parent.getServer().matchPlayer(parts[0].substring(1));

			if (matches.isEmpty())
			{
				try
				{
					chat.sendMessage("User " + parts[0] + " not found");
				}
				catch (XMPPException ex)
				{
					LOGGER.log(Level.WARNING, "Failed to send xmpp message.", ex);
				}
			}
			else
			{
				final String from = "[" + parent.getUserByAddress(StringUtils.parseBareAddress(chat.getParticipant())) + ">";
				for (Player p : matches)
				{
					p.sendMessage(from + p.getDisplayName() + "]  " + message);
				}
			}
		}
	}

	private void sendCommand(final Chat chat, final String message)
	{
		settings.acquireReadLock();
		try
		{
			if (settings.getData().getOpUsers().contains(StringUtils.parseBareAddress(chat.getParticipant())))
			{
				try
				{
					parent.getServer().dispatchCommand(parent.getServer().getConsoleSender(), message.substring(1));
				}
				catch (Exception ex)
				{
					LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
				}
			}
		}
		finally
		{
			settings.unlock();
		}
	}

	private void disableChat(final String address)
	{
		final Chat chat = chats.get(address);
		if (chat != null)
		{
			chat.removeMessageListener(this);
			chats.remove(address);
		}
	}


	private class LogHandler extends Handler
	{
		@Override
		public void publish(LogRecord logRecord)
		{
			try
			{
				if (ignoreLagMessages && logRecord.getMessage().equals("Can't keep up! Did the system time change, or is the server overloaded?"))
				{
					return;
				}
				if (logRecord.getLevel().intValue() >= logLevel.intValue())
				{
					synchronized (logrecords)
					{
						logrecords.add(logRecord);
					}
				}
			}
			catch (Exception e)
			{
				// Ignore all exceptions
				// Otherwise we create a loop.
			}
		}

		@Override
		public void flush()
		{
			//Ignore this
		}

		@Override
		public void close() throws SecurityException
		{
			//Ignore this
		}
	}
}
