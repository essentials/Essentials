package net.ess3.commands;

import static net.ess3.I18n._;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData;
import net.ess3.utils.Util;


public class Commandpowertool extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		String command = getFinalArg(args, 0);
		UserData userData = user.getData();
		// check to see if this is a clear all command
		if (command != null && command.equalsIgnoreCase("d:"))
		{
			userData.clearAllPowertools();
			user.queueSave();
			user.sendMessage(_("powerToolClearAll"));
			return;
		}

		final ItemStack itemStack = user.getPlayer().getItemInHand();
		if (itemStack == null || itemStack.getType() == Material.AIR)
		{
			throw new Exception(_("powerToolAir"));
		}

		final String itemName = itemStack.getType().toString().toLowerCase(Locale.ENGLISH).replaceAll("_", " ");
		List<String> powertools = userData.getPowertool(itemStack.getType());
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

				powertools = new ArrayList<String>(powertools);
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
					powertools = new ArrayList<String>(powertools);
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
			powertools = new ArrayList<String>();
			user.sendMessage(_("powerToolRemoveAll", itemName));
		}

		if (!userData.isPowerToolsEnabled())
		{
			userData.setPowerToolsEnabled(true);
			user.sendMessage(_("powerToolsEnabled"));
		}
		userData.setPowertool(itemStack.getType(), powertools);
		user.queueSave();
	}
}
