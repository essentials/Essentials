package net.ess3.settings.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Afk implements StorageObject
{
	@Comment(
			"After this timeout in seconds, the user will be set as afk.\n"
			 + "Set to -1 for no timeout.")
	private long autoAFK = 300;
	@Comment(
			"Auto-AFK Kick\n"
			 + "After this timeout in seconds, the user will be kicked from the server.\n"
			 + "Set to -1 for no timeout.")
	private long autoAFKKick = -1;
	@Comment(
			"Set this to true, if you want to freeze the player, if he is afk.\n"
			 + "Other players or monsters can't push him out of afk mode then.\n"
			 + "This will also enable temporary god mode for the afk player.\n"
			 + "The player has to use the command /afk to leave the afk mode.\n"
			 + "You have to add a message to your welcome message or help page,\n"
			 + "since the player will not get a message, if he tries to move.")
	private boolean freezeAFKPlayers = false;
	private boolean disableItemPickupWhileAfk = true;
}
