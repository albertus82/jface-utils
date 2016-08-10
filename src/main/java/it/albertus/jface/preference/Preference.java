package it.albertus.jface.preference;

import it.albertus.jface.preference.page.Page;

import java.util.Set;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

public interface Preference {

	String getConfigurationKey();

	String getLabel();

	Page getPage();

	String getDefaultValue();

	Preference getParent();

	boolean isRestartRequired();

	Set<? extends Preference> getChildren();

	FieldEditor createFieldEditor(Composite parent);

}
