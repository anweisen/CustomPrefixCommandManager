package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.text.DecimalFormat;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public interface NumberFormatter {

	@Nonnull
	@CheckReturnValue
	String format(double value);

	@Nonnull
	@CheckReturnValue
	default String format(long value) {
		return format((double) (value));
	}

	public static final NumberFormatter
			DEFAULT = fromPattern("0.##", "", false),
			FLOATING_POINT = fromPattern("0.0", "", false),
			PERCENTAGE = fromPattern("0.##", "%", true),
			MIDDLE_NUMBER = fromPattern("###,###,###,###,###,###,###,###,###,###,###,##0.#", "", false),

			/**
			 *  days, hours, minutes, seconds
			 */
			TIME = value -> {

				int seconds = (int) value;
				int minutes = seconds / 60;
				int hours = minutes / 60;
				int days = hours / 24;
				int years = days / 365;

				seconds %= 60;
				minutes %= 60;
				hours %= 24;
				days %= 365;

				return (years > 0 ? years + "y " : "")
					 + (days > 0 ? days + "d " : "")
					 + (hours > 0 ? hours + "h " : "")
					 + (minutes > 0 ? minutes + "m " : "")
					 + (years == 0 && days == 0 || (hours == 0 && minutes == 0) ? seconds + "s" : "");

			},

			/**
			 * days, hours, minutes
			 */
			BIG_TIME = value -> {

				int seconds = (int) value;
				int minutes = seconds / 60;
				int hours = minutes / 60;
				int days = hours / 24;
				int years = days / 365;

				minutes %= 60;
				hours %= 24;
				days %= 365;

				return (years > 0 ? years + "y " : "")
					 + (days > 0 ? days + "d " : "")
					 + (hours > 0 ? hours + "h " : "")
				     + (minutes > 0 || (years == 0 && days == 0 && hours == 0) ? minutes + "m " : "");

			},

			/**
			 *  billion, million, thousand, number
			 */
			BIG_NUMBER = value -> {

				DecimalFormat format = new DecimalFormat("0.##");
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

			},

			/**
			 * input in bytes
			 * kilobyte, megabyte, gigabyte, terrabyte
			 */
			DATA_SIZE = value -> {

				if (value < 0) value = 0;

				DecimalFormat format = new DecimalFormat("0.##");
				double divide;
				String ending;

				// KiloByte
				if (value < 1000000L) {
					divide = 1000;
					format = new DecimalFormat("0.#");
					ending = "KB";
				} else if (value < 1000000000L) {
				// MegaByte
					divide = 1000000L;
					ending = "MB";
				// GigaByte
				} else if (value < 1000000000000L) {
					divide = 1000000000L;
					ending = "GB";
				// TerraByte
				} else {
					divide = 1000000000000L;
					ending = "TB";
				}

				value /= divide;
				return format.format(value) + ending;

			},

			/**
			 * input in bytes
			 * gigabyte, terrabyte
			 */
			BIG_DATA_SIZE = value -> {

				if (value < 0) value = 0;

				DecimalFormat format = new DecimalFormat("0.##");
				double divide;
				String ending;

				// GigaByte
				if (value < 1000000000000L) {
					divide = 1000000000L;
					ending = "GB";
				// TerraByte
				} else {
					divide = 1000000000000L;
					ending = "TB";
				}

				value /= divide;
				return format.format(value) + ending;

			};

	@Nonnull
	@CheckReturnValue
	public static NumberFormatter fromPattern(@Nonnull String pattern, @Nonnull String ending, boolean plus) {
		return value -> new DecimalFormat(pattern).format(plus ? (value > 0 ? value : 0) : value) + ending;
	}

}
