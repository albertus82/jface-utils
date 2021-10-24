package it.albertus.jface.cocoa;

/**
 * Thrown by {@link CocoaUIEnhancer} to indicate that the UI cannot be improved
 * because of an error or exception.
 * 
 * @see CocoaUIEnhancer
 */
public class CocoaEnhancerException extends Exception {

	private static final long serialVersionUID = -8718766495662867613L;

	public CocoaEnhancerException(final Throwable cause) {
		super(cause);
	}

}
