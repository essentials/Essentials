package net.ess3.xmpp;

import java.io.File;
import java.util.*;
import java.util.logging.*;
import net.ess3.api.IEssentials;
import net.ess3.api.IReload;
import net.ess3.api.IUser;
import net.ess3.utils.FormatUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;


public final class XMPPManager extends Handler implements MessageListener, ChatManagerListener, IReload
{
	private static final SimpleFormatter formatter = new SimpleFormatter();
	private YamlConfiguration config = null;
	private XMPPConnection connection;
	private ChatManager chatManager;
	private final Map<String, Chat> chats = Collections.synchronizedMap(new HashMap<String, Chat>());
	private final Set<LogRecord> logrecords = Collections.synchronizedSet(new HashSet<LogRecord>());
	private final IEssentialsXMPP parent;
	private List<String> logUsers;
	private Level logLevel;
	private boolean ignoreLagMessages = true;
	private Thread loggerThread;
	private boolean threadrunning = true;

	public XMPPManager(final IEssentialsXMPP parent)
	{
		super();
		this.parent = parent;
		// config.setTemplateName("/config.yml", EssentialsXMPP.class);
		onReload();
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
					chat.sendMessage(FormatUtil.stripFormat(message));
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
				final IUser sender = parent.getUserByAddress(StringUtils.parseBareAddress(chat.getParticipant()));
				parent.broadcastMessage(sender, "=" + sender.getName() + ": " + message, StringUtils.parseBareAddress(chat.getParticipant()));
			}
		}
	}

	private boolean connect()
	{
		final String server = config.getString("xmpp.server");
		if (server == null || server.equals("example.com"))
		{
			parent.getLogger().log(Level.WARNING, "config broken for xmpp");
			return false;
		}
		final int port = config.getInt("xmpp.port", 5222);
		final String serviceName = config.getString("xmpp.servicename", server);
		final String xmppuser = config.getString("xmpp.user");
		final String password = config.getString("xmpp.password");
		final ConnectionConfiguration connConf = new ConnectionConfiguration(server, port, serviceName);
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Connecting to xmpp server ").append(server).append(":").append(port);
		stringBuilder.append(" as user ").append(xmppuser).append(".");
		parent.getLogger().log(Level.INFO, stringBuilder.toString());
		connConf.setSASLAuthenticationEnabled(config.getBoolean("xmpp.sasl-enabled", false));
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
			parent.getLogger().log(Level.WARNING, "Failed to connect to server: " + server, ex);
			return false;
		}
	}

	public void disconnect()
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
	public void onReload()
	{
		parent.getLogger().removeHandler(this);
		config = YamlConfiguration.loadConfiguration(new File(parent.getDataFolder(), "config.yml"));
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
		if (config.getBoolean("log-enabled", false))
		{
			parent.getLogger().addHandler(this);
			logUsers = config.getStringList("log-users");
			final String level = config.getString("log-level", "info");
			try
			{
				logLevel = Level.parse(level.toUpperCase(Locale.ENGLISH));
			}
			catch (IllegalArgumentException e)
			{
				logLevel = Level.INFO;
			}
			ignoreLagMessages = config.getBoolean("ignore-lag-messages", true);
		}
	}

	@Override
	public void publish(final LogRecord logRecord)
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
		// Ignore this
	}

	@Override
	public void close() throws SecurityException
	{
		// Ignore this
	}

	private void startLoggerThread()
	{
		loggerThread = new Thread(
				new Runnable()
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
										XMPPManager.this.startChat(user);
										for (LogRecord logRecord : copy)
										{
											final String message = formatter.format(logRecord);
											if (!XMPPManager.this.sendMessage(user, message))
											{
												failedUsers.add(user);
												break;
											}

										}
									}
									catch (XMPPException ex)
									{
										failedUsers.add(user);
										parent.getLogger().removeHandler(XMPPManager.this);
										parent.getLogger().log(Level.SEVERE, "Failed to deliver log message! Disabling logging to XMPP.", ex);
									}
								}
								logUsers.removeAll(failedUsers);
								if (logUsers.isEmpty())
								{
									parent.getLogger().removeHandler(XMPPManager.this);
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
						parent.getLogger().removeHandler(XMPPManager.this);
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
					parent.getLogger().log(Level.WARNING, "Failed to send xmpp message.", ex);
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
		if (config.getStringList("op-users").contains(StringUtils.parseBareAddress(chat.getParticipant())))
		{
			try
			{
				parent.getServer().dispatchCommand(parent.getServer().getConsoleSender(), message.substring(1));
			}
			catch (Exception ex)
			{
				parent.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
			}
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
}
