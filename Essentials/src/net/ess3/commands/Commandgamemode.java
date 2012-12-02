package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;


public class Commandgamemode extends EssentialsSettingsCommand
{

	protected void setValue(final IUser player, GameMode value)
	{
		if (value == null) {
			value = rotateGameMode(player);
		}
		player.getPlayer().setGameMode(value);
	}

	protected GameMode getValue(final IUser player)
	{
		return player.getPlayer().getGameMode();
	}

	protected void informSender(final CommandSender sender, final boolean value, final IUser player)
	{
		if (value) {
			sender.sendMessage( _("gameMode", getValue(player).toString().toLowerCase(Locale.ENGLISH), player.getPlayer().getDisplayName()));
		}
		else {
			//TODO: TL this
			sender.sendMessage("Can't change game mode for player " + player.getName());
		}
	}

	protected void informPlayer(final IUser player)
	{
		final String message = _("gameMode", getValue(player).toString().toLowerCase(Locale.ENGLISH), player.getPlayer().getDisplayName());
		player.sendMessage(message);
	}

	protected boolean canToggleOthers(final IUser user)
	{
		return Permissions.GAMEMODE_OTHERS.isAuthorized(user);
	}

	protected boolean isExempt(final CommandSender sender, final IUser player)
	{
		return Permissions.GAMEMODE_EXEMPT.isAuthorized(player);
	}

	protected boolean toggleOfflinePlayers()
	{
		return false;
	}

	protected boolean canMatch(final String arg)
	{
		try {
			matchGameMode(arg);
			return true;
		}
		catch (NotEnoughArgumentsException ex) {
			return false;
		}
	}

	protected void playerMatch(final IUser player, final String arg) throws NotEnoughArgumentsException
	{
		if (arg == null)
		{
			setValue(player, null);
		}
		else {
			setValue(player, matchGameMode(arg));
		}
	}

	private GameMode rotateGameMode(final IUser player) {
		return getValue(player) == GameMode.SURVIVAL ? GameMode.CREATIVE : getValue(player) == GameMode.CREATIVE ? GameMode.ADVENTURE : GameMode.SURVIVAL;
	}

	private GameMode matchGameMode(String modeString) throws NotEnoughArgumentsException
	{
		GameMode mode = null;
		if (modeString.equalsIgnoreCase("gmc") || modeString.equalsIgnoreCase("egmc")
			|| modeString.contains("creat") || modeString.equalsIgnoreCase("1") || modeString.equalsIgnoreCase("c"))
		{
			mode = GameMode.CREATIVE;
		}
		else if (modeString.equalsIgnoreCase("gms") || modeString.equalsIgnoreCase("egms")
				 || modeString.contains("survi") || modeString.equalsIgnoreCase("0") || modeString.equalsIgnoreCase("s"))
		{
			mode = GameMode.SURVIVAL;
		}
		else if (modeString.equalsIgnoreCase("gma") || modeString.equalsIgnoreCase("egma")
				 || modeString.contains("advent") || modeString.equalsIgnoreCase("2") || modeString.equalsIgnoreCase("a"))
		{
			mode = GameMode.ADVENTURE;
		}
		else if (modeString.equalsIgnoreCase("gmt") || modeString.equalsIgnoreCase("egmt")
				 || modeString.contains("toggle") || modeString.contains("cycle") || modeString.equalsIgnoreCase("t"))
		{
			mode = null;
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}
		return mode;
	}

}
