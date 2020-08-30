package net.anweisen.commandmanager.listener;

import net.anweisen.commandmanager.exceptions.ListenerException;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Developed in the CommandManager project
 * on 08-01-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public interface Listener extends EventListener {

	@Override
	default void onEvent(@Nonnull GenericEvent event) {

		List<Method> methods = getMethodsAnnotatedWith(this.getClass(), EventHandler.class);
		for (Method currentMethod : methods) {

			if (currentMethod.getParameterTypes().length == 1) {
				if (paramIsOK(event.getClass(), currentMethod.getParameterTypes()[0])) {

					try {
						currentMethod.setAccessible(true);
						currentMethod.invoke(this, event);
					} catch (Throwable ex) {
						throw new ListenerException(ex.getCause());
					}

				}
			}

		}

	}

	public static List<Method> getMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		while (clazz != Object.class) {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.isAnnotationPresent(annotation)) {
					methods.add(method);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return methods;
	}

	public static boolean paramIsOK(Class<?> expected, Class<?> given) {

		if (given == null || expected == null) return false;

		while (given != Object.class) {

			if (given == expected) return true;

			if (given.getInterfaces() != null) {
				for (Class<?> currentInterface : given.getInterfaces()) {
					if (paramIsOK(expected, currentInterface)) return true;
				}
			}

			given = given.getSuperclass();
			if (given == null) return false;

		}

		return false;

	}

}
