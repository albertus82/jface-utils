package it.albertus.util;

public class AnnotationConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -5256745486489697183L;

	public AnnotationConfigurationException() {
		super();
	}

	public AnnotationConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public AnnotationConfigurationException(final String message) {
		super(message);
	}

	public AnnotationConfigurationException(final Throwable cause) {
		super(cause);
	}

}
