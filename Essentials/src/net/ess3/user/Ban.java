package net.ess3.user;

import net.ess3.storage.StorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Ban implements StorageObject
{
	private String reason;
	private long timeout;
}
