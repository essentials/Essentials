package com.earth2me.essentials.components.settings.users;

import com.earth2me.essentials.storage.IStorageObject;
import java.util.LinkedHashMap;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public final class Users implements IStorageObject
{
	private LinkedHashMap<String, UserSurrogate> users = new LinkedHashMap<String, UserSurrogate>();
}
