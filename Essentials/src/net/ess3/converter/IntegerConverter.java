package net.ess3.converter;

import org.apache.commons.lang.Validate;


public class IntegerConverter implements ArgumentsParser<Integer>, Serializer<Integer>
{
	private static final IntegerConverter INSTANCE = new IntegerConverter();

	private IntegerConverter()
	{
	}

	public static IntegerConverter getInstance()
	{
		return INSTANCE;
	}

	@Override
	public ParserResult<Integer> parse(final String... args)
	{
		Validate.notEmpty(args);
		try
		{
			final int number = Integer.parseInt(args[0]);
			return new FirstEntryParserResult<Integer>(number, args);
		}
		catch (NumberFormatException ex)
		{
			throw new IllegalArgumentException(ex);
		}
	}

	@Override
	public String serialize(final Integer input)
	{
		return input.toString();
	}
}
