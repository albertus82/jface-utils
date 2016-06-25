package it.albertus.jface.preference;

import it.albertus.jface.preference.page.IPage;
import it.albertus.util.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractPreferences {

	protected final Shell parentShell;

	public AbstractPreferences(final Shell parentShell) {
		this.parentShell = parentShell;
	}

	public int open() {
		return open(null);
	}

	public int open(final IPage selectedPage) {
		final Configuration configuration = getConfiguration();

		final PreferenceManager preferenceManager = new PreferenceManager();

		// Pages creation...
		final Map<IPage, PreferenceNode> preferenceNodes = new HashMap<IPage, PreferenceNode>();
		for (final IPage page : getPages()) {
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
		for (final IPreference preference : getPreferences()) {
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

		final PreferenceDialog preferenceDialog = new ConfigurationDialog(parentShell, preferenceManager, getImages());

		preferenceDialog.setPreferenceStore(preferenceStore);

		if (selectedPage != null) {
			preferenceDialog.setSelectedNode(selectedPage.getNodeId());
		}

		// Open configuration dialog...
		final int returnCode = preferenceDialog.open();

		if (returnCode == Window.OK) {
			// Reload configuration (autosaved by PreferenceStore on OK button)...
			configuration.reload();
		}
		return returnCode;
	}

	public Shell getParentShell() {
		return parentShell;
	}

	protected abstract Configuration getConfiguration();

	protected abstract IPage[] getPages();

	protected abstract IPreference[] getPreferences();

	protected Image[] getImages() {
		return null;
	}

}
