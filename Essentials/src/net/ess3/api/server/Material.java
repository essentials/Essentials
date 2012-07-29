package net.ess3.api.server;

public abstract class Material {
	
	private static Material instance;
	
	public static void setInstance(final Material instance) {
		Material.instance = instance;
	}
	
	public static Material get(final int id) {
		return instance.getMaterialById(id);
	}
	
	public static Material get(final String name) {
		return instance.getMaterialByName(name);
	}

	public static Material match(String string)
	{
		return instance.matchMaterial(string);
	}

	protected abstract Material getMaterialByName(String name);
	
	protected abstract Material getMaterialById(int id);

	public abstract int getId();
	
	public abstract String getName();

	public abstract int getMaxStackSize();

	public abstract int getMaxDurability();

	protected abstract Material matchMaterial(String string);

	public abstract ItemStack convertToItem(ItemStack is);
}
