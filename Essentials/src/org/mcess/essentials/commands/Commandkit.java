package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.Kit;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandkit extends EssentialsCommand
{
	public Commandkit()
	{
		super("kit");
	}
	
	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			final String kitList = Kit.listKits(ess, user);
			user.sendMessage(kitList.length() > 0 ? I18n.tl("kits", kitList) : I18n.tl("noKits"));
			throw new NoChargeException();
		}
		else if (args.length > 1 && user.isAuthorized("essentials.kit.others"))
		{
			final User userTo = getPlayer(server, user, args, 1);
			final String kitNames = StringUtil.sanitizeString(args[0].toLowerCase(Locale.ENGLISH)).trim();
			giveKits(userTo, user, kitNames);
		}
		else
		{
			final String kitNames = StringUtil.sanitizeString(args[0].toLowerCase(Locale.ENGLISH)).trim();
			giveKits(user, user, kitNames);
		}
	}
	
	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			final String kitList = Kit.listKits(ess, null);
			sender.sendMessage(kitList.length() > 0 ? I18n.tl("kits", kitList) : I18n.tl("noKits"));
			throw new NoChargeException();
		}
		else
		{
			final User userTo = getPlayer(server, args, 1, true, false);
			final String[] kits = args[0].toLowerCase(Locale.ENGLISH).split(",");
			
			for (final String kitName : kits)
			{
				final Kit kit = new Kit(kitName, ess);
				kit.expandItems(userTo);
				
				sender.sendMessage(I18n.tl("kitGiveTo", kitName, userTo.getDisplayName()));
				userTo.sendMessage(I18n.tl("kitReceive", kitName));
			}
		}
	}
	
	private void giveKits(final User userTo, final User userFrom, final String kitNames) throws Exception
	{
		if (kitNames.isEmpty())
		{
			throw new Exception(I18n.tl("kitNotFound"));
		}
		String[] kitList = kitNames.split(",");
		
		List<Kit> kits = new ArrayList<Kit>();
		
		for (final String kitName : kitList)
		{
			if (kitName.isEmpty())
			{
				throw new Exception(I18n.tl("kitNotFound"));
			}
			
			Kit kit = new Kit(kitName, ess);
			kit.checkPerms(userFrom);
			kit.checkDelay(userFrom);
			kit.checkAffordable(userFrom);
			kits.add(kit);
		}
		
		for (final Kit kit : kits)
		{
			try
			{
				
				kit.checkDelay(userFrom);
				kit.checkAffordable(userFrom);
				kit.setTime(userFrom);
				kit.expandItems(userTo);
				kit.chargeUser(userTo);
				
				if (!userFrom.equals(userTo))
				{
					userFrom.sendMessage(I18n.tl("kitGiveTo", kit.getName(), userTo.getDisplayName()));
				}
				
				userTo.sendMessage(I18n.tl("kitReceive", kit.getName()));
				
			}
			catch (NoChargeException ex)			
			{
				if (ess.getSettings().isDebug())
				{
					ess.getLogger().log(Level.INFO, "Soft kit error, abort spawning " + kit.getName(), ex);
				}
			}
			catch (Exception ex)			
			{
				ess.showError(userFrom.getSource(), ex, "\\ kit: " + kit.getName());
			}
		}
	}
}
