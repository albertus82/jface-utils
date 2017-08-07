package it.albertus.jface.listener;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import it.albertus.jface.sysinfo.SystemInformationDialog;
import it.albertus.util.logging.LoggerFactory;

public class SystemInformationListener implements Listener, SelectionListener {

	private static final Logger logger = LoggerFactory.getLogger(SystemInformationListener.class);

	private final IShellProvider provider;

	public SystemInformationListener(final IShellProvider provider) {
		this.provider = provider;
	}

	@Override
	public void widgetSelected(@Nullable final SelectionEvent se) {
		openDialog();
	}

	@Override
	public void handleEvent(@Nullable final Event event) {
		openDialog();
	}

	@Override
	public void widgetDefaultSelected(@Nullable final SelectionEvent e) {/* Ignore */}

	private void openDialog() {
		@Nullable
		Map<String, String> properties = null;
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

		@Nullable
		Map<String, String> env = null;
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

		@Nullable
		List<String> jvmArgs = null;
		try {
			jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
		}
		catch (final SecurityException e) {
			logger.log(Level.FINE, e.toString(), e);
		}

		if (properties != null || env != null || jvmArgs != null) {
			new SystemInformationDialog(provider.getShell(), properties, env, jvmArgs).open();
		}
	}

}
