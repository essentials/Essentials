package net.ess3.converter;


public interface ParserResult<T>
{
	T getValue();

	String[] getUnusedInput();
}
