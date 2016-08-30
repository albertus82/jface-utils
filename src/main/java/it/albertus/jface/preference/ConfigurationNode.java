package it.albertus.jface.preference;

import it.albertus.jface.preference.page.BasePreferencePage;
import it.albertus.jface.preference.page.PageDefinition;
import it.albertus.util.Configuration;

import org.eclipse.jface.preference.PreferenceNode;

public class ConfigurationNode extends PreferenceNode {

	private final Preference[] preferences;
	private final Configuration configuration;
	private final PageDefinition pageDefinition;

	public ConfigurationNode(final PageDefinition pageDefinition, final Preference[] preferences, final Configuration configuration) {
		super(pageDefinition.getNodeId(), pageDefinition.getLabel(), null, pageDefinition.getPageClass() != null ? pageDefinition.getPageClass().getName() : null);
		this.preferences = preferences;
		this.configuration = configuration;
		this.pageDefinition = pageDefinition;
	}

	@Override
	public void createPage() {
		final BasePreferencePage page;
		if (pageDefinition.getPageClass() != null) {
			super.createPage();
			page = getPage();
		}
		else {
			page = new BasePreferencePage();
			setPage(page);
			page.setTitle(pageDefinition.getLabel());
		}
		page.setPreferences(preferences);
		page.setConfiguration(configuration);
		page.setPageDefinition(pageDefinition);
	}

	@Override
	public BasePreferencePage getPage() {
		return (BasePreferencePage) super.getPage();
	}

}
