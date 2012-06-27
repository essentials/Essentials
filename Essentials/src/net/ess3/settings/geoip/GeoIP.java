package net.ess3.settings.geoip;

import net.ess3.storage.StorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class GeoIP implements StorageObject
{
	private Database database = new Database();
	private boolean showOnWhois = true;
	private boolean showOnLogin = true;
}
