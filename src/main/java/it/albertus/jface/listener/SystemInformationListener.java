package it.albertus.jface.listener;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import it.albertus.jface.SystemInformationDialog;
import it.albertus.util.logging.LoggerFactory;

public class SystemInformationListener implements Listener, SelectionListener {

	private static final Logger logger = LoggerFactory.getLogger(SystemInformationListener.class);

	private final IShellProvider provider;

	public SystemInformationListener(final IShellProvider provider) {
		this.provider = provider;
	}

	@Override
	public void widgetSelected(final SelectionEvent se) {
		openDialog();
	}

	@Override
	public void handleEvent(final Event event) {
		openDialog();
	}

	@Override
	public void widgetDefaultSelected(final SelectionEvent e) {/* Ignore */}

	private void openDialog() {
		Map<String, String> properties = null;
		try {
			properties = new TreeMap<String, String>();
			for (final Entry<?, ?> entry : System.getProperties().entrySet()) {
				if (entry != null) {
					properties.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
				}
			}
		}
		catch (final SecurityException e) {
			logger.log(Level.FINE, e.toString(), e);
		}
		Map<String, String> env = null;
		try {
			env = new TreeMap<String, String>();
			for (final Entry<String, String> entry : System.getenv().entrySet()) {
				if (entry != null) {
					env.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
				}
			}
		}
		catch (final SecurityException e) {
			logger.log(Level.FINE, e.toString(), e);
		}

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
