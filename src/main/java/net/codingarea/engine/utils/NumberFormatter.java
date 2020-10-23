package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.function.Consumer;

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
			DEFAULT = fromPattern("0.##", null, false),
			SPACE_SPLIT = fromPattern("###,##0.###############", null, false,
									  init -> updateSymbols(init, symbols -> symbols.setGroupingSeparator(' '))),
			FLOATING_POINT = fromPattern("0.0", null, false),
			BIG_FLOATING_POINT = fromPattern("###,##0.00000", null, false),
			PERCENTAGE = fromPattern("0.##", "%", true),
			FLOATING_PERCENTAGE = fromPattern("0.00", "%", true),
			MIDDLE_NUMBER = fromPattern("###,###,##0.#", null, false),

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

				return ((years > 0 ? years + "y " : "")
					  + (days > 0 ? days + "d " : "")
					  + (hours > 0 ? hours + "h " : "")
					  + (minutes > 0 ? minutes + "m " : "")
					  + (seconds > 0 || (years == 0 && days == 0 && hours == 0 && minutes == 0) ? seconds + "s" : "")).trim();

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

				return ((years > 0 ? years + "y " : "")
				  	  + (days > 0 ? days + "d " : "")
					  + (hours > 0 ? hours + "h " : "")
					  + (minutes > 0 || (years == 0 && days == 0 && hours == 0) ? minutes + "m " : "")).trim();

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
				// Billion (Milliarde)
				} else if (value < 1000000000000D) {
					divide = 1000000000;
					ending = "b";
				// Trillion (Billion)
				} else {
					divide = 1000000000000D;
					ending = "t";
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
			 * gigabyte, terrabyte, petabyte
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
				} else if (value < 1000000000000000L) {
					divide = 1000000000000L;
					ending = "TB";
				// PetaByte
				} else {
					divide = 1000000000000000L;
					ending = "PB";
				}

				value /= divide;
				return format.format(value) + ending;

			},
			ORDINAL = value -> {

				String string = String.valueOf(((long) value));
				byte number = NumberConversions.toByte(string.substring(string.length() - 1));
				String ending = "th";

				if (value != 11 && value != 12 && value != 13) {
					switch (number) {
						case 1:
							ending = "st";
							break;
						case 2:
							ending = "nd";
							break;
						case 3:
							ending = "rd";
							break;
					}
				}

				return string + ending;

			},
			GERMAN_ORDINAL = fromPattern("0", ".", false);

	@Nonnull
	@CheckReturnValue
	public static NumberFormatter fromPattern(@Nonnull String pattern, String ending, boolean positive) {
		return fromPattern(pattern, ending, positive, null);
	}

	@Nonnull
	@CheckReturnValue
	public static NumberFormatter fromPattern(@Nonnull String pattern, String ending, boolean positive, Consumer<? super DecimalFormat> init) {
		DecimalFormat format = new DecimalFormat(pattern);
		if (init != null) init.accept(format);
		return value -> Double.isNaN(value) ? "NaN" : format.format(positive ? (value > 0 ? value : 0) : value) + (ending != null ? ending : "");
	}

	@Nonnull
	@CheckReturnValue
	public static DecimalFormatSymbols updateSymbols(@Nonnull DecimalFormatSymbols symbols, @Nonnull Consumer<? super DecimalFormatSymbols> action) {
		action.accept(symbols);
		return symbols;
	}

	@CheckReturnValue
	public static void updateSymbols(@Nonnull DecimalFormat format, @Nonnull Consumer<? super DecimalFormatSymbols> action) {
		format.setDecimalFormatSymbols(updateSymbols(format.getDecimalFormatSymbols(), action));
	}

}
