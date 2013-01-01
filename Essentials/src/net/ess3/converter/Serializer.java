package net.ess3.converter;


public interface Serializer<T>
{
	String serialize(T input);
}
