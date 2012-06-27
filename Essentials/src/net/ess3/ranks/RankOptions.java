package net.ess3.ranks;

import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class RankOptions implements StorageObject
{
	@Comment("Message format of chat messages")
	private String messageFormat;
	@Comment("Prefix for name")
	private String prefix;
	@Comment("Suffix for name")
	private String suffix;
	@Comment("Amount of homes a player can have")
	private Integer homes;
	@Comment("Cooldown between teleports")
	private Integer teleportCooldown;
	@Comment("Delay before teleport")
	private Integer teleportDelay;
	@Comment("Cooldown between heals")
	private Integer healCooldown;
}
