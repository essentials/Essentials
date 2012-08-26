package net.ess3.bukkit;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import static net.ess3.I18n._;
import org.bukkit.entity.EntityType;


public class LivingEntities
{
	final private static Map<String, EntityType> entities = new HashMap<String, EntityType>();
	
	final private static EnumMap<EntityType, String> entityI18n = new EnumMap<EntityType, String>(EntityType.class);
	final private static EnumMap<EntityType, String> entityI18nPlural = new EnumMap<EntityType, String>(EntityType.class);

	static
	{
		for (EntityType entityType : EntityType.values())
		{
			if (entityType.isAlive() && entityType.isSpawnable())
			{
				String entityName = entityType.name().toLowerCase(Locale.ENGLISH).replace("_", "");
				entities.put(entityName, entityType);
				entityI18n.put(entityType, entityName);
				entityI18nPlural.put(entityType,entityName+"Plural");
			}
		}
	}

	public static Set<String> getLivingEntityList()
	{
		return Collections.unmodifiableSet(entities.keySet());
	}

	public static EntityType fromName(final String name)
	{
		return entities.get(name.toLowerCase(Locale.ENGLISH));
	}

	public static String getName(int count, EntityType type) {
		return count == 1? _(entityI18n.get(type)):_(entityI18nPlural.get(type));
	}
}
