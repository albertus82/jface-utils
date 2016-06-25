package it.albertus.jface.preference.page;

public interface IPage {

	String getNodeId();

	String getLabel();

	Class<? extends AbstractPreferencePage> getPageClass();

	IPage getParent();

}
