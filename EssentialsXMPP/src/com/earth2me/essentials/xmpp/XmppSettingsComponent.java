/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.earth2me.essentials.xmpp;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.storage.StorageComponent;


public final class XmppSettingsComponent extends StorageComponent<XmppSettings>
{
	public XmppSettingsComponent(IContext context)
	{
		super(context, XmppSettings.class);
	}

	@Override
	public String getContainerId()
	{
		return "xmpp";
	}
}
