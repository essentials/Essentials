package net.ess3.converter;

import java.util.Arrays;


class FirstEntryParserResult<T> implements ParserResult<T>
{
	private final T value;
	private final String[] input;
	private String[] unusedInput = null;

	FirstEntryParserResult(T value, String... input)
	{
		this.value = value;
		this.input = input;
	}

	@Override
	public T getValue()
	{
		return value;
	}

	@Override
	public String[] getUnusedInput()
	{
		if (unusedInput == null)
		{
			unusedInput = Arrays.copyOfRange(input, input.length > 0 ? 1 : 0, input.length);
		}
		return unusedInput;
	}
}
