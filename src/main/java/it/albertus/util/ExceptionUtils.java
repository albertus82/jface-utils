package it.albertus.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

	private ExceptionUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static String getStackTrace(final Throwable e) {
		if (e != null) {
			StringWriter sw = null;
			PrintWriter pw = null;
			try {
				sw = new StringWriter();
				pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				return sw.toString();
			}
			catch (final Throwable t) {/* Ignore */}
			finally {
				IOUtils.closeQuietly(pw, sw);
			}
		}
		return "";
	}

	public static String getUIMessage(final Throwable throwable) {
		String message = "";
		if (throwable != null) {
			if (StringUtils.isNotBlank(throwable.getLocalizedMessage())) {
				message = throwable.getLocalizedMessage();
			}
			else if (StringUtils.isNotBlank(throwable.getMessage())) {
				message = throwable.getMessage();
			}
			else {
				message = throwable.getClass().getSimpleName();
			}

			if (StringUtils.isNotBlank(message)) {
				message = message.trim();
				if (!message.endsWith(".")) {
					message += ".";
				}
			}
		}
		return message;
	}

	public static String getLogMessage(final Throwable throwable) {
		String message = "";
		if (throwable != null) {
			message = throwable.getClass().getSimpleName();
			if (StringUtils.isNotBlank(throwable.getLocalizedMessage()) || StringUtils.isNotBlank(throwable.getMessage())) {
				message += ": " + (StringUtils.isNotBlank(throwable.getLocalizedMessage()) ? throwable.getLocalizedMessage() : throwable.getMessage());
			}

			if (StringUtils.isNotBlank(message)) {
				message = message.trim();
				if (!message.endsWith(".")) {
					message += ".";
				}
			}
		}
		return message;
	}

}
