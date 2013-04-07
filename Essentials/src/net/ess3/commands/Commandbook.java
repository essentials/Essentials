package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;


public class Commandbook extends EssentialsCommand
{
	//TODO: Translate this
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{

		final Player player = user.getPlayer();
		final ItemStack item = player.getItemInHand();
		if (item.getType() == Material.WRITTEN_BOOK)
		{
			BookMeta bmeta = (BookMeta)item.getItemMeta();

			if (args.length > 1 && args[0].equalsIgnoreCase("author"))
			{
				if (Permissions.BOOK_AUTHOR.isAuthorized(user) && (isAuthor(bmeta, player.getName()) || Permissions.BOOK_OTHERS.isAuthorized(user)))
				{
					bmeta.setAuthor(args[1]);
					item.setItemMeta(bmeta);
					user.sendMessage(_("§6Author of the book set to {0}.", getFinalArg(args, 1)));
				}
				else
				{
					throw new Exception(_("§4You cannot change the author of this book."));
				}
			}
			else if (args.length > 1 && args[0].equalsIgnoreCase("title"))
			{
				if (Permissions.BOOK_TITLE.isAuthorized(user) && (isAuthor(bmeta, player.getName()) || Permissions.BOOK_OTHERS.isAuthorized(user)))
				{
					bmeta.setTitle(args[1]);
					item.setItemMeta(bmeta);
					user.sendMessage(_("§6Title of the book set to {0}.", getFinalArg(args, 1)));
				}
				else
				{
					throw new Exception(_("§4You cannot change the title of this book."));
				}
			}
			else
			{
				if (isAuthor(bmeta, player.getName()) || Permissions.BOOK_OTHERS.isAuthorized(user))
				{
					ItemStack newItem = new ItemStack(Material.BOOK_AND_QUILL, item.getAmount());
					newItem.setItemMeta(bmeta);
					user.getPlayer().setItemInHand(newItem);
					user.sendMessage(_("§eYou may now edit the contents of this book."));
				}
				else
				{
					throw new Exception(_("§4You cannot unlock this book."));
				}
			}
		}
		else if (item.getType() == Material.BOOK_AND_QUILL)
		{
			BookMeta bmeta = (BookMeta)item.getItemMeta();
			if (!Permissions.BOOK_AUTHOR.isAuthorized(user))
			{
				bmeta.setAuthor(player.getName());
			}
			ItemStack newItem = new ItemStack(Material.WRITTEN_BOOK, item.getAmount());
			newItem.setItemMeta(bmeta);
			player.setItemInHand(newItem);
			user.sendMessage(_("§6This book is now locked."));
		}
		else
		{
			throw new Exception(_("§4You are not holding a writable book."));
		}
	}

	private boolean isAuthor(BookMeta bmeta, String player)
	{
		String author = bmeta.getAuthor();
		return author != null && author.equalsIgnoreCase(player);
	}
}
