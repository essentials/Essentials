
	package com.earth2me.essentials.signs;

	import com.earth2me.essentials.ChargeException;
	import static com.earth2me.essentials.I18n._;
	import com.earth2me.essentials.IEssentials;
	import com.earth2me.essentials.Trade;
	import com.earth2me.essentials.User;


	public class SignTime extends EssentialsSign
	{
		public SignTime()
		{
			super("Time");
		}

		@Override
		protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
		{
			validateTrade(sign, 2, ess);
			final String timeString = sign.getLine(1);
			if ("Day".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Day"));
				return true;
			}
			else if ("Night".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Night"));
				return true;
			}
			else if ("Dawn".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Dawn"));
				return true;
			}
			else if ("Sunrise".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Sunrise"));
				return true;
			}
			else if ("Morning".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Morning"));
				return true;
			}
			else if ("Midday".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Midday"));
				return true;
			}
			else if ("Noon".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Noon"));
				return true;
			}
			else if ("Afternoon".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Afternoon"));
				return true;
			}
			else if ("Sunset".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Sunset"));
				return true;
			}
			else if ("Dusk".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Dusk"));
				return true;
			}
			else if ("Sundown".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Sundown"));
				return true;
			}
			else if ("Nightfall".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Nightfall"));
				return true;
			}
			else if ("Midnight".equalsIgnoreCase(timeString))
			{
				sign.setLine(1, _("signFormatSuccess", "Midnight"));
				return true;
			}
			else
		      {
		        sign.setLine(1, _("signFormatFail", timeString));
		        return true;
		      }
		}

		@Override
		protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException
		{
			final Trade charge = getTrade(sign, 2, ess);
			charge.isAffordableFor(player);
			final String timeString = sign.getLine(1);
			long time = player.getWorld().getTime();
			time -= time % 24000;
			if (_("signFormatSuccess", "Day").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 24000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Night").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 38000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Sunrise").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 47000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Dawn").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 37700);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Morning").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 25000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Midday").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 30000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Noon").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 30000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Afternoon").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 33000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Sunset").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 36000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Dusk").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 36000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Sundown").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 36000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Nightfall").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 36000);
				charge.charge(player);
				return true;
			}
			else if (_("signFormatSuccess", "Midnight").equalsIgnoreCase(timeString))
			{
				player.getWorld().setTime(time + 42000);
				charge.charge(player);
				return true;
			}
			else
		      {
		        sign.setLine(1, _("signFormatFail", timeString));
		        return true;
		      }
		}
	}
}
