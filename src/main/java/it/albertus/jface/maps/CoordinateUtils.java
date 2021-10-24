package it.albertus.jface.maps;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Logger;

import it.albertus.util.logging.LoggerFactory;

public class CoordinateUtils {

	private static final Logger log = LoggerFactory.getLogger(CoordinateUtils.class);

	public static final String DEGREE_SIGN = "\u00B0";

	public static NumberFormat newFormatter() {
		log.fine("CoordinateUtils.newFormatter()");
		final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(false);
		return nf;
	}

	private CoordinateUtils() {
		throw new IllegalAccessError("Utility class");
	}

}
