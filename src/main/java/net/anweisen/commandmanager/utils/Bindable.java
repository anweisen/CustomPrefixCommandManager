package net.anweisen.commandmanager.utils;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Developed in the CommandManager project
 * on 09-05-2020
 *
 * @see StaticBinder
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public interface Bindable {

	/**
	 * Binds <code>this</code> to the key using the {@link StaticBinder}
	 * @return returns this
	 */
	default <T> T bind(String key) {
		StaticBinder.set(key, this);
		return (T) this;
	}

	/**
	 * Binds <code>this</code> to the key using the {@link StaticBinder}
	 * @return returns this
	 */
	default <T> T bind(long key) {
		return bind(String.valueOf(key));
	}

	/**
	 * Binds <code>this</code> to the key using the {@link StaticBinder}
	 * @return returns this
	 */
	default <T> T bind(double key) {
		return bind(String.valueOf(key));
	}

	/**
	 * Binds <code>this</code> to the key using the {@link StaticBinder}
	 * @return returns this
	 */
	default <T> T bind(char key) {
		return bind(String.valueOf(key));
	}

	default <T> T bind(@Nonnull Class<T> key) {
		return bind(key.getName());
	}

	@Nonnull
	default <T> T bindToClass() {
		return bind(this.getClass().getName());
	}

	default Collection<String> keys() {
		return StaticBinder.keys(this);
	}

	/**
	 * @return to how many keys this object was bound before
	 * @see Bindable#bindings
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
	default int bindings() {
		return keys().size();
	}

	default boolean isBound() {
		return !keys().isEmpty();
	}

}
