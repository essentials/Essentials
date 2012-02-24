package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.users.IUser;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;


public class Commandlightning extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{

		IUser user = null;
		if (sender instanceof Player)
		{
			user = getContext().getUser(((Player)sender));
		}
		if (args.length < 1 & user != null)
		{
			user.getWorld().strikeLightning(user.getTargetBlock(null, 600).getLocation());
			return;
		}

		if (getServer().matchPlayer(args[0]).isEmpty())
		{
			throw new Exception(_("playerNotFound"));
		}

		int power = 1;
		if (args.length > 1)
		{
			try
			{
				power = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException ex)
			{
			}
		}

		for (Player matchPlayer : getServer().matchPlayer(args[0]))
		{
			sender.sendMessage(_("lightningUse", matchPlayer.getDisplayName()));
			if (power <= 0)
			{
				matchPlayer.getWorld().strikeLightningEffect(matchPlayer.getLocation());
			}
			else
			{
				LightningStrike strike = matchPlayer.getWorld().strikeLightning(matchPlayer.getLocation());
				matchPlayer.damage(power - 1, strike);
			}
			if (!ess.getUser(matchPlayer).isGodModeEnabled())
			{
				matchPlayer.setHealth(matchPlayer.getHealth() < 5 ? 0 : matchPlayer.getHealth() - 5);
			}
			@Cleanup
			final ISettings settings = getContext().getSettings();
			settings.acquireReadLock();
			if (settings.getData().getCommands().getLightning().isWarnPlayer())
			{
				matchPlayer.sendMessage(_("lightningSmited"));
			}
		}
	}
}
