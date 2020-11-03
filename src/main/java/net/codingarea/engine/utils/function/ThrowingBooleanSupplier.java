package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

import java.util.function.BooleanSupplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public interface ThrowingBooleanSupplier extends BooleanSupplier {

	boolean getThrowing() throws Exception;

	@Override
	default boolean getAsBoolean() {
		try {
			return getThrowing();
		} catch (Exception ex) {
			throw new ConsumeException(ex);
		}
	}
}
