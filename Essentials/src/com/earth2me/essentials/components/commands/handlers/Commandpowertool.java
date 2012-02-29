package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class Commandpowertool extends EssentialsCommand
{
	@Override
	protected void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		String command = getFinalArg(args, 0);

		// check to see if this is a clear all command
		if (command != null && command.equalsIgnoreCase("d:"))
		{
			user.acquireWriteLock();
			user.getData().clearAllPowertools();
			user.sendMessage($("powerToolClearAll"));
			return;
		}

		final ItemStack itemStack = user.getItemInHand();
		if (itemStack == null || itemStack.getType() == Material.AIR)
		{
			throw new Exception($("powerToolAir"));
		}

		final String itemName = itemStack.getType().toString().toLowerCase(Locale.ENGLISH).replaceAll("_", " ");
		List<String> powertools = user.getPowerTool(itemStack.getType());
		if (command != null && !command.isEmpty())
		{
			if (command.equalsIgnoreCase("l:"))
			{
				if (powertools == null || powertools.isEmpty())
				{
					throw new Exception($("powerToolListEmpty", itemName));
				}
				else
				{
					user.sendMessage($("powerToolList", Util.joinList(powertools), itemName));
				}
				return;
			}
			if (command.startsWith("r:"))
			{
				try
				{
					command = command.substring(2);
					if (!powertools.contains(command))
					{
						throw new Exception($("powerToolNoSuchCommandAssigned", command, itemName));
					}

					powertools.remove(command);
					user.sendMessage($("powerToolRemove", command, itemName));
				}
				catch (Exception e)
				{
					user.sendMessage(e.getMessage());
					return;
				}
			}
			else
			{
				if (command.startsWith("a:"))
				{
					if (!Permissions.POWERTOOL_APPEND.isAuthorized(user))
					{
						throw new Exception($("noPerm", "essentials.powertool.append"));
					}
					command = command.substring(2);
					if (powertools.contains(command))
					{
						throw new Exception($("powerToolAlreadySet", command, itemName));
					}

				}
				else if (powertools != null && !powertools.isEmpty())
				{
					// Replace all commands with this one
					powertools.clear();
				}
				else
				{
					powertools = new ArrayList<String>();
				}

				powertools.add(command);
				user.sendMessage($("powerToolAttach", Util.joinList(powertools), itemName));
			}
		}
		else
		{
			if (powertools != null)
			{
				powertools.clear();
			}
			user.sendMessage($("powerToolRemoveAll", itemName));
		}

		user.acquireWriteLock();
		user.getData().setPowertool(itemStack.getType(), powertools);
	}
}
