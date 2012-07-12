package net.ess3.settings.geoip;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Database implements StorageObject
{
	private boolean showCities = false;
	private boolean downloadIfMissing = true;
	private String downloadUrlCity = "http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz";
	private String downloadUrl = "http://geolite.maxmind.com/download/geoip/database/GeoLiteCountry/GeoIP.dat.gz";
}
