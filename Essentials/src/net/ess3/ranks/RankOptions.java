package net.ess3.ranks;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class RankOptions implements StorageObject
{
	@Comment("Message format of chat messages")
	private String chatFormat;
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
