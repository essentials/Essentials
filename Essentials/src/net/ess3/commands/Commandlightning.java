package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import lombok.Cleanup;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;


public class Commandlightning extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{

		IUser user = null;
		if (sender instanceof Player)
		{
			user = ess.getUser(((Player)sender));
		}
		if ((args.length < 1 || !Permissions.LIGHTNING_OTHERS.isAuthorized(user)) && user != null)
		{
			user.getWorld().strikeLightning(user.getTargetBlock(null, 600).getLocation());
			return;
		}

		if (server.matchPlayer(args[0]).isEmpty())
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

		for (Player matchPlayer : server.matchPlayer(args[0]))
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
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			if (settings.getData().getCommands().getLightning().isWarnPlayer())
			{
				matchPlayer.sendMessage(_("lightningSmited"));
			}
		}
	}
}
