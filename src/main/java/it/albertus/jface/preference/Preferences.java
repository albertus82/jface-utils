package it.albertus.jface.preference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.EnhancedErrorDialog;
import it.albertus.jface.JFaceMessages;
import it.albertus.jface.preference.page.IPageDefinition;
import it.albertus.util.IOUtils;
import it.albertus.util.logging.LoggerFactory;

public class Preferences {

	private static final Logger logger = LoggerFactory.getLogger(Preferences.class);

	private final IPreferencesCallback preferencesCallback;
	private final IPageDefinition[] pageDefinitions;
	private final IPreference[] preferenceItems;
	private final Image[] images;

	private final PreferenceManager preferenceManager;
	private final PreferenceStore preferenceStore;

	private String dialogTitle = JFaceMessages.get("lbl.preferences.title");
	private boolean restartRequired = false;

	public Preferences(final IPageDefinition[] pageDefinitions, final IPreference[] preferences, final IPreferencesCallback preferencesCallback) {
		this(pageDefinitions, preferences, preferencesCallback, null);
	}

	public Preferences(final IPageDefinition[] pageDefinitions, final IPreference[] preferenceItems, final IPreferencesCallback preferencesCallback, final Image[] images) {
		this.preferencesCallback = preferencesCallback;
		this.pageDefinitions = pageDefinitions;
		this.preferenceItems = preferenceItems;
		this.images = images;
		this.preferenceManager = createPreferenceManager();
		this.preferenceStore = createPreferenceStore();
	}

	public int openDialog(final Shell parentShell) throws IOException {
		return openDialog(parentShell, null);
	}

	public int openDialog(final Shell parentShell, final IPageDefinition selectedPage) throws IOException {
		// Load configuration file if exists...
		final File file = new File(preferencesCallback.getFileName());
		if (file.exists()) {
			loadConfigurationFile(file);
		}

		final PreferenceDialog preferenceDialog = new EnhancedPreferenceDialog(parentShell, preferenceManager, dialogTitle, images);

		preferenceDialog.setPreferenceStore(preferenceStore);

		if (selectedPage != null) {
			preferenceDialog.setSelectedNode(selectedPage.getNodeId());
		}

		final Map<String, String> configurationBackup = new HashMap<String, String>();
		for (final IPreference preference : preferenceItems) {
			if (preference.isRestartRequired()) {
				configurationBackup.put(preference.getName(), preferenceStore.getString(preference.getName()));
			}
		}

		// Open configuration dialog...
		final int returnCode = preferenceDialog.open();

		if (returnCode == Window.OK) {
			// Reload configuration (autosaved by PreferenceStore on OK button)...
			try {
				preferencesCallback.reload(); // Callback
			}
			catch (final IOException ioe) {
				final String message = JFaceMessages.get("err.preferences.reload");
				logger.log(Level.WARNING, message, ioe);
				EnhancedErrorDialog.openError(parentShell, dialogTitle, message, IStatus.WARNING, ioe, Display.getCurrent().getSystemImage(SWT.ICON_WARNING));
			}
		}

		for (final Entry<String, String> backedUpProperty : configurationBackup.entrySet()) {
			final String oldValue = backedUpProperty.getValue();
			final String newValue = preferenceStore.getString(backedUpProperty.getKey());
			if ((oldValue != null && newValue == null) || (oldValue == null && newValue != null) || (oldValue != null && !oldValue.equals(newValue))) {
				restartRequired = true;
				break;
			}
		}

		return returnCode;
	}

	private void loadConfigurationFile(final File file) throws IOException {
		FileInputStream configurationInputStream = null;
		try {
			configurationInputStream = new FileInputStream(file);
			preferenceStore.load(configurationInputStream); // buffered internally
		}
		finally {
			IOUtils.closeQuietly(configurationInputStream);
		}
	}

	protected PreferenceStore createPreferenceStore() {
		final String fileName = preferencesCallback.getFileName();
		final PreferenceStore store = new ConfigurationStore(fileName);

		// Set default values...
		for (final IPreference preference : preferenceItems) {
			if (preference.getDefaultValue() != null) {
				store.setDefault(preference.getName(), preference.getDefaultValue());
			}
		}

		return store;
	}

	protected PreferenceManager createPreferenceManager() {
		final PreferenceManager manager = new PreferenceManager();

		// Pages creation...
		final Map<IPageDefinition, PreferenceNode> preferenceNodes = new HashMap<IPageDefinition, PreferenceNode>();
		for (final IPageDefinition page : pageDefinitions) {
			final PreferenceNode preferenceNode = new ConfigurationNode(page, preferenceItems, preferencesCallback);
			if (page.getParent() != null) {
				preferenceNodes.get(page.getParent()).add(preferenceNode);
			}
			else {
				manager.addToRoot(preferenceNode);
			}
			preferenceNodes.put(page, preferenceNode);
		}
		return manager;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(final String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public boolean isRestartRequired() {
		return restartRequired;
	}

	public void setRestartRequired(final boolean restartRequired) {
		this.restartRequired = restartRequired;
	}

	protected IPreferencesCallback getPreferencesCallback() {
		return preferencesCallback;
	}

	protected IPageDefinition[] getPageDefinitions() {
		return pageDefinitions;
	}

	protected IPreference[] getPreferences() {
		return preferenceItems;
	}

	protected Image[] getImages() {
		return images;
	}

	protected PreferenceManager getPreferenceManager() {
		return preferenceManager;
	}

	protected PreferenceStore getPreferenceStore() {
		return preferenceStore;
	}

}
