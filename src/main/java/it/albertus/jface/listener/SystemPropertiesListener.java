package it.albertus.jface.listener;

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

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.SystemPropertiesDialog;
import it.albertus.util.logging.LoggerFactory;

public class SystemPropertiesListener implements Listener, SelectionListener {

	private static final Logger logger = LoggerFactory.getLogger(SystemPropertiesListener.class);

	private final IShellProvider gui;

	public SystemPropertiesListener(final IShellProvider gui) {
		this.gui = gui;
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
		try {
			final Map<String, String> properties = new TreeMap<String, String>();
			for (final Entry<?, ?> entry : System.getProperties().entrySet()) {
				if (entry != null) {
					properties.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
				}
			}
			final SystemPropertiesDialog dialog = new SystemPropertiesDialog(gui.getShell(), properties);
			dialog.setText(JFaceMessages.get("lbl.system.properties.dialog.title"));
			dialog.open();
		}
		catch (final SecurityException e) {
			logger.log(Level.WARNING, e.toString(), e);
		}
	}

}
