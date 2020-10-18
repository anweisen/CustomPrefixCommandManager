package net.codingarea.engine.discord.listener;

import net.codingarea.engine.exceptions.ListenerException;
import net.codingarea.engine.utils.Utils;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public interface Listener extends EventListener {

	@Override
	default void onEvent(@Nonnull GenericEvent event) {

		List<Method> methods = Utils.getMethodsAnnotatedWith(this.getClass(), DiscordEvent.class);
		for (Method currentMethod : methods) {

			if (currentMethod.getParameterTypes().length == 1 &&
				currentMethod.getParameterTypes()[0].isAssignableFrom(event.getClass())) {

				try {
					currentMethod.setAccessible(true);
					currentMethod.invoke(this, event);
				} catch (Throwable ex) {
					throw new ListenerException(ex);
				}

			}
		}

	}

}
