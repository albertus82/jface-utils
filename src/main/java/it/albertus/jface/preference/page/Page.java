package it.albertus.jface.preference.page;

public interface Page {

	String getNodeId();

	String getLabel();

	Class<? extends AbstractPreferencePage> getPageClass();

	Page getParent();

}
