package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.mcess.essentials.textreader.IText;
import org.mcess.essentials.textreader.SimpleTextInput;
import org.mcess.essentials.textreader.TextPager;
import org.mcess.essentials.utils.FormatUtil;
import org.mcess.essentials.utils.StringUtil;
import org.bukkit.Server;
import org.mcess.essentials.I18n;

import java.util.List;

import java.util.UUID;


public class Commandmail extends EssentialsCommand
{
	private static int mailsPerMinute = 0;
	private static long timestamp = 0;

	public Commandmail()
	{
		super("mail");
	}

	//TODO: Tidy this up / TL these errors.
	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length >= 1 && "read".equalsIgnoreCase(args[0]))
		{
			final List<String> mail = user.getMails();
			if (mail.isEmpty())
			{
				user.sendMessage(I18n.tl("noMail"));
				throw new NoChargeException();
			}
			
			IText input = new SimpleTextInput(mail);
			final TextPager pager = new TextPager(input);			
			pager.showPage(args.length > 1 ? args[1] : null, null, commandLabel + " " + args[0], user.getSource());
			
			user.sendMessage(I18n.tl("mailClear"));
			return;
		}
		if (args.length >= 3 && "send".equalsIgnoreCase(args[0]))
		{
			if (!user.isAuthorized("essentials.mail.send"))
			{
				throw new Exception(I18n.tl("noPerm", "essentials.mail.send"));
			}

			if (user.isMuted())
			{
				throw new Exception(I18n.tl("voiceSilenced"));
			}

			User u = getPlayer(server, args[1], true, true);
			if (u == null)
			{
				throw new Exception(I18n.tl("playerNeverOnServer", args[1]));
			}

			final String mail = I18n.tl("mailFormat", user.getName(), StringUtil.sanitizeString(FormatUtil.stripFormat(getFinalArg(args, 2))));
			if (mail.length() > 1000)
			{
				throw new Exception(I18n.tl("mailTooLong"));
			}

			if (!u.isIgnoredPlayer(user))
			{
				if (Math.abs(System.currentTimeMillis() - timestamp) > 60000)
				{
					timestamp = System.currentTimeMillis();
					mailsPerMinute = 0;
				}
				mailsPerMinute++;
				if (mailsPerMinute > ess.getSettings().getMailsPerMinute())
				{
					throw new Exception(I18n.tl("mailDelay", ess.getSettings().getMailsPerMinute()));
				}
				u.addMail(I18n.tl("mailMessage", mail));
			}

			user.sendMessage(I18n.tl("mailSentTo", u.getDisplayName(), u.getName()));
			user.sendMessage(mail);
			return;
		}
		if (args.length > 1 && "sendall".equalsIgnoreCase(args[0]))
		{
			if (!user.isAuthorized("essentials.mail.sendall"))
			{
				throw new Exception(I18n.tl("noPerm", "essentials.mail.sendall"));
			}
			ess.runTaskAsynchronously(new SendAll(I18n.tl("mailFormat", user.getName(), FormatUtil.stripFormat(getFinalArg(args, 1)))));
			user.sendMessage(I18n.tl("mailSent"));
			return;
		}
		if (args.length >= 1 && "clear".equalsIgnoreCase(args[0]))
		{
			user.setMails(null);
			user.sendMessage(I18n.tl("mailCleared"));
			return;
		}
		throw new NotEnoughArgumentsException();
	}

	@Override
	protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception
	{
		if (args.length >= 1 && "read".equalsIgnoreCase(args[0]))
		{
			throw new Exception(I18n.tl("onlyPlayers", commandLabel + " read"));
		}
		else if (args.length >= 1 && "clear".equalsIgnoreCase(args[0]))
		{
			throw new Exception(I18n.tl("onlyPlayers", commandLabel + " clear"));
		}
		else if (args.length >= 3 && "send".equalsIgnoreCase(args[0]))
		{
			User u = getPlayer(server, args[1], true, true);
			if (u == null)
			{
				throw new Exception(I18n.tl("playerNeverOnServer", args[1]));
			}
			u.addMail(I18n.tl("mailFormat", "Server", getFinalArg(args, 2)));
			sender.sendMessage(I18n.tl("mailSent"));
			return;
		}
		else if (args.length >= 2 && "sendall".equalsIgnoreCase(args[0]))
		{
			ess.runTaskAsynchronously(new SendAll(I18n.tl("mailFormat", "Server", getFinalArg(args, 1))));
			sender.sendMessage(I18n.tl("mailSent"));
			return;
		}
		else if (args.length >= 2)
		{
			//allow sending from console without "send" argument, since it's the only thing the console can do
			User u = getPlayer(server, args[0], true, true);
			if (u == null)
			{
				throw new Exception(I18n.tl("playerNeverOnServer", args[0]));
			}
			u.addMail(I18n.tl("mailFormat", "Server", getFinalArg(args, 1)));
			sender.sendMessage(I18n.tl("mailSent"));
			return;
		}
		throw new NotEnoughArgumentsException();
	}


	private class SendAll implements Runnable
	{
		String message;

		public SendAll(String message)
		{
			this.message = message;
		}

		@Override
		public void run()
		{
			for (UUID userid : ess.getUserMap().getAllUniqueUsers())
			{
				User user = ess.getUserMap().getUser(userid);
				if (user != null)
				{
					user.addMail(message);
				}
			}
		}
	}
}
