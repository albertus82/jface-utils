package it.albertus.jface.preference;

import it.albertus.jface.preference.page.IPreferencePageDefinition;

import java.util.Set;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

public interface IPreference {

	String getName();

	String getLabel();

	IPreferencePageDefinition getPageDefinition();

	String getDefaultValue();

	boolean isSeparate();

	IPreference getParent();

	boolean isRestartRequired();

	Set<? extends IPreference> getChildren();

	FieldEditor createFieldEditor(Composite parent);

}
