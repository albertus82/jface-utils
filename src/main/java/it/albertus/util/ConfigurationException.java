package it.albertus.util;

public class ConfigurationException extends IllegalArgumentException {

	private static final long serialVersionUID = -1878141960506568155L;

	private final String key;

	public ConfigurationException(final String message, final Throwable cause, final String key) {
		super(message, cause);
		this.key = key;
	}

	public ConfigurationException(final String message, final String key) {
		super(message);
		this.key = key;
	}

	public ConfigurationException(final Throwable cause, final String key) {
		super(cause);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
