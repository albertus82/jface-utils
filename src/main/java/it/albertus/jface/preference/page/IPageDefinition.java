package it.albertus.jface.preference.page;

import org.eclipse.jface.resource.ImageDescriptor;

public interface IPageDefinition {

	String getNodeId();

	String getLabel();

	Class<? extends BasePreferencePage> getPageClass();

	ImageDescriptor getImage();

	IPageDefinition getParent();

}
