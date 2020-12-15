package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
@FunctionalInterface
public interface INamed {

	@Nonnull
	@CheckReturnValue
	String getName();

	@CheckReturnValue
	static boolean matches(@Nonnull Object object, @Nonnull String name) {
		if (object instanceof INamed) {
			if (((INamed) object).getName().equalsIgnoreCase(name))
				return true;
		}
		if (object instanceof IAlias) {
			for (String alias : ((IAlias) object).getAlias()) {
				if (alias.equalsIgnoreCase(name))
					return true;
			}
		}
		return false;
	}

}
