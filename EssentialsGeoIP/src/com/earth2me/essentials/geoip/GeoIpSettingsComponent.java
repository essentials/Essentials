package com.earth2me.essentials.geoip;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.settings.geoip.GeoIP;
import com.earth2me.essentials.storage.StorageComponent;

public class GeoIpSettingsComponent extends StorageComponent<GeoIP, EssentialsGeoIpPlugin>
{
	public GeoIpSettingsComponent(final IContext ess, final EssentialsGeoIpPlugin plugin)
	{
		super(ess, GeoIP.class, plugin);
	}
}
