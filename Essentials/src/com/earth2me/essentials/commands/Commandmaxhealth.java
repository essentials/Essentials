package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n.tl;

import java.math.BigDecimal;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.earth2me.essentials.User;

public class Commandmaxhealth extends EssentialsCommand {
	private double maxHealth = 20D;

	protected Commandmaxhealth() {
		super("maxhealth");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
		Player sender = user.getBase();
		if (args.length == 1) {
			this.setMaxHealth(sender, sender, args[0], true);
		} else if (args.length == 2) {
			if (user.isAuthorized("essentials.maxhealth.other")) {
				User targetUser = getPlayer(server, user, args, 1);
				if (targetUser == null) throw new PlayerNotFoundException();
				else this.setMaxHealth(sender, targetUser.getBase(), args[0], false);
			} else {
				this.setMaxHealth(sender, sender, args[0], true);
			}
		} else {
			throw new NotEnoughArgumentsException();
		}
	}

	private void setMaxHealth(final Player sender, final Player player, final String strHealth, final boolean same) throws NotNumericException {
		if (isDouble(strHealth)) {
			this.maxHealth = new BigDecimal(String.valueOf(Double.parseDouble(strHealth))).setScale(1, BigDecimal.ROUND_UP).doubleValue();
			double maxMaxHealth = this.ess.getSettings().getMaxHealth();
			if (this.maxHealth < 1D) {
				sender.sendMessage(tl("setMaxHealthTooLow"));
			} else if (this.maxHealth > maxMaxHealth) {
				sender.sendMessage(tl("setMaxHealthTooHigh", maxMaxHealth));
			} else {
				player.setMaxHealth(this.maxHealth);
				if (same) sender.sendMessage(tl("setMaxHealthSuccessSelf", this.maxHealth));
				else sender.sendMessage(tl("setMaxHealthSuccessOther", player.getName(), this.maxHealth));
			}
		} else {
			throw new NotNumericException();
		}
	}

	private static boolean isDouble(String aString) {
		try {
			Double.parseDouble(aString);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
