package net.ess3.converter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.Validate;


public class EnumConverter<E extends Enum<E>> implements ArgumentsParser<E>, Serializer<E>
{
	private static final Map<Class, EnumConverter> converterMap = new HashMap<Class, EnumConverter>();
	private final Map<String, E> enumMap = new HashMap<String, E>();
	private final Map<E, String> serializedMap;

	private EnumConverter(final Class<E> enumClass)
	{
		serializedMap = new EnumMap<E, String>(enumClass);
		for (E t : enumClass.getEnumConstants())
		{
			enumMap.put(t.name().replaceAll("[_-]", "").toLowerCase(Locale.ENGLISH), t);
			serializedMap.put(t, t.name().replace("_", "-").toLowerCase(Locale.ENGLISH));
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
		final E e = enumMap.get(args[0].replaceAll("[_-]", "").toLowerCase(Locale.ENGLISH));
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
}
