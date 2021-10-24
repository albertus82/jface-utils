package it.albertus.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

	private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

	@Override
	public Thread newThread(final Runnable r) {
		final Thread thread = defaultThreadFactory.newThread(r);
		thread.setDaemon(true);
		return thread;
	}

}
