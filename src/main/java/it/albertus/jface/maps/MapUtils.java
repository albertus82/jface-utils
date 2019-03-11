package it.albertus.jface.maps;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MapUtils {

	private static final ThreadLocal<DecimalFormat> coordinateFormats = new ThreadLocal<DecimalFormat>() {
		@Override
		protected DecimalFormat initialValue() {
			final DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
			decimalFormatSymbols.setDecimalSeparator('.');
			final DecimalFormat coordinateFormat = new DecimalFormat();
			coordinateFormat.setDecimalFormatSymbols(decimalFormatSymbols);
			coordinateFormat.setMaximumFractionDigits(2);
			coordinateFormat.setMinimumFractionDigits(2);
			coordinateFormat.setGroupingUsed(false);
			return coordinateFormat;
		}
	};

	public static DecimalFormat getCoordinateFormat() {
		return coordinateFormats.get();
	}

	private MapUtils() {
		throw new IllegalAccessError("Utility class");
	}

}
