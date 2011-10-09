package com.earth2me.essentials.commands;

import com.earth2me.essentials.Util;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandkill extends EssentialsCommand
{
	public Commandkill()
	{
		super("kill");
	}

	@Override
	public void run(Server server, CommandSender sender, String commandLabel, String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		for (Player p : server.matchPlayer(args[0]))
		{
			EntityDamageEvent ede = new EntityDamageEvent(p, EntityDamageEvent.DamageCause.CUSTOM, 1000);
            		server.getPluginManager().callEvent(ede);
            		//if (ede.isCancelled()) return;
            		
			p.setHealth(0);
			sender.sendMessage(Util.format("kill", p.getDisplayName()));
		}
	}
}
