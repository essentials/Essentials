package net.ess3.settings.geoip;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Database implements StorageObject
{
	boolean showCities = false;
	boolean downloadIfMissing = true;
	String downloadUrlCity = "http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz";
	String downloadUrl = "http://geolite.maxmind.com/download/geoip/database/GeoLiteCountry/GeoIP.dat.gz";
}
