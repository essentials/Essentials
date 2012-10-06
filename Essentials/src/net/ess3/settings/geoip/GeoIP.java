package net.ess3.settings.geoip;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class GeoIP implements StorageObject
{
	private Database database = new Database();
	private boolean showOnLogin = true;
	private boolean showOnWhois = true;
}
