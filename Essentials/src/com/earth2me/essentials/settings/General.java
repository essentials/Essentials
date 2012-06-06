package com.earth2me.essentials.settings;

import com.earth2me.essentials.storage.Comment;
import com.earth2me.essentials.storage.StorageObject;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class General implements StorageObject
{
	@Comment("Backup runs a command while saving is disabled")
	private Backup backup = new Backup();
	@Comment("You can disable the death messages of minecraft.")
	private boolean deathMessages = true;
	@Comment("Turn this on, if you want to see more error messages, if something goes wrong.")
	private boolean debug = false;
	@Comment(
	
	
	{
		"Set the locale here, if you want to change the language of Essentials.",
		"If this is not set, Essentials will use the language of your computer.",
		"Available locales: da, de, en, fr, nl"
	})
	private String locale;
	@Comment(
	
	
	{
		"The number of items given, if the quantity parameter is left out in /item or /give.",
		"If this number is below 1, the maximum stack size size is given. If oversized stacks",
		"is not enabled, any number higher then the maximum stack size results in more than one stack."
	})
	private int defaultStacksize = -1;
	@Comment(
	
	
	{
		"Oversized stacks are stacks that ignore the normal max stacksize.",
		"They can be obtained using /give and /item, if the player has essentials.oversizedstacks permission.",
		"How many items should be in a oversized stack?"
	})
	private int oversizedStacksize = 64;


	public enum GroupStorage
	{
		FILE, GROUPMANAGER, VAULT
	}
	@Comment(
	
	
	{
		"Sets the place where group options should be stored:",
		" FILE: Options are stored inside groups.yml in the Essentials folder",
		" GROUPMANAGER: Options are stored using the GroupManager groups",
		" VAULT: Options are stored using a permissions plugin supported by Vault"
	})
	private GroupStorage groupStorage = GroupStorage.FILE;
	@Comment(
			
	{
		"The delay, in seconds, a player can't be attacked by other players after he has been teleported by a command",
		"This will also prevent that the player can attack other players"
	})
	private long teleportInvulnerability = 0;

	public long getTeleportInvulnerability()
	{
		return teleportInvulnerability * 1000;
	}
	
		@Comment(
	{
		"Set to true to enable per-world permissions for teleporting between worlds with essentials commands",
		"This applies to /world, /back, /tp[a|o][here|all], but not warps.",
		"Give someone permission to teleport to a world with essentials.world.<worldname>"
	})
	private boolean worldTeleportPermissions = false;		
	private boolean worldHomePermissions = false;	
	
		
    @Comment("Prevent creatures spawning")
	private Map<String, Boolean> creatureSpawn = new HashMap<String, Boolean>();
	
	public boolean getPreventSpawn(String creatureName)
	{
		if (creatureSpawn == null)
		{
			return false;
		}
		return creatureSpawn.get(creatureName);		
	}
	
	@Comment("Delay to wait before people can cause attack damage after logging in ")
	private long loginAttackDelay = 0;
	
	public long getLoginAttackDelay()
	{
		return loginAttackDelay * 1000;
	}
}
