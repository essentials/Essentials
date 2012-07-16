package net.ess3.api.server;


public abstract class ItemStack implements Cloneable
{
	public interface ItemStackFactory
	{
		ItemStack create(int id, int amount, int data);
		ItemStack clone(ItemStack stack);
	}
	private static ItemStackFactory factory;

	public static void setFactory(final ItemStackFactory factory)
	{
		ItemStack.factory = factory;
	}

	public static ItemStack create(int id, int amount, int data)
	{
		return factory.create(id, amount, data);
	}

	public static ItemStack create(Material mat, int amount, int data)
	{
		return factory.create(mat.getId(), amount, data);
	}

	public abstract Material getType();

	public abstract int getAmount();

	public abstract void setAmount(int value);
	
	public abstract short getDurability(); 

	public abstract int getMaxStackSize();

	public abstract boolean isAir();

	public abstract void addEnchantment(Enchantment enchantment, int level);

	@Override
	public ItemStack clone()
	{
		return factory.clone(this);
	}	
}
