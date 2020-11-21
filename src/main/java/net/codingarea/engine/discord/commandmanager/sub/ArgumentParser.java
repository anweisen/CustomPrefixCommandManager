package net.codingarea.engine.discord.commandmanager.sub;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArgumentParser {

	/**
	 * @return The class the method parses
	 */
	@Nonnull
	Class<?> argument();

}
