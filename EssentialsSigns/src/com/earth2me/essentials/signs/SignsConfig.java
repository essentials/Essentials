package com.earth2me.essentials.signs;

import com.earth2me.essentials.storage.IStorageObject;
import java.util.HashMap;
import java.util.Map;

public class SignsConfig implements IStorageObject {
	private Map<String, Boolean> signs = new HashMap<String, Boolean>();

	public Map<String, Boolean> getSigns()
	{
		return signs;
	}

	public void setSigns(Map<String, Boolean> signs)
	{
		this.signs = signs;
	}
}
