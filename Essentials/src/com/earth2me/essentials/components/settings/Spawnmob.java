package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.storage.Comment;
import com.earth2me.essentials.storage.IStorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Spawnmob implements IStorageObject
{
	@Comment("The maximum amount of monsters, a player can spawn with a call of /spawnmob.")
	private int limit = 10;
}
