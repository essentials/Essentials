package com.earth2me.essentials.components.settings.groups;

import com.earth2me.essentials.storage.Comment;
import com.earth2me.essentials.storage.IStorageObject;
import com.earth2me.essentials.storage.MapValueType;
import java.util.LinkedHashMap;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Groups implements IStorageObject
{
	public Groups()
	{
		Group defaultOptions = new Group();
		groups.put("default", defaultOptions);
	}
	@Comment(
	{
		"The order of the groups matters, the groups are checked from top to bottom.",
		"All group names have to be lower case.",
		"The groups can be connected to users using the permission essentials.groups.groupname"
	})
	@MapValueType(Group.class)
	private LinkedHashMap<String, Group> groups = new LinkedHashMap<String, Group>();
}
