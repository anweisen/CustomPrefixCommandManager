package net.anweisen.commandmanager.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see net.anweisen.commandmanager.listener.Listener
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

}
