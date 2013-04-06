package net.ess3.settings.protect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;


@Data
@EqualsAndHashCode(callSuper = false)
public class Prevent implements StorageObject
{
	private boolean lavaFlow = false;
	private boolean waterFlow = false;
	// private boolean waterbucketFlow = false; TODO: Test if this still works
	private boolean firespread = true;
	private boolean lavaFirespread = true;
	private boolean lightningFirespread = true;
	private boolean portalCreation = false;
	private boolean tntBlockdamage = false;
	private boolean tntPlayerdamage = false;
	private boolean tntMinecartBlockdamage = false;
	private boolean tntMinecartPlayerdamage = false;
	private boolean fireballBlockdamage = false;
	private boolean fireballPlayerdamage = false;
	private boolean fireballFire = false;
	private boolean creeperBlockdamage = false;
	private boolean creeperPlayerdamage = false;
	private boolean enderdragonBlockdamage = false;
	private boolean witherSpawnBlockdamage = false;
	private boolean witherSpawnPlayerdamage = false;
	private boolean witherskullBlockdamage = false;
	private boolean witherskullPlayerdamage = false;
	private boolean witherBlockreplace = false;
	private boolean endermanPickup = false;
	private boolean villagerDeath = false;
	@Comment(
			"Monsters won't follow players\n"
			 + "permission essentials.protect.entitytarget.bypass disables this")
	private boolean entitytarget = false;
}
