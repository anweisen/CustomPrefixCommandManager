package net.anweisen.commandmanager.listener;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen
 * CommandManager developed on 08-01-2020
 * https://github.com/anweisen
 */

public interface Listener extends EventListener {

	@Override
	default void onEvent(@Nonnull GenericEvent event) {

		List<Method> methods = getMethodsAnnotatedWith(this.getClass(), EventHandler.class);
		for (Method currentMethod : methods) {

			if (currentMethod.getParameterTypes().length == 1) {
				if (currentMethod.getParameterTypes()[0] == event.getClass()) {

					try {
						currentMethod.setAccessible(true);
						currentMethod.invoke(this, event);
					} catch (Exception ignored) { }

				}
			}

		}

	}

	public static List<Method> getMethodsAnnotatedWith(Class<?> clazz, final Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		while (clazz != Object.class) {
			for (final Method method : clazz.getDeclaredMethods()) {
				if (method.isAnnotationPresent(annotation)) {
					methods.add(method);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return methods;
	}

}
