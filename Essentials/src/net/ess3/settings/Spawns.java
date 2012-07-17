package net.ess3.settings;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.Location;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Spawns implements StorageObject
{
	@Comment(
	{
		"Should we announce to the server when someone logs in for the first time?",
		"If so, use this format, replacing {DISPLAYNAME} with the player name.",
		"If not, set to ''"
	})
	private String newPlayerAnnouncement = "&dWelcome {DISPLAYNAME} to the server!";
	@Comment(
	{
		"Priority of the respawn event listener",
		"Set this to lowest, if you want e.g. Multiverse to handle the respawning",
		"Set this to normal, if you want EssentialsSpawn to handle the respawning",
		"Set this to highest, if you want to force EssentialsSpawn to handle the respawning"
	})
	private String respawnPriority = "normal";
	@Comment({
		"When we spawn for the first time, which spawnpoint do we use?",
		"Set to none if you want to use the spawn point of the world."
	})
	private String newbieSpawn = "none";
	@Comment("List of all spawnpoints")
	@MapValueType(StoredLocation.class)
	private Map<String, StoredLocation> spawns = new HashMap<String, StoredLocation>();
}
