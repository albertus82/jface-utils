package it.albertus.jface.preference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.preference.page.IPageDefinition;
import it.albertus.util.IOUtils;

public class Preferences {

	private final IPreferencesCallback preferencesCallback;
	private final IPageDefinition[] pageDefinitions;
	private final IPreference[] preferences;
	private final Image[] images;

	private final PreferenceManager preferenceManager;
	private final PreferenceStore preferenceStore;

	private String dialogTitle = JFaceMessages.get("lbl.preferences.title");
	private boolean restartRequired = false;

	public Preferences(final IPageDefinition[] pageDefinitions, final IPreference[] preferences, final IPreferencesCallback preferencesCallback) {
		this(pageDefinitions, preferences, preferencesCallback, null);
	}

	public Preferences(final IPageDefinition[] pageDefinitions, final IPreference[] preferences, final IPreferencesCallback preferencesCallback, final Image[] images) {
		this.preferencesCallback = preferencesCallback;
		this.pageDefinitions = pageDefinitions;
		this.preferences = preferences;
		this.images = images;
		this.preferenceManager = createPreferenceManager();
		this.preferenceStore = createPreferenceStore();
	}

	public int openDialog(final Shell parentShell) {
		return openDialog(parentShell, null);
	}

	public int openDialog(final Shell parentShell, final IPageDefinition selectedPage) {
		// Load configuration file...
		FileInputStream configurationInputStream = null;
		try {
			configurationInputStream = new FileInputStream(preferencesCallback.getFileName());
			if (configurationInputStream != null) {
				preferenceStore.load(configurationInputStream); // buffered internally
			}
		}
		catch (final FileNotFoundException fnfe) {/* Ignore */}
		catch (final IOException ioe) {
			throw new RuntimeException(ioe);
		}
		finally {
			IOUtils.closeQuietly(configurationInputStream);
		}

		final PreferenceDialog preferenceDialog = new ConfigurationDialog(parentShell, preferenceManager, dialogTitle, images);

		preferenceDialog.setPreferenceStore(preferenceStore);

		if (selectedPage != null) {
			preferenceDialog.setSelectedNode(selectedPage.getNodeId());
		}

		final Map<String, String> configurationBackup = new HashMap<String, String>();
		for (final IPreference preference : preferences) {
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
				ioe.printStackTrace();
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

	protected PreferenceStore createPreferenceStore() {
		final String fileName = preferencesCallback.getFileName();
		final PreferenceStore store = new ConfigurationStore(fileName);

		// Set default values...
		for (final IPreference preference : preferences) {
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
			final PreferenceNode preferenceNode = new ConfigurationNode(page, preferences, preferencesCallback);
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

	public IPreferencesCallback getPreferencesCallback() {
		return preferencesCallback;
	}

	public IPageDefinition[] getPageDefinitions() {
		return pageDefinitions;
	}

	public IPreference[] getPreferences() {
		return preferences;
	}

	public Image[] getImages() {
		return images;
	}

	public PreferenceManager getPreferenceManager() {
		return preferenceManager;
	}

	public PreferenceStore getPreferenceStore() {
		return preferenceStore;
	}

}
