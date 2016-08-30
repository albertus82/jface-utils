package it.albertus.jface.preference;

import it.albertus.jface.preference.page.AbstractPreferencePage;
import it.albertus.jface.preference.page.PageDefinition;
import it.albertus.util.Configuration;

import org.eclipse.jface.preference.PreferenceNode;

public class ConfigurationNode extends PreferenceNode {

	private final Preference[] preferences;
	private final Configuration configuration;
	private final PageDefinition pageDefinition;

	public ConfigurationNode(final PageDefinition pageDefinition, final Preference[] preferences, final Configuration configuration) {
		super(pageDefinition.getNodeId(), pageDefinition.getLabel(), null, pageDefinition.getPageClass().getName());
		this.preferences = preferences;
		this.configuration = configuration;
		this.pageDefinition = pageDefinition;
	}

	@Override
	public void createPage() {
		super.createPage();
		if (getPage() instanceof AbstractPreferencePage) {
			final AbstractPreferencePage page = (AbstractPreferencePage) getPage();
			page.setPreferences(preferences);
			page.setConfiguration(configuration);
			page.setPageDefinition(pageDefinition);
		}
	}

}
