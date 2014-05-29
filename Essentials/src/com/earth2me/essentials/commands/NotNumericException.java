package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n.tl;

public class NotNumericException extends Exception {

	public NotNumericException() {
		super(tl("notNumeric"));
	}

}
