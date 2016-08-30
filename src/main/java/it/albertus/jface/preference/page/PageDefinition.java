package it.albertus.jface.preference.page;

public interface PageDefinition {

	String getNodeId();

	String getLabel();

	Class<? extends AbstractPreferencePage> getPageClass();

	PageDefinition getParent();

}
