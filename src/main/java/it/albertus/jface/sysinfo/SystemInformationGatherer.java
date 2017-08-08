package it.albertus.jface.sysinfo;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import it.albertus.util.logging.LoggerFactory;

public class SystemInformationGatherer implements IRunnableWithProgress {

	private static final String TASK_NAME = "Gathering system information";

	private static final Logger logger = LoggerFactory.getLogger(SystemInformationGatherer.class);

	/** The map contaning the system properties (can be null). */
	@Nullable
	Map<String, String> properties;

	/** The map containing the environment variables (can be null). */
	@Nullable
	Map<String, String> env;

	/** The list containing the JVM arguments (can be null). */
	@Nullable
	List<String> jvmArgs;

	@Override
	public void run(final IProgressMonitor monitor) {
		monitor.beginTask(TASK_NAME, 3);
		try {
			final Properties systemProperties = System.getProperties();
			properties = new TreeMap<String, String>();
			for (final Entry<?, ?> entry : systemProperties.entrySet()) {
				if (entry != null) {
					properties.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
				}
			}
		}
		catch (final SecurityException e) {
			logger.log(Level.FINE, e.toString(), e);
		}
		monitor.worked(1);

		try {
			final Map<String, String> systemEnv = System.getenv();
			env = new TreeMap<String, String>();
			for (final Entry<String, String> entry : systemEnv.entrySet()) {
				if (entry != null) {
					env.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
				}
			}
		}
		catch (final SecurityException e) {
			logger.log(Level.FINE, e.toString(), e);
		}
		monitor.worked(1);

		try {
			jvmArgs = Collections.unmodifiableList(ManagementFactory.getRuntimeMXBean().getInputArguments());
		}
		catch (final SecurityException e) {
			logger.log(Level.FINE, e.toString(), e);
		}
		monitor.done();
	}

	/**
	 * Returns a map contaning the system properties (can be null).
	 * 
	 * @return a map contaning a copy of the system properties (can be null).
	 */
	@Nullable
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * Returns a map containing the environment variables (can be null).
	 * 
	 * @return a map containing a copy of the environment variables (can be
	 *         null).
	 */
	@Nullable
	public Map<String, String> getEnv() {
		return env;
	}

	/**
	 * Returns a list containing the JVM arguments (can be null).
	 * 
	 * @return an unmodifiable list containing the JVM arguments (can be null).
	 */
	@Nullable
	public List<String> getJvmArgs() {
		return jvmArgs;
	}

}
