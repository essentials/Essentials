package com.earth2me.essentials.components.users;

import com.earth2me.essentials.storage.IStorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Ban implements IStorageObject
{
	private String reason;
	private long timeout;
}
