package com.earth2me.essentials.components.settings.commands;


import com.earth2me.essentials.storage.Comment;
import com.earth2me.essentials.storage.IStorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class List implements IStorageObject
{
	@Comment("Sort output of /list command by groups")
	private boolean sortByGroups = true;
}
