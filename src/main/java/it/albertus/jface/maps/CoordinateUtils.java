package it.albertus.jface.maps;

import java.text.NumberFormat;
import java.util.Locale;

public class CoordinateUtils {

	public static final String DEGREE_SIGN = "\u00B0";

	private static final ThreadLocal<NumberFormat> formats = new ThreadLocal<NumberFormat>() {
		@Override
		protected NumberFormat initialValue() {
			final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			nf.setGroupingUsed(false);
			return nf;
		}
	};

	public static NumberFormat getFormatter() {
		return formats.get();
	}

	private CoordinateUtils() {
		throw new IllegalAccessError("Utility class");
	}

}
