package it.albertus.jface.preference;

import it.albertus.jface.preference.page.IPageDefinition;

import java.util.Collection;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

public interface IPreference {

	FieldEditorFactory fieldEditorFactory = new FieldEditorFactory();

	String getName();

	String getLabel();

	IPageDefinition getPageDefinition();

	String getDefaultValue();

	boolean isSeparate();

	IPreference getParent();

	boolean isRestartRequired();

	Collection<? extends IPreference> getChildren();

	FieldEditor createFieldEditor(Composite parent);

}
