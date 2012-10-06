package net.ess3.settings;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;
import org.bukkit.entity.EntityType;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorldOptions implements StorageObject
{
	
	public WorldOptions()
	{
		//Populate creature spawn values
		for (EntityType t : EntityType.values())
		{
			if (t.isAlive())
			{
				creatureSpawn.put(t, false);
			}
		}
	}
	
	@Comment("Disables godmode for all players if they teleport to this world.")
	private boolean godmode = true;
	
	@Comment("Prevent creatures spawning")
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<EntityType, Boolean> creatureSpawn = new HashMap<EntityType, Boolean>();

	public boolean getPreventSpawn(String creatureName)
	{
		return getPreventSpawn(EntityType.fromName(creatureName));
	}

	public boolean getPreventSpawn(EntityType creature)
	{
		if (creatureSpawn == null)
		{
			return false;
		}
		return creatureSpawn.get(creature);
	}
}
