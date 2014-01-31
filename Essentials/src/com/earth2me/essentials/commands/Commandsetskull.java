package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.LocationUtil;

import java.util.Locale;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;


public class Commandsetskull extends EssentialsCommand
{
	public Commandsetskull()
	{
		super("setskull");
	}
	
	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1 || args[0].length() < 2)
		{
			throw new NotEnoughArgumentsException(_("playersAvailable"));
		}
		
		final Location target = LocationUtil.getTarget(user.getBase());
		BlockState state = target.getBlock().getState();
		Skull skull = null;
		
		if(state instanceof Skull)
        {
        skull = (Skull)state;
        }
		
		if (target == null || target.getBlock().getType() != Material.SKULL || skull.getSkullType().toString() != "PLAYER")
		{			
			throw new Exception(_("skullOwnerTarget"));
		}

		try
		{
			String name = args[0];

		    if (!user.isAuthorized("essentials.skullowner." + name.toLowerCase(Locale.ENGLISH)))
		    {
			     throw new Exception(_("invalidSkullOwner"));
		    }
			final Trade charge = new Trade("sull owner-" + name, ess);
			charge.isAffordableFor(user);
			
            if(skull.setOwner(name) == false) {
				throw new Exception(_("skullOwnerError"));
			}
			skull.update();
				
			charge.charge(user);
			user.sendMessage(_("setSkullOwner", name));
		}
		catch (Exception ex)
		{
			throw new Exception(_("skullOwnerError"), ex);
		}
	}
}
