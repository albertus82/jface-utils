package it.albertus.jface.preference.page;

import it.albertus.util.Localized;

import org.eclipse.jface.resource.ImageDescriptor;

public interface IPageDefinition {

	String getNodeId();

	Localized getLabel();

	Class<? extends BasePreferencePage> getPageClass();

	ImageDescriptor getImage();

	IPageDefinition getParent();

}
