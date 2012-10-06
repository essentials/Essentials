package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LightningStrike;


public class Commandlightning extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{

		IUser user = null;
		if (isUser(sender))
		{
			user = getUser(sender);
		}
		if ((args.length < 1 || !Permissions.LIGHTNING_OTHERS.isAuthorized(user)) && user != null)
		{
			user.getPlayer().getWorld().strikeLightning(user.getPlayer().getTargetBlock(null, 600).getLocation());
			return;
		}

		if (ess.getUserMap().matchUsers(args[0], false, false).isEmpty())
		{
			throw new Exception(_("playerNotFound"));
		}

		int power = 5;
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

		for (IUser matchPlayer : ess.getUserMap().matchUsers(args[0], false, false))
		{
			sender.sendMessage(_("lightningUse", matchPlayer.getPlayer().getDisplayName()));
			final LightningStrike strike = matchPlayer.getPlayer().getWorld().strikeLightningEffect(matchPlayer.getPlayer().getLocation());
			if (!matchPlayer.isGodModeEnabled())
			{
				matchPlayer.getPlayer().damage(power, strike);
			}
			final ISettings settings = ess.getSettings();
			if (settings.getData().getCommands().getLightning().isWarnPlayer())
			{
				matchPlayer.sendMessage(_("lightningSmited"));
			}
		}
	}
}
