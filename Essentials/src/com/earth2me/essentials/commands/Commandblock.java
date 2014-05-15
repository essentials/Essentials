package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n.tl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.earth2me.essentials.User;

public class Commandblock extends EssentialsCommand {
	private Map<Material, Material> blockMaterials = new HashMap<Material, Material>();
	private Map<Material, Short> blockData = new HashMap<Material, Short>();
	private Map<Material, Short> blockOldData = new HashMap<Material, Short>();

	protected Commandblock() {
		super("block");

		Iterator<Recipe> recipeIter = Bukkit.getServer().recipeIterator();
		List<Recipe> recipeList = new ArrayList<Recipe>();
		while (recipeIter.hasNext())
			recipeList.add(recipeIter.next());
		for (Recipe recipe : recipeList) {
			if (recipe instanceof ShapedRecipe) {
				ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
				if (shapedRecipe.getResult() != null && shapedRecipe.getResult().getType() != Material.AIR && shapedRecipe.getResult().getType().isBlock()) {
					Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();
					if (ingredientMap != null) {
						Material firstMaterial = Material.AIR;
						short firstData = 0;
						boolean isFirst = true;
						int sameSize = 0;
						for (Map.Entry<Character, ItemStack> ingredientEntry : ingredientMap.entrySet()) {
							if (ingredientEntry.getValue() != null && ingredientEntry.getValue().getType() != Material.AIR) {
								ItemStack itemStack = ingredientEntry.getValue();
								if (isFirst) {
									firstMaterial = itemStack.getType();
									firstData = itemStack.getDurability();
									isFirst = false;
								} else {
									if (itemStack.getType() == firstMaterial && (firstMaterial.isBlock() ? itemStack.getDurability() == firstData : true)) sameSize++;
								}
							} else {
								break;
							}
						}
						if (sameSize == 8) {
							if (firstMaterial != Material.AIR) {
								blockMaterials.put(firstMaterial, shapedRecipe.getResult().getType());
								blockData.put(firstMaterial, shapedRecipe.getResult().getDurability());
								if (firstData != 0) blockOldData.put(firstMaterial, firstData);
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
		if (args.length == 0) {
			Player player = user.getBase();
			boolean converted = false;
			List<ItemStack> unconvertedItems = new ArrayList<ItemStack>();
			Map<Integer, ItemStack> spareItems = new HashMap<Integer, ItemStack>();
			for (ItemStack itemStack : player.getInventory().getContents()) {
				if (itemStack != null) {
					Material itemType = itemStack.getType();
					int blockAmount = (int) (itemStack.getAmount() / 9);
					int leftOver = itemStack.getAmount() % 9;
					short oldData = itemStack.getDurability();
					if (this.blockMaterials.containsKey(itemType) && this.blockData.containsKey(itemType)) {
						Material newType = this.blockMaterials.get(itemType);
						if (newType != null) {
							if (this.blockOldData.containsKey(itemType)) {
								if (oldData != this.blockOldData.get(itemType)) continue;
							}
							if (blockAmount > 0) {
								player.getInventory().remove(itemStack);
								short blockData = this.blockData.get(itemType);
								spareItems.putAll(player.getInventory().addItem(new ItemStack(newType, blockAmount, blockData)));
								if (!converted) converted = true;
								if (leftOver > 0) unconvertedItems.add(new ItemStack(itemType, leftOver, oldData));
							}
						}
					}
				}
			}
			for (ItemStack itemStack : unconvertedItems) {
				if (itemStack != null) {
					spareItems.putAll(player.getInventory().addItem(itemStack));
				}
			}
			for (ItemStack itemStack : spareItems.values()) {
				if (itemStack != null) {
					player.getWorld().dropItem(player.getLocation(), itemStack);
				}
			}
			if (!spareItems.isEmpty()) {
				player.sendMessage(tl("itemsInventoryFull"));
			}
			if (converted) {
				player.sendMessage(tl("itemsConverted"));
			} else {
				player.sendMessage(tl("itemsNotConverted"));
			}
			player.updateInventory();
		} else {
			throw new NotEnoughArgumentsException();
		}
	}
}
