package net.ess3.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData;
import net.ess3.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class Commandpowertool extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		String command = getFinalArg(args, 0);
		UserData userData = user.getData();
		// check to see if this is a clear all command
		if (command.equalsIgnoreCase("d:"))
		{
			userData.clearAllPowertools();
			user.queueSave();
			user.sendMessage(_("All powertool commands have been cleared."));
			return;
		}

		final ItemStack itemStack = user.getPlayer().getItemInHand();
		if (itemStack == null || itemStack.getType() == Material.AIR)
		{
			throw new Exception(_("Command can't be attached to air."));
		}

		final String itemName = itemStack.getType().toString().toLowerCase(Locale.ENGLISH).replaceAll("_", " ");
		List<String> powertools = userData.getPowertool(itemStack.getType());
		if (!command.isEmpty())
		{
			if (command.equalsIgnoreCase("l:"))
			{
				if (powertools == null || powertools.isEmpty())
				{
					throw new Exception(_("Item {0} has no commands assigned.", itemName));
				}
				else
				{
					user.sendMessage(_("Item {1} has the following commands: {0}.", Util.joinList(powertools), itemName));
				}
				throw new NoChargeException();
			}
			if (command.startsWith("r:"))
			{
				command = command.substring(2);
				if (!powertools.contains(command))
				{
					throw new Exception(_("Command {0} has not been assigned to {1}.", command, itemName));
				}

				powertools = new ArrayList<String>(powertools);
				powertools.remove(command);
				user.sendMessage(_("Command {0} removed from {1}.", command, itemName));
			}
			else
			{
				if (command.startsWith("a:"))
				{
					if (!Permissions.POWERTOOL_APPEND.isAuthorized(user))
					{
						throw new Exception(_("You do not have the {0} permission.", "essentials.powertool.append"));
					}
					command = command.substring(2);
					if (powertools.contains(command))
					{
						throw new Exception(_("Command {0} is already assigned to {1}.", command, itemName));
					}
					powertools = new ArrayList<String>(powertools);
				}
				else
				{
					powertools = new ArrayList<String>();
				}

				powertools.add(command);
				user.sendMessage(_("{0} command assigned to {1}.", Util.joinList(powertools), itemName));
			}
		}
		else
		{
			powertools = new ArrayList<String>();
			user.sendMessage(_("All commands removed from {0}.", itemName));
		}

		if (!userData.isPowerToolsEnabled())
		{
			userData.setPowerToolsEnabled(true);
			user.sendMessage(_("All of your power tools have been enabled."));
		}
		userData.setPowertool(itemStack.getType(), powertools);
		user.queueSave();
	}
}
