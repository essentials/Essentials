package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.storage.Comment;
import com.earth2me.essentials.storage.IStorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Back implements IStorageObject
{
	@Comment(
	{
		"Do you want essentials to keep track of previous location for /back in the teleport listener?",
		"If you set this to true any plugin that uses teleport will have the previous location registered."
	})
	private boolean registerBackInListener = false;
}
