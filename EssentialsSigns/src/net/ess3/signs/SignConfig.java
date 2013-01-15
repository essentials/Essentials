package net.ess3.signs;

import java.util.Collections;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class SignConfig implements StorageObject
{
	private Map<String, Boolean> signs;

	public Map<String, Boolean> getSigns()
	{
		return signs == null ? Collections.<String, Boolean>emptyMap() : Collections.unmodifiableMap(signs);
	}

	public void setSigns(final Map<String, Boolean> signs)
	{
		this.signs = signs;
	}
	@Comment(
	{
		"How many times per second can Essentials signs be interacted with.",
		"Values should be between 1-20, 20 being virtually no lag protection.",
		"Lower numbers will reduce the possiblity of lag, but may annoy players."
	})
	private int signUsesPerSecond = 4;

	public int getSignUsePerSecond()
	{

		return signUsesPerSecond > 0 ? signUsesPerSecond : 1; //TODO: This needs to be ported from 2.9
	}
}
