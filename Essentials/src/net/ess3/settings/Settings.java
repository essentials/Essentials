package net.ess3.settings;

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


@Data
@EqualsAndHashCode(callSuper = false)
public class Settings implements StorageObject
{
	@Comment(
	{
		"##########################################################",
		"+------------------------------------------------------+ #",
		"|                 General Settings                     | #",
		"+------------------------------------------------------+ #",
		"##########################################################"
	})
	private General general = new General();
	@Comment(
	{
		"##########################################################",
		"+------------------------------------------------------+ #",
		"|                  Chat Settings                       | #",
		"+------------------------------------------------------+ #",
		"##########################################################"
	})
	private Chat chat = new Chat();
	@Comment(
	{
		"##########################################################",
		"+------------------------------------------------------+ #",
		"|                 Economy Settings                     | #",
		"+------------------------------------------------------+ #",
		"##########################################################"
	})
	private Economy economy = new Economy();
	@Comment(
	{
		"##########################################################",
		"+------------------------------------------------------+ #",
		"|                 Commands Settings                    | #",
		"+------------------------------------------------------+ #",
		"##########################################################"
	})
	private Commands commands = new Commands();
	@Comment(
	{
		"##########################################################",
		"+------------------------------------------------------+ #",
		"|                  Worlds Settings                     | #",
		"+------------------------------------------------------+ #",
		"##########################################################"
	})
	@MapValueType(WorldOptions.class)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Map<String, WorldOptions> worlds;

	public WorldOptions getWorldOptions(final String name)
	{
		if (worlds == null)
		{
			return new WorldOptions();
		}
		final WorldOptions options = worlds.get(name);
		return (options == null) ? new WorldOptions() : options;
	}
}
