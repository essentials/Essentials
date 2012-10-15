/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 *
 * @author Janus
 */
public class Commandjailinfo extends EssentialsCommand
{
	public Commandjailinfo()
	{
		super("jailinfo");
	}
	
	@Override
	public boolean getIgnoreJailed()
	{
		return true;
	}
	
	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if ((args.length == 0) || (args.length == 1 && args[0].equalsIgnoreCase("time")))
		{
			if (sender instanceof Player
					&& !ess.getUser(sender).isAuthorized("essentials.jailinfo.time.self", true))
			{
				ess.getLogger().info("time.self.failed");
				sender.sendMessage(_("noAccessCommand"));
				return;
			}
			
			if (!(sender instanceof Player))
			{
				throw new NotEnoughArgumentsException();
			}
			
			ess.getJails().jailtimeSelf(ess.getUser(sender));
			return;
		}
		
		if ((args.length > 1) && (args[0].equalsIgnoreCase("time")))
		{
			if (sender instanceof Player && !ess.getUser(sender).isAuthorized("essentials.jailinfo.time.other"))
			{
				ess.getLogger().info("time.other.failed");
				sender.sendMessage(_("noAccessCommand"));
				return;
			}
			
			final User player = getPlayer(server, args, 1, true);
			
			if (player == null)
			{
				sender.sendMessage(_("playerNotFound"));
				return;
			}
			
			if (!player.isJailed()) 
			{
				sender.sendMessage(_("playerNotJailed", "Player " + player.getDisplayName(), "is"));
				return;
			}
			
			ess.getJails().jailtimeOther(sender, player);
			return;
		}
		
		if ((args.length > 1) && (args[0].equalsIgnoreCase("history")) && (!args[1].equalsIgnoreCase("erase")))
		{
			if (sender instanceof Player && !ess.getUser(sender).isAuthorized("essentials.jailinfo.history.view"))
			{
				ess.getLogger().info("history.view.failed");
				sender.sendMessage(_("noAccessCommand"));
				return;
			}
			
			if (!ess.getSettings().jailHistory())
			{
				sender.sendMessage(_("jailHistoryDisabled"));
				return;
			}
			
			final User player = getPlayer(server, args, 1, true);
			
			if (player == null) 
			{
				sender.sendMessage(_("playerNotFound"));
				return;
			}
			
			ess.getJails().jailhistory(sender, player);
			return;
		}
		
		if ((args.length > 2) && (args[0].equalsIgnoreCase("history")) && (args[1].equalsIgnoreCase("erase")))
		{
			if (sender instanceof Player && !ess.getUser(sender).isAuthorized("essentials.jailinfo.history.erase"))
			{
				ess.getLogger().info("history.erase.failed");
				sender.sendMessage(_("noAccessCommand"));
				return;
			}
			
			if (!ess.getSettings().jailHistory())
			{
				sender.sendMessage(_("jailHistoryDisabled"));
				return;
			}
			
			final User player = getPlayer(server, args, 2, true);
			
			if (player == null) 
			{
				sender.sendMessage(_("playerNotFound"));
				return;
			}
			
			if (args.length > 3)
			{
				final String entry = args[3];
				
				if (player.getJailHistory().contains(entry))
				{
					player.delJailHistory(entry);
					sender.sendMessage(_("jailHistorySingleErased", player.getDisplayName()));
				}
				else
				{
					sender.sendMessage(_("jailHistoryNotFound", player.getDisplayName()));
				}
				return;
			}
			else
			{
				if (player.getJailHistory().isEmpty())
				{
					sender.sendMessage(_("jailHistoryNotFound", player.getDisplayName()));
				}
				else
				{
					for (String s : player.getJailHistory())
					{
						player.delJailHistory(s);
					}
					sender.sendMessage(_("jailHistoryAllErased", player.getDisplayName()));
				}
				return;
			}
		}
		
		throw new NotEnoughArgumentsException();
	}
	
}
