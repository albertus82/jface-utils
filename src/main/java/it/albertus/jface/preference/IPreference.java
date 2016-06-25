package it.albertus.jface.preference;

import it.albertus.jface.preference.page.IPage;

import java.util.Set;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

public interface IPreference {

	String getConfigurationKey();

	String getLabel();

	IPage getPage();

	String getDefaultValue();

	IPreference getParent();

	Set<? extends IPreference> getChildren();

	FieldEditor createFieldEditor(Composite parent);

}
