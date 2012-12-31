package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandspeed extends EssentialsCommand
{

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		final boolean isFly = isFlyMode(args[0]);
		final float speed = getMoveSpeed(args[1]);
		speedOtherPlayers(server, sender, isFly, true, speed, args[2]);
	}

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		boolean isFly;
		double speed;
		boolean isBypass = Permissions.SPEED_BYPASS.isAuthorized(user);
		if (args.length == 1)
		{
			//isFly = user.isFlying();
			isFly = true;
			speed = getMoveSpeed(args[0]);
		}
		else
		{
			//isFly = isFlyMode(args[0]);
			//speed = getMoveSpeed(args[1]);
			//if (args.length > 2 && user.isAuthorized("essentials.speed.others"))
			//{
			//	speedOtherPlayers(server, user, isFly, isBypass, speed, args[2]);
			//	return;
			//}
			isFly = true;
			speed = getMoveSpeed(args[0]);
			if (Permissions.SPEED_OTHERS.isAuthorized(user))
			{
				speedOtherPlayers(server, user, isFly, isBypass, speed, args[1]);
				return;
			}
		}

		//if (isFly)
		//{
		user.getPlayer().setFlySpeed((float)getRealMoveSpeed(speed, isFly, isBypass));
		user.sendMessage(_("moveSpeed", _("flying"), speed, user.getPlayer().getDisplayName()));
		//}
		//else
		//{
		//	user.setWalkSpeed(getRealMoveSpeed(speed, isFly, isBypass));
		//	user.sendMessage(_("moveSpeed", _("walking"), speed, user.getDisplayName()));
		//}
	}

	private void speedOtherPlayers(final Server server, final CommandSender sender, final boolean isFly, final boolean isBypass, final double speed, final String target)
	{
		for (Player matchPlayer : server.matchPlayer(target))
		{
			if (isFly)
			{
				matchPlayer.setFlySpeed((float)getRealMoveSpeed(speed, isFly, isBypass));
				sender.sendMessage(_("moveSpeed", _("flying"), speed, matchPlayer.getDisplayName()));
			}
			//else
			//	{
			//		matchPlayer.setWalkSpeed(getRealMoveSpeed(speed, isFly, isBypass));
			//		sender.sendMessage(_("moveSpeed", _("walking"), speed, matchPlayer.getDisplayName()));
			//	}
		}
	}

	private boolean isFlyMode(final String modeString) throws NotEnoughArgumentsException
	{
		final boolean isFlyMode;
		if (modeString.contains("fly") || modeString.equalsIgnoreCase("f"))
		{
			isFlyMode = true;
		}
		else if (modeString.contains("walk") || modeString.contains("run") || modeString.equalsIgnoreCase("w") || modeString.equalsIgnoreCase("r"))
		{
			isFlyMode = false;
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}
		return isFlyMode;
	}

	private float getMoveSpeed(final String moveSpeed) throws NotEnoughArgumentsException
	{
		float userSpeed;
		try
		{
			userSpeed = Float.parseFloat(moveSpeed);
			if (userSpeed > 10f)
			{
				userSpeed = 10f;
			}
			else if (userSpeed < 0f)
			{
				userSpeed = 0f;
			}
		}
		catch (NumberFormatException e)
		{
			throw new NotEnoughArgumentsException();
		}
		return userSpeed;
	}

	private double getRealMoveSpeed(final double userSpeed, final boolean isFly, final boolean isBypass)
	{
		final double defaultSpeed = isFly ? 0.1f : 0.2f;
		double maxSpeed = 1f;
		if (!isBypass)
		{
			maxSpeed = ess.getSettings().getData().getCommands().getSpeed().getMaxFlySpeed();// : ess.getSettings().ess.getSettings().getData().getCommands().getSpeed()getMaxWalkSpeed());
		}

		if (userSpeed < 1f)
		{
			return defaultSpeed * userSpeed;
		}
		else
		{
			double ratio = ((userSpeed - 1) / 9) * (maxSpeed - defaultSpeed);
			return ratio + defaultSpeed;
		}
	}
}
