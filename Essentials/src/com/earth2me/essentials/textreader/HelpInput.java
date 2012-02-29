package com.earth2me.essentials.textreader;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.ISettingsComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.HelpPermissions;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Cleanup;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;


public class HelpInput implements IText
{
	private static final String DESCRIPTION = "description";
	private static final String PERMISSION = "permission";
	private static final String PERMISSIONS = "permissions";
	private final transient List<String> lines = new ArrayList<String>();
	private final transient List<String> chapters = new ArrayList<String>();
	private final transient Map<String, Integer> bookmarks = new HashMap<String, Integer>();
	private final static Logger logger = Logger.getLogger("Minecraft");

	@SuppressWarnings("unchecked")
	public HelpInput(final IUserComponent user, final String match, final IContext ess) throws IOException
	{
		@Cleanup
		final ISettingsComponent settings = ess.getSettings();
		settings.acquireReadLock();
		boolean reported = false;
		String pluginName = "";
		for (Plugin p : ess.getServer().getPluginManager().getPlugins())
		{
			try
			{
				final PluginDescriptionFile desc = p.getDescription();
				final HashMap<String, HashMap<String, Object>> cmds = (HashMap)desc.getCommands();
				pluginName = p.getDescription().getName().toLowerCase(Locale.ENGLISH);
				for (Map.Entry<String, HashMap<String, Object>> k : cmds.entrySet())
				{
					try
					{
						if ((!match.equalsIgnoreCase(""))
							&& (!k.getKey().toLowerCase(Locale.ENGLISH).contains(match))
							&& (!(k.getValue().get(DESCRIPTION) instanceof String
								  && ((String)k.getValue().get(DESCRIPTION)).toLowerCase(Locale.ENGLISH).contains(match)))
							&& (!pluginName.contains(match)))
						{
							continue;
						}

						if (pluginName.contains("essentials"))
						{
							final String node = "essentials." + k.getKey();
							if (!settings.getData().getCommands().isDisabled(k.getKey()) && user.hasPermission(node))
							{
								lines.add("§c" + k.getKey() + "§7: " + k.getValue().get(DESCRIPTION));
							}
						}
						else
						{
							if (settings.getData().getCommands().getHelp().isShowNonEssCommandsInHelp())
							{
								final HashMap<String, Object> value = k.getValue();
								Object permissions = null;
								if (value.containsKey(PERMISSION))
								{
									permissions = value.get(PERMISSION);
								}
								else if (value.containsKey(PERMISSIONS))
								{
									permissions = value.get(PERMISSIONS);
								}
								if (HelpPermissions.getPermission(pluginName).isAuthorized(user))
								{
									lines.add("§c" + k.getKey() + "§7: " + value.get(DESCRIPTION));
								}
								else if (permissions instanceof List && !((List)permissions).isEmpty())
								{
									boolean enabled = false;
									for (Object o : (List)permissions)
									{
										if (o instanceof String && user.hasPermission(o.toString()))
										{
											enabled = true;
											break;
										}
									}
									if (enabled)
									{
										lines.add("§c" + k.getKey() + "§7: " + value.get(DESCRIPTION));
									}
								}
								else if (permissions instanceof String && !"".equals(permissions))
								{
									if (user.hasPermission(permissions.toString()))
									{
										lines.add("§c" + k.getKey() + "§7: " + value.get(DESCRIPTION));
									}
								}
								else
								{
									if (!settings.getData().getCommands().getHelp().isHidePermissionlessCommands())
									{
										lines.add("§c" + k.getKey() + "§7: " + value.get(DESCRIPTION));
									}
								}
							}
						}
					}
					catch (NullPointerException ex)
					{
						continue;
					}
				}
			}
			catch (NullPointerException ex)
			{
				continue;
			}
			catch (Exception ex)
			{
				if (!reported)
				{
					logger.log(Level.WARNING, $("commandHelpFailedForPlugin", pluginName), ex);
				}
				reported = true;
				continue;
			}
		}
	}

	@Override
	public List<String> getLines()
	{
		return Collections.unmodifiableList(lines);
	}

	@Override
	public List<String> getChapters()
	{
		return Collections.unmodifiableList(chapters);
	}

	@Override
	public Map<String, Integer> getBookmarks()
	{
		return Collections.unmodifiableMap(bookmarks);
	}
}
