package net.ess3.settings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.ess3.storage.Comment;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;
import net.ess3.storage.StoredLocation;
import org.bukkit.Location;


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
	@Comment(
	{
		"When we spawn for the first time, which spawnpoint do we use?",
		"Set to none if you want to use the spawn point of the world."
	})
	private String newbieSpawn = "none";
	@Comment("List of all spawnpoints")
	@MapValueType(StoredLocation.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<String, StoredLocation> spawns;
	
	public Map<String, StoredLocation> getSpawns()
	{
		return spawns == null
			   ? Collections.<String, StoredLocation>emptyMap()
			   : Collections.unmodifiableMap(spawns);
	}
	
	public void addSpawn(String name, Location location)
	{
		Map<String, StoredLocation> newspawns = new HashMap<String, StoredLocation>(getSpawns());
		newspawns.put(name, new StoredLocation(location));
		spawns = newspawns;
	}
}
