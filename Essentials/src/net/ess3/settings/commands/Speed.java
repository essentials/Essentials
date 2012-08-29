package net.ess3.settings.commands;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.ess3.storage.Comment;
import net.ess3.storage.StorageObject;

@Data
@EqualsAndHashCode(callSuper = false)
public class Speed implements StorageObject
{
	@Comment({"#Set the max fly speed, values range from 0.1 to 1.0"})
	private float maxFlySpeed = 1.0f;
	
	/*@Comment({"#Set the max walk speed, values range from 0.1 to 1.0"})
	private float maxWalkSpeed = 0.8f;*/
}
