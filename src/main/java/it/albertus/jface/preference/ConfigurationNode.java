package it.albertus.jface.preference;

import it.albertus.jface.preference.page.BasePreferencePage;
import it.albertus.jface.preference.page.IPreferencePageDefinition;
import it.albertus.util.Configuration;

import org.eclipse.jface.preference.PreferenceNode;

public class ConfigurationNode extends PreferenceNode {

	private final IPreference[] preferences;
	private final Configuration configuration;
	private final IPreferencePageDefinition pageDefinition;

	public ConfigurationNode(final IPreferencePageDefinition pageDefinition, final IPreference[] preferences, final Configuration configuration) {
		super(pageDefinition.getNodeId(), pageDefinition.getLabel().getString(), pageDefinition.getImage(), pageDefinition.getPageClass() != null ? pageDefinition.getPageClass().getName() : null);
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
			if (getLabelImage() != null) {
				page.setImageDescriptor(getImageDescriptor());
			}
		}
		else {
			page = new BasePreferencePage();
			setPage(page);
			page.setTitle(pageDefinition.getLabel().getString());
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
