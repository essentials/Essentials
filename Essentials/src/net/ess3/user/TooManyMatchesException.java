package net.ess3.user;

import java.util.Collections;
import java.util.Set;
import static net.ess3.I18n._;
import net.ess3.api.IUser;


public class TooManyMatchesException extends Exception
{
	private final Set<IUser> matches;

	public TooManyMatchesException()
	{
		super();
		matches = Collections.emptySet();
	}

	public TooManyMatchesException(Set<IUser> users)
	{
		super();
		this.matches = users;
	}

	@Override
	public String getMessage()
	{
		if (!matches.isEmpty())
		{
			StringBuilder builder = new StringBuilder(matches.size() * 16);
			for (IUser iUser : matches)
			{
				if (builder.length() > 0)
				{
					builder.append(", ");
				}
				builder.append(iUser.getPlayer().getDisplayName());
			}
			return _("tooManyMatchesWithList", builder.toString());
		}
		else
		{
			return _("tooManyMatches");
		}
	}
}
