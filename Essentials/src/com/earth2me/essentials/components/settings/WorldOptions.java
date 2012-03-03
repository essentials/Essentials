package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.storage.Comment;
import com.earth2me.essentials.storage.IStorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorldOptions implements IStorageObject
{
	@Comment("Disables godmode for all players if they teleport to this world.")
	private boolean godmode = true;
}
