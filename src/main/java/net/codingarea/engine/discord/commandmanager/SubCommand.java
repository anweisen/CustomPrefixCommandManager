package net.codingarea.engine.discord.commandmanager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

	/**
	 * Represents the name and aliases for the {@link SubCommand SubCommand} <br>
	 * If the value is left at default value, the {@link Method#getName() MethodName} is used
	 *
	 * @return The names for the {@link SubCommand SubCommand}
	 */
	String[] name() default {};

	/**
	 * The syntax which should be displayed, when wrong arguments are used
	 * If empty, we will create one our selves
	 *
	 * @return The syntax
	 */
	String syntax() default "";

}
