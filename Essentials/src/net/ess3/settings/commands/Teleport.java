package net.ess3.settings.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Teleport implements StorageObject
{
	@Comment(
	{
		"Set timeout in seconds for players to accept tpa before request is cancelled.", "Set to 0 for no timeout."
	})
	private int requestTimeout = 120;
	@Comment(
	{
		"Cancels a request made by tpa / tphere on world change to prevent cross world tp"
	})
	private boolean cancelRequestsOnWorldChange = false;
	@Comment(
	{
		"The delay, in seconds, a player can't be attacked by other players after he has been teleported by a command",
		"This will also prevent that the player can attack other players"
	})
	private long invulnerability = 0;

	public long getInvulnerability()
	{
		return invulnerability * 1000;
	}
}
