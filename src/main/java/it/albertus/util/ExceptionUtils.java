package it.albertus.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

	public static String getStackTrace(Throwable e) {
		String stackTrace = "";
		if (e != null) {
			try {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				stackTrace = sw.toString();
			}
			catch (Throwable t) {}
		}
		return stackTrace;
	}

	public static String getUIMessage(Throwable throwable) {
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

	public static String getLogMessage(Throwable throwable) {
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
