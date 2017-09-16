package it.albertus.jface;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.eclipse.swt.widgets.Event;

import it.albertus.util.logging.LoggerFactory;

public class Events {

	private static final String EVENT_NAMES_RESOURCE_NAME = "event-names.properties";

	private static final Logger logger = LoggerFactory.getLogger(Events.class);

	private static final Properties eventNames;

	static {
		eventNames = new Properties();
		try {
			eventNames.load(Events.class.getResourceAsStream(EVENT_NAMES_RESOURCE_NAME));
		}
		catch (final IOException e) {
			logger.log(Level.WARNING, "Unable to load resource " + EVENT_NAMES_RESOURCE_NAME, e);
		}
	}

	private Events() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Returns the event name corresponding to the provided type, as defined in
	 * {@link org.eclipse.swt.SWT} class.
	 * 
	 * @param type the event type
	 * @return the event name, or null if there's no match for the provided type
	 */
	@Nullable
	public static String getName(final int type) {
		return eventNames.getProperty(Integer.toString(type));
	}

	/**
	 * Returns the event name corresponding to the provided event object, as
	 * defined in {@link org.eclipse.swt.SWT} class.
	 * 
	 * @param event the event object
	 * @return the event name, or null if the argument is null or there's no
	 *         match for the event's type
	 */
	@Nullable
	public static String getName(@Nullable final Event event) {
		return event != null ? getName(event.type) : null;
	}

}
