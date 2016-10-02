package it.albertus.jface.preference.decoration;

import it.albertus.jface.decoration.TextDecoration;
import it.albertus.jface.validation.TextValidator;
import it.albertus.util.Localized;

import org.eclipse.jface.preference.StringFieldEditor;

public class StringFieldEditorDecoration extends TextDecoration {

	public StringFieldEditorDecoration(final TextValidator validator, final StringFieldEditor fieldEditor) {
		super(validator, new Localized() {
			@Override
			public String getString() {
				return fieldEditor.getErrorMessage();
			}
		});
	}

}
