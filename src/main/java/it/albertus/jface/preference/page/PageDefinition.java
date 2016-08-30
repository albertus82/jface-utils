package it.albertus.jface.preference.page;

import org.eclipse.jface.resource.ImageDescriptor;

public interface PageDefinition {

	String getNodeId();

	String getLabel();

	Class<? extends BasePreferencePage> getPageClass();

	ImageDescriptor getImage();

	PageDefinition getParent();

}
