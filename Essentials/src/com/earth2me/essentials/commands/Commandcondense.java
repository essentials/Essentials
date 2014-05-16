package com.earth2me.essentials.commands;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.Trade.OverflowType;
import com.earth2me.essentials.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;


public class Commandcondense extends EssentialsCommand {
	
	public Commandcondense() {
		super("condense");
	}
	
	private Map<ItemStack, SimpleRecipe> condenseList = new HashMap<ItemStack, SimpleRecipe>();

	@SuppressWarnings("deprecation")
	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
		Player player = user.getBase();
		List<ItemStack> is = new ArrayList<ItemStack>();

		boolean validateReverse = false;
		if (args.length > 0) {
			is = this.ess.getItemDb().getMatching(user, args);
		} else {
			for (ItemStack stack : player.getInventory().getContents()) {
				if (stack == null || stack.getType() == Material.AIR) {
					continue;
				}
				is.add(stack);
			}
			validateReverse = true;
		}

		for (final ItemStack itemStack : is) {
			condenseStack(user, itemStack, validateReverse);
		}
		player.updateInventory();
	}

	private void condenseStack(final User user, final ItemStack stack, final boolean validateReverse) throws ChargeException, MaxMoneyException {
		final SimpleRecipe condenseType = getCondenseType(stack);
		if (condenseType != null) {
			final ItemStack input = condenseType.getInput();
			final ItemStack result = condenseType.getResult();

			if (validateReverse) {
				boolean pass = false;
				for (Recipe revRecipe : this.ess.getServer().getRecipesFor(input)) {
					if (getStackOnRecipeMatch(revRecipe, result) != null) {
						pass = true;
						break;
					}
				}
				if (!pass) return;
			}

			int amount = 0;

			for (final ItemStack contents : user.getBase().getInventory().getContents()) {
				if (contents != null && contents.isSimilar(stack)) {
					amount += contents.getAmount();
				}
			}

			int output = ((amount / input.getAmount()) * result.getAmount());
			amount -= amount % input.getAmount();

			if (amount > 0) {
				input.setAmount(amount);
				result.setAmount(output);
				new Trade(input, this.ess).charge(user);
				new Trade(result, this.ess).pay(user, OverflowType.DROP);
			}
		}
	}

	private SimpleRecipe getCondenseType(final ItemStack stack) {
		if (this.condenseList.containsKey(stack)) {
			return this.condenseList.get(stack);
		}

		final Iterator<Recipe> intr = this.ess.getServer().recipeIterator();
		while (intr.hasNext()) {
			final Recipe recipe = intr.next();
			final Collection<ItemStack> recipeItems = getStackOnRecipeMatch(recipe, stack);

			if (recipeItems != null && (recipeItems.size() == 4 || recipeItems.size() == 9) && (recipeItems.size() > recipe.getResult().getAmount())) {
				final ItemStack input = stack.clone();
				input.setAmount(recipeItems.size());
				SimpleRecipe newRecipe = new SimpleRecipe(recipe.getResult(), input);
				this.condenseList.put(stack, newRecipe);
				return newRecipe;
			}
		}
		this.condenseList.put(stack, null);
		return null;
	}

	private Collection<ItemStack> getStackOnRecipeMatch(final Recipe recipe, final ItemStack stack) {
		final Collection<ItemStack> inputList;

		if (recipe instanceof ShapedRecipe) {
			ShapedRecipe sRecipe = (ShapedRecipe) recipe;
			inputList = sRecipe.getIngredientMap().values();
		} else if (recipe instanceof ShapelessRecipe) {
			ShapelessRecipe slRecipe = (ShapelessRecipe) recipe;
			inputList = slRecipe.getIngredientList();
		} else {
			return null;
		}

		boolean match = true;
		Iterator<ItemStack> iter = inputList.iterator();
		while (iter.hasNext()) {
			ItemStack inputSlot = iter.next();
			if (inputSlot == null) {
				iter.remove();
				continue;
			}

			if (inputSlot.getDurability() == Short.MAX_VALUE) {
				inputSlot.setDurability((short) 0);
			}
			if (!inputSlot.isSimilar(stack)) {
				match = false;
			}
		}

		if (match) return inputList;
		else return null;
	}

	private class SimpleRecipe implements Recipe {
		private ItemStack result;
		private ItemStack input;

		private SimpleRecipe(ItemStack result, ItemStack input) {
			this.result = result;
			this.input = input;
		}

		@Override
		public ItemStack getResult() {
			return this.result.clone();
		}

		public ItemStack getInput() {
			return this.input.clone();
		}
	}
}
