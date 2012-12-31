package net.ess3.utils.textreader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ArrayListInput implements IText
{
	private final transient List<String> lines = new ArrayList<String>();

	@Override
	public List<String> getLines()
	{
		return lines;
	}

	@Override
	public List<String> getChapters()
	{
		return Collections.emptyList();
	}

	@Override
	public Map<String, Integer> getBookmarks()
	{
		return Collections.emptyMap();
	}

}
