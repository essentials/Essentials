package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import org.bukkit.Server;


public class Commandvanish extends EssentialsCommand
{
	public Commandvanish()
	{
		super("vanish");
	}

	@Override
	protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		vanishOtherPlayers(server, sender, args);
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && args[0].trim().length() > 2 && user.isAuthorized("essentials.vanish.others"))
		{
			vanishOtherPlayers(server, user, args);
			return;
		}
			user.toggleVanished();
			if (user.isVanished())
			{
				user.sendMessage(_("vanished"));
			}
			else
			{
				user.sendMessage(_("unvanished"));
			}
	}

	private void godPlayer(User player, boolean enabled)
	{
		player.setGodModeEnabled(enabled);
		if (enabled && player.getHealth() != 0)
		{
			player.setHealth(player.getMaxHealth());
			player.setFoodLevel(20);
		}
	}

	private void vanishOtherPlayers(final Server server, final CommandSender sender, final String[] args) throws NotEnoughArgumentsException
	{
		boolean skipHidden = sender instanceof Player && !ess.getUser(sender).isAuthorized("essentials.vanish.interact");
		boolean foundUser = false;
		final List<Player> matchedPlayers = server.matchPlayer(args[0]);
		for (Player matchPlayer : matchedPlayers)
		{
			final User player = ess.getUser(matchPlayer);
			if (skipHidden && player.isHidden())
			{
				continue;
			}
			foundUser = true;
			user.toggleVanished();
			if (user.isVanished())
			{
				player.sendMessage(_("vanished"));
				//TODO: TL this
				sender.sendMessage("You have vanished " + player.getDisplayName());
				
			}
			else
			{
				player.sendMessage(_("unvanished"));
				//TODO: TL this
				sender.sendMessage("You have unvanished " + player.getDisplayName());
			}
		}
		if (!foundUser)
		{
			throw new NotEnoughArgumentsException(_("playerNotFound"));
		}
	}
}
