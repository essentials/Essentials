package com.earth2me.essentials.xmpp;

import com.earth2me.essentials.storage.IStorageObject;
import com.earth2me.essentials.storage.ListType;
import com.earth2me.essentials.storage.MapKeyType;
import com.earth2me.essentials.storage.MapValueType;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public final class XmppSettings implements IStorageObject
{
	private ServerSettings xmpp = new ServerSettings();
	@ListType
	private List<String> opUsers;
	
	@ListType
	private List<String> logUsers;
	private boolean logEnabled = false;
	private Level logLevel = Level.INFO;
	private boolean ignoreLagMessages = true;
}
