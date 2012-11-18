package net.ess3.commands;

import static net.ess3.I18n._;

import net.ess3.api.IUser;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


public class Commanddisableplugin extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
	    	if (args.length < 1) {
	    	    throw new NotEnoughArgumentsException();
	    	}
	    	PluginManager pm = Bukkit.getServer().getPluginManager();
	        Plugin plugin = pm.getPlugin(args[0]);
	        if (plugin != null) {
	            pm.disablePlugin(plugin);
	            user.sendMessage(_("pluginDisabled"));
	        } else {
	            user.sendMessage(_("badPluginName")); 
	        }	
	}
}
