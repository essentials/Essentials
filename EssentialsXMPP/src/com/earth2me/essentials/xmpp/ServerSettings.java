package com.earth2me.essentials.xmpp;

import com.earth2me.essentials.storage.IStorageObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ServerSettings implements IStorageObject {
	private String server = "example.com";
	private int port = 5222;
	private String servicename;
	private String user = "";
	private String password = "";
	private boolean saslEnabled = false;
}
