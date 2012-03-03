package com.earth2me.essentials.signs;

import org.bukkit.block.Block;


public interface ISign
{
	String getLine(final int index);

	void setLine(final int index, final String text);

	public Block getBlock();

	void updateSign();
}
