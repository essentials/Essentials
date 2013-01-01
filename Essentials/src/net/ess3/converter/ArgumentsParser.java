package net.ess3.converter;


public interface ArgumentsParser<T>
{
	ParserResult<T> parse(String... args);
}
