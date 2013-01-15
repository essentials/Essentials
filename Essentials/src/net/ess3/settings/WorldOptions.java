package net.ess3.settings;

import java.util.EnumMap;
import java.util.Map;
import lombok.*;
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
		if (creatureSpawn.isEmpty())
		{
			for (EntityType t : EntityType.values())
			{
				if (t.isAlive())
				{
					creatureSpawn.put(t, false);
				}
			}
		}
	}

	@Comment("Disables godmode for all players if they teleport to this world.")
	private boolean godmode = true;
	@Comment("Prevent creatures spawning")
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<EntityType, Boolean> creatureSpawn = new EnumMap<EntityType, Boolean>(EntityType.class);

	/**
	 * Checks if a entity can be spawned.
	 *
	 * @param creatureName - Name of the {@link EntityType}
	 * @return
	 */
	public boolean getPreventSpawn(String creatureName)
	{
		return getPreventSpawn(EntityType.fromName(creatureName));
	}

	/**
	 * Checks if an {@link EntityType} is allowed to be spawned
	 *
	 * @param creature - {@link EntityType} to check
	 * @return
	 */
	public boolean getPreventSpawn(EntityType creature)
	{
		if (creatureSpawn == null)
		{
			return false;
		}
		return creatureSpawn.get(creature);
	}
}
