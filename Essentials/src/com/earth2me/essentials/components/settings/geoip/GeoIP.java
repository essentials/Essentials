package com.earth2me.essentials.components.settings.geoip;

import com.earth2me.essentials.storage.IStorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class GeoIP implements IStorageObject
{
	private Database database = new Database();
	boolean showOnWhois = true;
	boolean showOnLogin = true;
}
