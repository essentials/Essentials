package net.ess3.signs;

import java.util.Collections;
import java.util.Map;
import net.ess3.storage.StorageObject;


public class SignsConfig implements StorageObject
{
	private Map<String, Boolean> signs;
	
	public Map<String, Boolean> getSigns()
	{
		return signs == null
			   ? Collections.<String, Boolean>emptyMap()
			   : Collections.unmodifiableMap(signs);
	}
	
	public void setSigns(final Map<String, Boolean> signs)
	{
		this.signs = signs;
	}
}
