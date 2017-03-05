package it.albertus.jface;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.albertus.util.logging.LoggerFactory;

public class JFaceMessages {

	private static final Logger logger = LoggerFactory.getLogger(JFaceMessages.class);

	private static final String BASE_NAME = JFaceMessages.class.getName().toLowerCase();

	private static ResourceBundle resources = ResourceBundle.getBundle(BASE_NAME, ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));

	// Instantiation not permitted.
	private JFaceMessages() {
		throw new IllegalAccessError();
	}

	public static String getLanguage() {
		final String language = resources.getLocale().getLanguage();
		if (!language.isEmpty()) {
			return language;
		}
		else {
			return Locale.ENGLISH.getLanguage(); // Default.
		}
	}

	/**
	 * Switch the language for JFaceUtils messages.
	 *
	 * @param language lowercase two-letter ISO-639 code.
	 *
	 * @exception NullPointerException thrown if argument is null.
	 *
	 * @see java.util.Locale#Locale(String)
	 */
	public static void setLanguage(final String language) {
		if (language != null) {
			resources = ResourceBundle.getBundle(BASE_NAME, new Locale(language), ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
		}
	}

	public static String get(final String key, final Object... params) {
		final List<String> stringParams = new ArrayList<String>(params.length);
		for (final Object param : params) {
			stringParams.add(String.valueOf(param));
		}
		String message;
		try {
			message = MessageFormat.format(resources.getString(key), stringParams.toArray());
		}
		catch (final MissingResourceException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			message = key;
		}
		return message != null ? message.trim() : "";
	}

}
