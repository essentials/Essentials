package net.ess3.utils.textreader;

import java.util.*;


public class SimpleTextInput implements IText
{
	private final transient List<String> lines = new ArrayList<String>();
	
	public SimpleTextInput (final String input) {
		lines.add(input);
	}
		
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
