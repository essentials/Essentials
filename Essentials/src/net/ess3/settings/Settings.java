package net.ess3.settings;

import java.util.Map;
import lombok.*;
import net.ess3.storage.Comment;
import net.ess3.storage.MapValueType;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Settings implements StorageObject
{
	@Comment(
			"##########################################################\n"
			 + "+------------------------------------------------------+ #\n"
			 + "|                 General Settings                     | #\n"
			 + "+------------------------------------------------------+ #\n"
			 + "##########################################################")
	private General general = new General();
	@Comment(
			"##########################################################\n"
			 + "+------------------------------------------------------+ #\n"
			 + "|                  Chat Settings                       | #\n"
			 + "+------------------------------------------------------+ #\n"
			 + "##########################################################")
	private Chat chat = new Chat();
	@Comment(
			"##########################################################\n"
			 + "+------------------------------------------------------+ #\n"
			 + "|                 Economy Settings                     | #\n"
			 + "+------------------------------------------------------+ #\n"
			 + "##########################################################")
	private Economy economy = new Economy();
	@Comment(
			"##########################################################\n"
			 + "+------------------------------------------------------+ #\n"
			 + "|                 Commands Settings                    | #\n"
			 + "+------------------------------------------------------+ #\n"
			 + "##########################################################")
	private Commands commands = new Commands();
	@Comment(
			"##########################################################\n"
			 + "+------------------------------------------------------+ #\n"
			 + "|                  Worlds Settings                     | #\n"
			 + "+------------------------------------------------------+ #\n"
			 + "##########################################################")
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
