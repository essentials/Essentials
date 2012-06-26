package com.earth2me.essentials.settings.geoip;

import com.earth2me.essentials.storage.StorageObject;
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
