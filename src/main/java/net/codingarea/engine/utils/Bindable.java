package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Allows you to bind the object the call is coming from, to bind using {@link StaticBinder#set(String, Object)}
 * @see StaticBinder
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public interface Bindable {

	/**
	 * Binds <code>this</code> to the key using the {@link StaticBinder}
	 * @return This
	 */
	@Nonnull
	default <T> T bind(String key) {
		StaticBinder.set(key, this);
		return (T) this;
	}

	/**
	 * Binds <code>this</code> to the key using the {@link StaticBinder}
	 * @return This
	 */
	@Nonnull
	default <T> T bind(long key) {
		return bind(String.valueOf(key));
	}

	/**
	 * Binds <code>this</code> to the key using the {@link StaticBinder}
	 * @return This
	 */
	@Nonnull
	default <T> T bind(double key) {
		return bind(String.valueOf(key));
	}

	/**
	 * Binds <code>this</code> to the key using the {@link StaticBinder}
	 * @return This
	 */
	@Nonnull
	default <T> T bind(char key) {
		return bind(String.valueOf(key));
	}

	/**
	 * Binds <code>this</code> to the class mame as key using the {@link StaticBinder}
	 * @return This
	 */
	@Nonnull
	default <T> T bind(@Nonnull Class<T> key) {
		return bind(key.getName());
	}

	/**
	 * Binds <code>this</code> to {@link Class#getSimpleName()} as key using the {@link StaticBinder}
	 * @return This
	 */
	@Nonnull
	default <T> T bindToClass() {
		return bind(this.getClass().getName());
	}

	@Nonnull
	@CheckReturnValue
	default Collection<String> keys() {
		return StaticBinder.keys(this);
	}

	/**
	 * @return to how many keys this object was bound before
	 * @see #bindings
	 */
	default int unbind() {
		int binds = 0;
		for (String currentKey : keys()) {
			StaticBinder.remove(currentKey);
			binds++;
		}
		return binds;
	}

	/**
	 * @return to how many keys this object was bound
	 */
	@CheckReturnValue
	default int bindings() {
		return keys().size();
	}

	@CheckReturnValue
	default boolean isBound() {
		return !keys().isEmpty();
	}

}
