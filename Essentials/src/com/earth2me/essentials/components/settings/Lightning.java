package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.storage.Comment;
import com.earth2me.essentials.storage.IStorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Lightning implements IStorageObject
{
	@Comment("Shall we notify users when using /lightning")
	private boolean warnPlayer = true;
}
