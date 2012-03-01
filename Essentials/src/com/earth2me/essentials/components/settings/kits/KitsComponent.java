package com.earth2me.essentials.components.settings.kits;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.settings.Kit;
import com.earth2me.essentials.components.settings.Kits;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.storage.StorageComponent;
import java.util.*;
import org.bukkit.inventory.ItemStack;


public class KitsComponent extends StorageComponent<Kits, IEssentials> implements IKitsComponent
{
	public KitsComponent(final IContext context, final IEssentials plugin)
	{
		super(context, Kits.class, plugin);
	}

	@Override
	public String getContainerId()
	{
		return "kits";
	}

	@Override
	public Kit getKit(String kitName) throws IllegalStateException
	{
		acquireReadLock();
		try
		{
			if (getData().getKits() == null || kitName == null
				|| !getData().getKits().containsKey(kitName.toLowerCase(Locale.ENGLISH)))
			{
				throw new IllegalStateException(_("kitError2"));
			}
			final Kit kit = getData().getKits().get(kitName.toLowerCase(Locale.ENGLISH));
			if (kit == null)
			{
				throw new IllegalStateException(_("kitError2"));
			}
			return kit;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void sendKit(IUserComponent user, String kitName) throws Exception
	{
		final Kit kit = getKit(kitName);
		sendKit(user, kit);
	}

	@Override
	public void sendKit(IUserComponent user, Kit kit) throws Exception
	{
		final List<ItemStack> itemList = kit.getItems();
		user.giveItems(itemList, true);
	}

	@Override
	public Collection<String> getList() throws Exception
	{
		acquireReadLock();
		try
		{
			if (getData().getKits() == null)
			{
				return Collections.emptyList();
			}
			return new ArrayList<String>(getData().getKits().keySet());
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean isEmpty()
	{
		return getData().getKits().isEmpty();
	}
}
