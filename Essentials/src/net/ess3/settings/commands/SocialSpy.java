package net.ess3.settings.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.ListType;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class SocialSpy implements StorageObject
{
	public SocialSpy()
	{
		if (socialspyCommands.isEmpty())
		{
			socialspyCommands.addAll(Arrays.asList("msg", "r", "mail", "m", "t", "emsg", "tell", "er", "reply", "ereply", "email"));
		}
	}
	@ListType
	@Comment("Commands to listen for in socialspy")
	private List<String> socialspyCommands = new ArrayList<String>();
}
