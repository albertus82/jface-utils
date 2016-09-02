package it.albertus.jface.preference;

import it.albertus.jface.preference.page.IPageDefinition;
import it.albertus.util.IConfiguration;

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

	protected final IConfiguration preferencesCallback;
	protected final IPageDefinition[] pageDefinitions;
	protected final IPreference[] preferences;
	protected final Image[] images;

	private boolean restartRequired = false;
	private PreferenceDialog preferenceDialog;

	public Preferences(final IConfiguration preferencesCallback, final IPageDefinition[] pageDefinitions, final IPreference[] preferences) {
		this(preferencesCallback, pageDefinitions, preferences, null);
	}

	public Preferences(final IConfiguration preferencesCallback, final IPageDefinition[] pageDefinitions, final IPreference[] preferences, final Image[] images) {
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

		final PreferenceStore preferenceStore = new PreferenceStore(preferencesCallback.getFile().getPath());

		// Set default values...
		for (final IPreference preference : preferences) {
			if (preference.getDefaultValue() != null) {
				preferenceStore.setDefault(preference.getName(), preference.getDefaultValue());
			}
		}

		// Load configuration file...
		InputStream configurationInputStream = null;
		try {
			configurationInputStream = new BufferedInputStream(new FileInputStream(preferencesCallback.getFile()));
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

		preferenceDialog = new ConfigurationDialog(parentShell, preferenceManager, images);

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

	protected PreferenceDialog getPreferenceDialog() {
		return preferenceDialog;
	}

	public boolean isRestartRequired() {
		return restartRequired;
	}

	protected void setRestartRequired(final boolean restartRequired) {
		this.restartRequired = restartRequired;
	}

}
