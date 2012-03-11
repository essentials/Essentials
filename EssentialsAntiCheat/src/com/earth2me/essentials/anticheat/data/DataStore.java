package com.earth2me.essentials.anticheat.data;

import com.earth2me.essentials.anticheat.DataItem;
import java.util.HashMap;
import java.util.Map;


public class DataStore
{
	private final Map<String, DataItem> dataMap = new HashMap<String, DataItem>();
	private final Statistics statistics = new Statistics();
	private final long timestamp = System.currentTimeMillis();

	@SuppressWarnings("unchecked")
	public <T extends DataItem> T get(String id)
	{
		return (T)dataMap.get(id);
	}

	public void set(String id, DataItem data)
	{
		dataMap.put(id, data);
	}

	public Map<String, Object> collectData()
	{
		Map<String, Object> map = statistics.get();
		map.put("nocheat.starttime", timestamp);
		map.put("nocheat.endtime", System.currentTimeMillis());

		return map;
	}

	public Statistics getStatistics()
	{
		return statistics;
	}
}
