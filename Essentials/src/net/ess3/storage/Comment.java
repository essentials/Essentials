package net.ess3.storage;

import java.lang.annotation.*;


@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment
{
	String[] value() default "";
}