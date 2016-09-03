package it.albertus.jface.preference;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.preference.page.IPageDefinition;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

public class Preferences {

	private final IPreferencesCallback preferencesCallback;
	private final IPageDefinition[] pageDefinitions;
	private final IPreference[] preferences;
	private final Image[] images;

	private String dialogTitle = JFaceMessages.get("lbl.preferences.title");
	private PreferenceDialog preferenceDialog;
	private boolean restartRequired = false;

	public Preferences(final IPageDefinition[] pageDefinitions, final IPreference[] preferences, final IPreferencesCallback preferencesCallback) {
		this(pageDefinitions, preferences, preferencesCallback, null);
	}

	public Preferences(final IPageDefinition[] pageDefinitions, final IPreference[] preferences, final IPreferencesCallback preferencesCallback, final Image[] images) {
		this.preferencesCallback = preferencesCallback;
		this.pageDefinitions = pageDefinitions;
		this.preferences = preferences;
		this.images = images;
	}

	public int openDialog(final Shell parentShell) {
		return openDialog(parentShell, null);
	}

	public int openDialog(final Shell parentShell, final IPageDefinition selectedPage) {
		final PreferenceManager preferenceManager = new PreferenceManager();

		// Pages creation...
		final Map<IPageDefinition, PreferenceNode> preferenceNodes = new HashMap<IPageDefinition, PreferenceNode>();
		for (final IPageDefinition page : pageDefinitions) {
			final PreferenceNode preferenceNode = new ConfigurationNode(page, preferences, preferencesCallback);
			if (page.getParent() != null) {
				preferenceNodes.get(page.getParent()).add(preferenceNode);
			}
			else {
				preferenceManager.addToRoot(preferenceNode);
			}
			preferenceNodes.put(page, preferenceNode);
		}

		final PreferenceStore preferenceStore = new PreferenceStore(preferencesCallback.getFileName());

		// Set default values...
		for (final IPreference preference : preferences) {
			if (preference.getDefaultValue() != null) {
				preferenceStore.setDefault(preference.getName(), preference.getDefaultValue());
			}
		}

		// Load configuration file...
		InputStream configurationInputStream = null;
		try {
			configurationInputStream = new BufferedInputStream(new FileInputStream(preferencesCallback.getFileName()));
			if (configurationInputStream != null) {
				preferenceStore.load(configurationInputStream);
			}
		}
		catch (final FileNotFoundException fnfe) {/* Ignore */}
		catch (final IOException ioe) {
			throw new RuntimeException(ioe);
		}
		finally {
			try {
				configurationInputStream.close();
			}
			catch (final Exception e) {/* Ignore */}
		}

		preferenceDialog = new ConfigurationDialog(parentShell, preferenceManager, dialogTitle, images);

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

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(final String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public boolean isRestartRequired() {
		return restartRequired;
	}

	protected void setRestartRequired(final boolean restartRequired) {
		this.restartRequired = restartRequired;
	}

	protected PreferenceDialog getPreferenceDialog() {
		return preferenceDialog;
	}

	protected IPreferencesCallback getPreferencesCallback() {
		return preferencesCallback;
	}

	protected IPageDefinition[] getPageDefinitions() {
		return pageDefinitions;
	}

	protected IPreference[] getPreferences() {
		return preferences;
	}

	protected Image[] getImages() {
		return images;
	}

}
