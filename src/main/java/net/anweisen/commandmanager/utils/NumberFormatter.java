package net.anweisen.commandmanager.utils;

import java.text.DecimalFormat;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public interface NumberFormatter {

	String format(double value);

	default String format(long value) {
		return format((double) (value));
	}

	public static final NumberFormatter
			DEFAULT = fromPattern("0.##", ""),
			PERCENTAGE = fromPattern("0.##", "%"),
			MIDDLE_NUMBER = fromPattern("###,###,###,###,###,###,###,###,###,###,###,##0.#", ""),
			BIG_NUMBER = value -> {

				DecimalFormat format = new DecimalFormat("0.###");
				double divide;
				String ending = "";

				// Normal number
				if (value < 1000) {
					divide = 1;
					format = new DecimalFormat("0.#");
					// Thousand
				} else if (value < 1000000) {
					divide = 1000;
					ending = "k";
					// Million
				} else if (value < 1000000000) {
					divide = 1000000;
					ending = "m";
					// Billion
				} else {
					divide = 1000000000;
					ending = "b";
				}

				value /= divide;
				return format.format(value) + ending;

			};

	public static NumberFormatter fromPattern(String pattern, String ending) {
		return value -> new DecimalFormat(pattern).format(value) + ending;
	}

}
