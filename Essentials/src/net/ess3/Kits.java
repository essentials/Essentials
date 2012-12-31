package net.ess3;

import java.io.File;
import java.util.*;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IKits;
import net.ess3.api.IUser;
import net.ess3.commands.NoChargeException;
import net.ess3.settings.Kit;
import net.ess3.storage.AsyncStorageObjectHolder;
import net.ess3.user.UserData.TimestampType;
import net.ess3.utils.DateUtil;
import org.bukkit.inventory.ItemStack;


public class Kits extends AsyncStorageObjectHolder<net.ess3.settings.Kits> implements IKits
{
	public Kits(final IEssentials ess)
	{
		super(ess, net.ess3.settings.Kits.class, new File(ess.getPlugin().getDataFolder(), "kits.yml"));
		onReload();
	}

	@Override
	public Kit getKit(String kitName) throws Exception
	{
		if (getData().getKits() == null || kitName == null
			|| !getData().getKits().containsKey(kitName.toLowerCase(Locale.ENGLISH)))
		{
			throw new Exception(_("kitError2"));
		}
		final Kit kit = getData().getKits().get(kitName.toLowerCase(Locale.ENGLISH));
		if (kit == null)
		{
			throw new Exception(_("kitError2"));
		}
		return kit;
	}

	@Override
	public void sendKit(IUser user, String kitName) throws Exception
	{
		final Kit kit = getKit(kitName);
		sendKit(user, kit);
	}

	@Override
	public void sendKit(IUser user, Kit kit) throws Exception
	{
		final List<ItemStack> itemList = kit.getItems();
		user.giveItems(itemList, true);
	}

	@Override
	public Collection<String> getList() throws Exception
	{
		if (getData().getKits() == null)
		{
			return Collections.emptyList();
		}
		return new ArrayList<String>(getData().getKits().keySet());
	}

	@Override
	public boolean isEmpty()
	{
		return getData().getKits().isEmpty();
	}

	@Override
	public void checkTime(IUser user, Kit kit) throws NoChargeException
	{
		final Calendar time = new GregorianCalendar();
		// Take the current time, and remove the delay from it.
		final double delay = kit.getDelay();
		final Calendar earliestTime = new GregorianCalendar();
		earliestTime.add(Calendar.SECOND, -(int)delay);
		earliestTime.add(Calendar.MILLISECOND, -(int)((delay * 1000.0) % 1000.0));

		// This value contains the most recent time a kit could have been used that would allow another use.

		final long earliestLong = earliestTime.getTimeInMillis();

		// When was the last kit used?
		final Long lastTime = user.getTimestamp(TimestampType.KIT);
		if (lastTime == null || lastTime < earliestLong)
		{
			user.setTimestamp(TimestampType.KIT, time.getTimeInMillis());
		}
		else if (lastTime > time.getTimeInMillis())
		{
			// This is to make sure time didn't get messed up on last kit use.
			// If this happens, let's give the user the benifit of the doubt.
			user.setTimestamp(TimestampType.KIT, time.getTimeInMillis());
		}
		else
		{
			time.setTimeInMillis(lastTime);
			time.add(Calendar.SECOND, (int)delay);
			time.add(Calendar.MILLISECOND, (int)((delay * 1000.0) % 1000.0));
			user.sendMessage(_("kitTimed", DateUtil.formatDateDiff(time.getTimeInMillis())));
			throw new NoChargeException();
		}
	}
}
