package net.codingarea.engine.sql.helper;

import net.codingarea.engine.utils.Utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public final class Where {

	public static final String[] OPERATORS = {"=", "!=", "<>", ">", ">=", "<", "<=", "LIKE", "IN", "BETWEEN"};
	public static final String DEFAULT_OPERATOR = "=";

	private final String operator;
	private final Object value;
	private final String column;

	public Where(final @Nonnull String operator, final @Nonnull Object value, final @Nonnull String column) {
		if (!Utils.arrayContainsIgnoreCase(Where.OPERATORS, operator))
			throw new IllegalArgumentException("Unknown operator: \"" + operator + "\"");
		this.operator = operator;
		this.value = value;
		this.column = column;
	}

	@Nonnull
	@CheckReturnValue
	public String getColumn() {
		return column;
	}

	@Nonnull
	@CheckReturnValue
	public Object getValue() {
		return value;
	}

	@Nonnull
	@CheckReturnValue
	public String getOperator() {
		return operator;
	}

}
