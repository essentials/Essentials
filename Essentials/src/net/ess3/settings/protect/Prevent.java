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
	private boolean tntExplosion = false;
	private boolean fireballExplosion = false;
	private boolean fireballFire = false;
	private boolean creeperExplosion = false;
	private boolean creeperBlockdamage = false;
	private boolean enderdragonBlockdamage = false;
	private boolean witherSpawnExplosion = false;
	private boolean witherskullExplosion = false;
	private boolean witherBlockreplace = false;
	private boolean endermanPickup = false;
	private boolean villagerDeath = false;
	@Comment(
			{
					"Monsters won't follow players", "permission essentials.protect.entitytarget.bypass disables this"
			})
	private boolean entitytarget = false;
}