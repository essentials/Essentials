
import java.util.Random;
import net.ess3.api.IUser;
import net.ess3.commands.EssentialsCommand;
import net.ess3.extra.AnnotatedCommand;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;


@AnnotatedCommand(description = "Throw an exploding kitten at your opponent", usage = "/<command>")
public class Commandkittycannon extends EssentialsCommand
{
	private static Random random = new Random();

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final EntityType cat = EntityType.OCELOT;
		final Ocelot ocelot = (Ocelot)user.getPlayer().getWorld().spawn(user.getPlayer().getEyeLocation(), cat.getEntityClass());
		if (ocelot == null)
		{
			return;
		}
		final int i = random.nextInt(Ocelot.Type.values().length);
		ocelot.setCatType(Ocelot.Type.values()[i]);
		ocelot.setTamed(true);
		ocelot.setVelocity(user.getPlayer().getEyeLocation().getDirection().multiply(2));
		ess.getPlugin().scheduleSyncDelayedTask(new Runnable()
		{
			@Override
			public void run()
			{
				final Location loc = ocelot.getLocation();
				ocelot.remove();
				loc.getWorld().createExplosion(loc, 0F);
			}
		}, 20);
	}
}
