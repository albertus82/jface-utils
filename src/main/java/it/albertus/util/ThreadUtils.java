package it.albertus.util;

public class ThreadUtils {

	public static InterruptedException sleep(long millis) {
		try {
			Thread.sleep(millis);
			return null;
		}
		catch (InterruptedException ie) {
			return ie;
		}
	}

}
