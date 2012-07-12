package net.ess3.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Ban implements StorageObject
{
	private String reason;
	private long timeout;
}
