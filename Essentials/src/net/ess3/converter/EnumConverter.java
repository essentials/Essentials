package net.ess3.converter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;


public class EnumConverter<E extends Enum<E>> implements ArgumentsParser<E>, Serializer<E>
{
	private static final Map<Class, EnumConverter> converterMap = new HashMap<Class, EnumConverter>();
	private static final Pattern REPLACE = Pattern.compile("[_-]");
	private final Map<String, E> enumMap = new HashMap<String, E>();
	private final Map<E, String> serializedMap;

	private EnumConverter(final Class<E> enumClass)
	{
		serializedMap = new EnumMap<E, String>(enumClass);
		for (E t : enumClass.getEnumConstants())
		{
			enumMap.put(cleanString(t.name()), t);
			serializedMap.put(t, prettifyString(t.name()));
		}
	}

	public static <T extends Enum<T>> EnumConverter<T> getInstance(final Class<T> enumClass)
	{
		synchronized (converterMap)
		{
			EnumConverter<T> converter = converterMap.get(enumClass);
			if (converter == null)
			{
				converter = new EnumConverter<T>(enumClass);
				converterMap.put(enumClass, converter);
			}
			return converter;
		}
	}

	@Override
	public ParserResult<E> parse(final String... args)
	{
		Validate.notEmpty(args);
		Validate.notEmpty(args[0]);
		final E e = enumMap.get(cleanString(args[0]));
		if (e == null)
		{
			throw new IllegalArgumentException();
		}
		return new FirstEntryParserResult<E>(e, args);
	}

	@Override
	public String serialize(final E input)
	{
		return serializedMap.get(input);
	}
	
	private String cleanString(final String input) {
		return REPLACE.matcher(input).replaceAll("").toLowerCase(Locale.ENGLISH);
	}
	
	private String prettifyString(final String input) {
		return input.replace("_", "-").toLowerCase(Locale.ENGLISH);
	}
}
