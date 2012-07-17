package net.ess3.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.Material;
import net.ess3.permissions.Permissions;
import net.ess3.utils.Util;


public class Commandpowertool extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		String command = getFinalArg(args, 0);

		// check to see if this is a clear all command
		if (command != null && command.equalsIgnoreCase("d:"))
		{
			user.acquireWriteLock();
			user.getData().clearAllPowertools();
			user.sendMessage(_("powerToolClearAll"));
			return;
		}

		final ItemStack itemStack = user.getItemInHand();
		if (itemStack == null || itemStack.getType() == Material.AIR)
		{
			throw new Exception(_("powerToolAir"));
		}

		final String itemName = itemStack.getType().toString().toLowerCase(Locale.ENGLISH).replaceAll("_", " ");
		user.acquireReadLock();
		List<String> powertools = user.getData().getPowertool(itemStack.getType());
		if (command != null && !command.isEmpty())
		{
			if (command.equalsIgnoreCase("l:"))
			{
				if (powertools == null || powertools.isEmpty())
				{
					throw new Exception(_("powerToolListEmpty", itemName));
				}
				else
				{
					user.sendMessage(_("powerToolList", Util.joinList(powertools), itemName));
				}
				throw new NoChargeException();
			}
			if (command.startsWith("r:"))
			{
				command = command.substring(2);
				if (!powertools.contains(command))
				{
					throw new Exception(_("powerToolNoSuchCommandAssigned", command, itemName));
				}

				powertools.remove(command);
				user.sendMessage(_("powerToolRemove", command, itemName));
			}
			else
			{
				if (command.startsWith("a:"))
				{
					if (!Permissions.POWERTOOL_APPEND.isAuthorized(user))
					{
						throw new Exception(_("noPerm", "essentials.powertool.append"));
					}
					command = command.substring(2);
					if (powertools.contains(command))
					{
						throw new Exception(_("powerToolAlreadySet", command, itemName));
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
				user.sendMessage(_("powerToolAttach", Util.joinList(powertools), itemName));
			}
		}
		else
		{
			if (powertools != null)
			{
				powertools.clear();
			}
			user.sendMessage(_("powerToolRemoveAll", itemName));
		}

		if (!user.getData().isPowerToolsEnabled())
		{
			user.getData().setPowerToolsEnabled(true);
			user.sendMessage(_("powerToolsEnabled"));
		}
		user.acquireWriteLock();
		user.getData().setPowertool(itemStack.getType(), powertools);
	}
}
