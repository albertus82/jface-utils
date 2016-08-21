package it.albertus.util;

public class ThreadUtils {

	/**
	 * Causes the currently executing thread to sleep (temporarily cease
	 * execution) for the specified number of milliseconds, subject to the
	 * precision and accuracy of system timers and schedulers. The thread does
	 * not lose ownership of any monitors.
	 *
	 * @param millis the length of time to sleep in milliseconds.
	 * @return InterruptedException if any thread has interrupted the current
	 *         thread. The <i>interrupted status</i> of the current thread is
	 *         cleared when this exception is returned.
	 */
	public static InterruptedException sleep(final long millis) {
		try {
			Thread.sleep(millis);
			return null;
		}
		catch (final InterruptedException ie) {
			return ie;
		}
	}

}
