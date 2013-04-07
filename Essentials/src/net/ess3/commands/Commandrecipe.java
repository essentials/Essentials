package net.ess3.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.utils.Util;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.*;


public class Commandrecipe extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final ItemStack itemType = ess.getItemDb().get(args[0]);
		int recipeNo = 0;

		if (args.length > 1)
		{
			if (Util.isInt(args[1]))
			{
				recipeNo = Integer.parseInt(args[1]) - 1;
			}
			else
			{
				throw new Exception(_("Invalid Number."));
			}
		}

		final List<Recipe> recipesOfType = ess.getServer().getRecipesFor(itemType);
		if (recipesOfType.size() < 1)
		{
			throw new Exception(_("No recipes exist for {0}", getMaterialName(itemType)));
		}

		if (recipeNo < 0 || recipeNo >= recipesOfType.size())
		{
			throw new Exception(_("There is no recipe by that number."));
		}

		final Recipe selectedRecipe = recipesOfType.get(recipeNo);
		sender.sendMessage(_("§6Recipe for §c{0}§6 ({1} of {2})", getMaterialName(itemType), recipeNo + 1, recipesOfType.size()));

		if (selectedRecipe instanceof FurnaceRecipe)
		{
			furnaceRecipe(sender, (FurnaceRecipe)selectedRecipe);
		}
		else if (selectedRecipe instanceof ShapedRecipe)
		{
			shapedRecipe(sender, (ShapedRecipe)selectedRecipe);
		}
		else if (selectedRecipe instanceof ShapelessRecipe)
		{
			shapelessRecipe(sender, (ShapelessRecipe)selectedRecipe);
		}

		if (recipesOfType.size() > 1 && args.length == 1)
		{
			sender.sendMessage(_("§6Type /{0} §c{1}§6 <number> to see other recipes for §c{2}§6.", commandLabel, args[0], getMaterialName(itemType)));
		}
	}

	public void furnaceRecipe(final CommandSender sender, final FurnaceRecipe recipe)
	{
		sender.sendMessage(_("§6Smelt §c{0}", getMaterialName(recipe.getInput())));
	}

	public void shapedRecipe(final CommandSender sender, final ShapedRecipe recipe)
	{
		final Map<Character, ItemStack> recipeMap = recipe.getIngredientMap();
		if (sender instanceof IUser)
		{
			final IUser user = (IUser)sender;
			user.setRecipeSee(true);
			final InventoryView view = user.getPlayer().openWorkbench(null, true);
			for (int j = 0; j < recipe.getShape().length; j++)
			{
				for (int k = 0; k < recipe.getShape()[j].length(); k++)
				{
					final ItemStack item = recipe.getIngredientMap().get(recipe.getShape()[j].toCharArray()[k]);
					if (item == null)
					{
						continue;
					}
					item.setAmount(0);
					view.getTopInventory().setItem(j * 3 + k + 1, item);
				}
			}
		}
		else
		{
			final HashMap<Material, String> colorMap = new HashMap<Material, String>(); //TODO: Might be better as an Enum
			int i = 1;
			for (Character c : "abcdefghi".toCharArray()) // TODO: Faster to use new char[] { 'a','b','c','d','e','f','g','h','i' } ?
			{
				final ItemStack item = recipeMap.get(c);
				if (!colorMap.containsKey(item == null ? null : item.getType()))
				{
					colorMap.put(item == null ? null : item.getType(), String.valueOf(i++));
				}
			}
			final Material[][] materials = new Material[3][3];
			for (int j = 0; j < recipe.getShape().length; j++)
			{
				for (int k = 0; k < recipe.getShape()[j].length(); k++)
				{
					final ItemStack item = recipe.getIngredientMap().get(recipe.getShape()[j].toCharArray()[k]);
					materials[j][k] = item == null ? null : item.getType();
				}
			}
			sender.sendMessage(_("§{0}X §6| §{1}X §6| §{2}X", colorMap.get(materials[0][0]), colorMap.get(materials[0][1]), colorMap.get(materials[0][2])));
			sender.sendMessage(_("§{0}X §6| §{1}X §6| §{2}X", colorMap.get(materials[1][0]), colorMap.get(materials[1][1]), colorMap.get(materials[1][2])));
			sender.sendMessage(_("§{0}X §6| §{1}X §6| §{2}X", colorMap.get(materials[2][0]), colorMap.get(materials[2][1]), colorMap.get(materials[2][2])));

			final StringBuilder s = new StringBuilder();
			for (Material items : colorMap.keySet().toArray(new Material[colorMap.size()]))
			{
				s.append(_("§{0}X §6is §c{1}", colorMap.get(items), getMaterialName(items)));
			}
			sender.sendMessage(_("§6Where: {0}", s.toString()));
		}
	}

	public void shapelessRecipe(final CommandSender sender, final ShapelessRecipe recipe)
	{
		final List<ItemStack> ingredients = recipe.getIngredientList();
		if (sender instanceof IUser)
		{
			final IUser user = getUser(sender);
			user.setRecipeSee(true);
			final InventoryView view = user.getPlayer().openWorkbench(null, true);
			for (int i = 0; i < ingredients.size(); i++)
			{
				view.setItem(i + 1, ingredients.get(i));
			}
		}
		else
		{
			final StringBuilder s = new StringBuilder();
			for (int i = 0; i < ingredients.size(); i++)
			{
				s.append(getMaterialName(ingredients.get(i)));
				if (i != ingredients.size() - 1)
				{
					s.append(",");
				}
				s.append(" ");
			}
			sender.sendMessage(_("§6Combine §c{0}", s.toString()));
		}
	}

	public String getMaterialName(final ItemStack stack)
	{
		if (stack == null)
		{
			return _("nothing");
		}
		return getMaterialName(stack.getType());
	}

	public String getMaterialName(final Material type)
	{
		if (type == null)
		{
			return _("nothing");
		}
		return type.toString().replace("_", " ").toLowerCase(Locale.ENGLISH);
	}
}
