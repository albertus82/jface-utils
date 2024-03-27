package io.github.albertus82.jface.preference;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.preference.PreferenceNode;

import io.github.albertus82.jface.preference.page.BasePreferencePage;
import io.github.albertus82.jface.preference.page.IPageDefinition;

public class ConfigurationNode extends PreferenceNode {

	private final IPageDefinition pageDefinition;
	private final IPreference[] preferences;
	private final IPreferencesCallback preferencesCallback;

	public ConfigurationNode(final IPageDefinition pageDefinition, final IPreference[] preferences, final IPreferencesCallback preferencesCallback) {
		super(pageDefinition.getNodeId(), pageDefinition.getLabel().replace("&&", "&"), pageDefinition.getImage(), pageDefinition.getPageClass() != null ? pageDefinition.getPageClass().getName() : null);
		this.pageDefinition = pageDefinition;
		this.preferences = preferences;
		this.preferencesCallback = preferencesCallback;
	}

	@Override
	public void createPage() {
		final BasePreferencePage page;
		final Class<? extends BasePreferencePage> pageClass = pageDefinition.getPageClass();
		if (pageClass != null) {
			try {
				page = pageClass.getDeclaredConstructor().newInstance();
			}
			catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new IllegalStateException(e);
			}
		}
		else {
			page = new BasePreferencePage();
		}
		setPage(page);
		if (getLabelImage() != null) {
			page.setImageDescriptor(getImageDescriptor());
		}
		page.setTitle(pageDefinition.getLabel());
		page.setPreferences(preferences);
		page.setPreferencesCallback(preferencesCallback);
		page.setPageDefinition(pageDefinition);
	}

	@Override
	public BasePreferencePage getPage() {
		return (BasePreferencePage) super.getPage();
	}

}
