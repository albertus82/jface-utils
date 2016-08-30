package it.albertus.jface.preference;

import it.albertus.jface.preference.page.PageDefinition;
import it.albertus.util.Configuration;

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

	protected final Configuration configuration;
	protected final PageDefinition[] pages;
	protected final Preference[] preferences;
	protected final Image[] images;

	protected boolean restartRequired = false;

	public Preferences(final Configuration configuration, final PageDefinition[] pages, final Preference[] preferences) {
		this(configuration, pages, preferences, null);
	}

	public Preferences(final Configuration configuration, final PageDefinition[] pages, final Preference[] preferences, final Image[] images) {
		this.configuration = configuration;
		this.pages = pages;
		this.preferences = preferences;
		this.images = images;
	}

	public int openDialog(final Shell parentShell) {
		return openDialog(parentShell, null);
	}

	public int openDialog(final Shell parentShell, final PageDefinition selectedPage) {
		final PreferenceManager preferenceManager = new PreferenceManager();

		// Pages creation...
		final Map<PageDefinition, PreferenceNode> preferenceNodes = new HashMap<PageDefinition, PreferenceNode>();
		for (final PageDefinition page : pages) {
			final PreferenceNode preferenceNode = new PreferenceNode(page.getNodeId(), page.getLabel(), null, page.getPageClass().getName());
			if (page.getParent() != null) {
				preferenceNodes.get(page.getParent()).add(preferenceNode);
			}
			else {
				preferenceManager.addToRoot(preferenceNode);
			}
			preferenceNodes.put(page, preferenceNode);
		}

		final PreferenceStore preferenceStore = new PreferenceStore(configuration.getFile().getPath());

		// Set default values...
		for (final Preference preference : preferences) {
			if (preference.getDefaultValue() != null) {
				preferenceStore.setDefault(preference.getConfigurationKey(), preference.getDefaultValue());
			}
		}

		// Load configuration file...
		InputStream configurationInputStream = null;
		try {
			configurationInputStream = configuration.openConfigurationInputStream();
			if (configurationInputStream != null) {
				preferenceStore.load(configurationInputStream);
			}
		}
		catch (final IOException ioe) {
			throw new RuntimeException(ioe);
		}
		finally {
			try {
				configurationInputStream.close();
			}
			catch (final Exception e) {}
		}

		final PreferenceDialog preferenceDialog = new ConfigurationDialog(parentShell, preferenceManager, images);

		preferenceDialog.setPreferenceStore(preferenceStore);

		if (selectedPage != null) {
			preferenceDialog.setSelectedNode(selectedPage.getNodeId());
		}

		final Map<String, String> configurationBackup = new HashMap<String, String>();
		for (final Preference preference : preferences) {
			if (preference.isRestartRequired()) {
				configurationBackup.put(preference.getConfigurationKey(), configuration.getProperties().getProperty(preference.getConfigurationKey()));
			}
		}

		// Open configuration dialog...
		final int returnCode = preferenceDialog.open();

		if (returnCode == Window.OK) {
			// Reload configuration (autosaved by PreferenceStore on OK button)...
			configuration.reload();
		}

		for (final Entry<String, String> backedUpProperty : configurationBackup.entrySet()) {
			final String oldValue = backedUpProperty.getValue();
			final String newValue = configuration.getProperties().getProperty(backedUpProperty.getKey());
			if ((oldValue != null && newValue == null) || (oldValue == null && newValue != null) || (oldValue != null && !oldValue.equals(newValue))) {
				restartRequired = true;
				break;
			}
		}

		return returnCode;
	}

	public boolean isRestartRequired() {
		return restartRequired;
	}

}
