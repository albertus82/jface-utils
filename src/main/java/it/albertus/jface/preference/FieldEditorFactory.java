package it.albertus.jface.preference;

import it.albertus.jface.preference.field.ComboFieldEditor;
import it.albertus.jface.preference.field.DefaultBooleanFieldEditor;
import it.albertus.jface.preference.field.DefaultComboFieldEditor;
import it.albertus.jface.preference.field.DefaultDirectoryFieldEditor;
import it.albertus.jface.preference.field.DefaultFileFieldEditor;
import it.albertus.jface.preference.field.DefaultIntegerFieldEditor;
import it.albertus.jface.preference.field.DefaultRadioGroupFieldEditor;
import it.albertus.jface.preference.field.DefaultStringFieldEditor;
import it.albertus.jface.preference.field.DelimiterComboFieldEditor;
import it.albertus.jface.preference.field.EditableComboFieldEditor;
import it.albertus.jface.preference.field.EmailAddressesListEditor;
import it.albertus.jface.preference.field.IntegerComboFieldEditor;
import it.albertus.jface.preference.field.PasswordFieldEditor;
import it.albertus.jface.preference.field.ScaleIntegerFieldEditor;
import it.albertus.jface.preference.field.UriListEditor;
import it.albertus.jface.preference.field.ValidatedComboFieldEditor;
import it.albertus.jface.preference.field.WrapStringFieldEditor;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class FieldEditorFactory {

	public FieldEditor createFieldEditor(final Class<? extends FieldEditor> type, final String name, final String label, final Composite parent, final FieldEditorData data) {
		if (BooleanFieldEditor.class.equals(type)) {
			return new BooleanFieldEditor(name, label, parent);
		}
		if (ComboFieldEditor.class.equals(type)) {
			return new ComboFieldEditor(name, label, data.getLabelsAndValues().toArray(), parent);
		}
		if (DefaultBooleanFieldEditor.class.equals(type)) {
			return new DefaultBooleanFieldEditor(name, label, parent);
		}
		if (DefaultComboFieldEditor.class.equals(type)) {
			return new DefaultComboFieldEditor(name, label, data.getLabelsAndValues().toArray(), parent);
		}
		if (DefaultDirectoryFieldEditor.class.equals(type)) {
			return createDefaultDirectoryFieldEditor(name, label, parent, data);
		}
		if (DefaultFileFieldEditor.class.equals(type)) {
			return createDefaultFileFieldEditor(name, label, parent, data);
		}
		if (DefaultIntegerFieldEditor.class.equals(type)) {
			return createDefaultIntegerFieldEditor(name, label, parent, data);
		}
		if (DefaultRadioGroupFieldEditor.class.equals(type)) {
			return createDefaultRadioGroupFieldEditor(name, label, parent, data);
		}
		if (DefaultStringFieldEditor.class.equals(type)) {
			return createDefaultStringFieldEditor(name, label, parent, data);
		}
		if (DelimiterComboFieldEditor.class.equals(type)) {
			return createDelimiterComboFieldEditor(name, label, parent, data);
		}
		if (DirectoryFieldEditor.class.equals(type)) {
			return createDirectoryFieldEditor(name, label, parent, data);
		}
		if (EditableComboFieldEditor.class.equals(type)) {
			return new EditableComboFieldEditor(name, label, data.getLabelsAndValues().toArray(), parent);
		}
		if (EmailAddressesListEditor.class.equals(type)) {
			return new EmailAddressesListEditor(name, label, parent, data.getHorizontalSpan(), data.getIcons());
		}
		if (FileFieldEditor.class.equals(type)) {
			return createFileFieldEditor(name, label, parent, data);
		}
		if (IntegerFieldEditor.class.equals(type)) {
			return createIntegerFieldEditor(name, label, parent, data);
		}
		if (IntegerComboFieldEditor.class.equals(type)) {
			return new IntegerComboFieldEditor(name, label, data.getLabelsAndValues().toArray(), parent);
		}
		if (PasswordFieldEditor.class.equals(type)) {
			return createPasswordFieldEditor(name, label, parent, data);
		}
		if (RadioGroupFieldEditor.class.equals(type)) {
			return createRadioGroupFieldEditor(name, label, parent, data);
		}
		if (ScaleFieldEditor.class.equals(type)) {
			return createScaleFieldEditor(name, label, parent, data);
		}
		if (ScaleIntegerFieldEditor.class.equals(type)) {
			return createScaleIntegerFieldEditor(name, label, parent, data);
		}
		if (StringFieldEditor.class.equals(type)) {
			return createStringFieldEditor(name, label, parent, data);
		}
		if (UriListEditor.class.equals(type)) {
			return new UriListEditor(name, label, parent, data.getHorizontalSpan(), data.getIcons());
		}
		if (ValidatedComboFieldEditor.class.equals(type)) {
			return createValidatedComboFieldEditor(name, label, parent, data);
		}
		if (WrapStringFieldEditor.class.equals(type)) {
			return createWrapStringFieldEditor(name, label, parent, data);
		}
		throw new IllegalStateException("Unsupported FieldEditor: " + type);
	}

	protected DefaultDirectoryFieldEditor createDefaultDirectoryFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final DefaultDirectoryFieldEditor formattedDirectoryFieldEditor = new DefaultDirectoryFieldEditor(name, label, parent);
		if (data != null) {
			if (data.getTextLimit() != null) {
				formattedDirectoryFieldEditor.setTextLimit(data.getTextLimit());
			}
			if (data.getEmptyStringAllowed() != null) {
				formattedDirectoryFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
			}
			if (data.getDirectoryDialogMessage() != null) {
				formattedDirectoryFieldEditor.setDialogMessage(data.getDirectoryDialogMessage());
			}
		}
		return formattedDirectoryFieldEditor;
	}

	protected DefaultFileFieldEditor createDefaultFileFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final DefaultFileFieldEditor formattedFileFieldEditor;
		if (data != null && data.getFileEnforceAbsolute() != null) {
			formattedFileFieldEditor = new DefaultFileFieldEditor(name, label, data.getFileEnforceAbsolute(), parent);
		}
		else {
			formattedFileFieldEditor = new DefaultFileFieldEditor(name, label, parent);
		}
		if (data != null) {
			if (data.getTextLimit() != null) {
				formattedFileFieldEditor.setTextLimit(data.getTextLimit());
			}
			if (data.getEmptyStringAllowed() != null) {
				formattedFileFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
			}
			if (data.getFileExtensions() != null && data.getFileExtensions().length != 0) {
				formattedFileFieldEditor.setFileExtensions(data.getFileExtensions());
			}
		}
		return formattedFileFieldEditor;
	}

	protected DefaultIntegerFieldEditor createDefaultIntegerFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final DefaultIntegerFieldEditor formattedIntegerFieldEditor = new DefaultIntegerFieldEditor(name, label, parent);
		if (data != null) {
			if (data.getEmptyStringAllowed() != null) {
				formattedIntegerFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
			}
			if (data.getIntegerMinValidValue() != null && data.getIntegerMaxValidValue() != null) {
				formattedIntegerFieldEditor.setValidRange(data.getIntegerMinValidValue(), data.getIntegerMaxValidValue());
				formattedIntegerFieldEditor.setTextLimit(data.getIntegerMaxValidValue().toString().length());
			}
			if (data.getTextLimit() != null) {
				formattedIntegerFieldEditor.setTextLimit(data.getTextLimit());
			}
		}
		return formattedIntegerFieldEditor;
	}

	protected DefaultRadioGroupFieldEditor createDefaultRadioGroupFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		if (data.getRadioUseGroup() != null) {
			return new DefaultRadioGroupFieldEditor(name, label, data.getRadioNumColumns(), data.getLabelsAndValues().toArray(), parent, data.getRadioUseGroup());
		}
		else {
			return new DefaultRadioGroupFieldEditor(name, label, data.getRadioNumColumns(), data.getLabelsAndValues().toArray(), parent);
		}
	}

	protected DefaultStringFieldEditor createDefaultStringFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final DefaultStringFieldEditor formattedStringFieldEditor;
		if (data != null && data.getTextWidth() != null && data.getTextValidateStrategy() != null) {
			formattedStringFieldEditor = new DefaultStringFieldEditor(name, label, data.getTextWidth(), data.getTextValidateStrategy(), parent);
		}
		else if (data != null && data.getTextValidateStrategy() != null) {
			formattedStringFieldEditor = new DefaultStringFieldEditor(name, label, StringFieldEditor.UNLIMITED, data.getTextValidateStrategy(), parent);
		}
		else if (data != null && data.getTextWidth() != null) {
			formattedStringFieldEditor = new DefaultStringFieldEditor(name, label, data.getTextWidth(), parent);
		}
		else {
			formattedStringFieldEditor = new DefaultStringFieldEditor(name, label, parent);
		}
		if (data != null) {
			if (data.getEmptyStringAllowed() != null) {
				formattedStringFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
			}
			if (data.getTextLimit() != null) {
				formattedStringFieldEditor.setTextLimit(data.getTextLimit());
			}
		}
		return formattedStringFieldEditor;
	}

	protected DelimiterComboFieldEditor createDelimiterComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final DelimiterComboFieldEditor delimiterComboFieldEditor = new DelimiterComboFieldEditor(name, label, data.getLabelsAndValues().toArray(), parent);
		if (data.getEmptyStringAllowed() != null) {
			delimiterComboFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
		}
		return delimiterComboFieldEditor;
	}

	protected DirectoryFieldEditor createDirectoryFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final DirectoryFieldEditor directoryFieldEditor = new DirectoryFieldEditor(name, label, parent);
		if (data != null) {
			if (data.getTextLimit() != null) {
				directoryFieldEditor.setTextLimit(data.getTextLimit());
			}
			if (data.getEmptyStringAllowed() != null) {
				directoryFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
			}
		}
		return directoryFieldEditor;
	}

	protected FileFieldEditor createFileFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final FileFieldEditor fileFieldEditor;
		if (data != null && data.getFileEnforceAbsolute() != null) {
			fileFieldEditor = new FileFieldEditor(name, label, data.getFileEnforceAbsolute(), parent);
		}
		else {
			fileFieldEditor = new FileFieldEditor(name, label, parent);
		}
		if (data != null) {
			if (data.getTextLimit() != null) {
				fileFieldEditor.setTextLimit(data.getTextLimit());
			}
			if (data.getEmptyStringAllowed() != null) {
				fileFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
			}
			if (data.getFileExtensions() != null && data.getFileExtensions().length != 0) {
				fileFieldEditor.setFileExtensions(data.getFileExtensions());
			}
		}
		return fileFieldEditor;
	}

	protected IntegerFieldEditor createIntegerFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(name, label, parent);
		if (data != null) {
			if (data.getEmptyStringAllowed() != null) {
				integerFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
			}
			if (data.getIntegerMinValidValue() != null && data.getIntegerMaxValidValue() != null) {
				integerFieldEditor.setValidRange(data.getIntegerMinValidValue(), data.getIntegerMaxValidValue());
				integerFieldEditor.setTextLimit(data.getIntegerMaxValidValue().toString().length());
			}
			if (data.getTextLimit() != null) {
				integerFieldEditor.setTextLimit(data.getTextLimit());
			}
		}
		return integerFieldEditor;
	}

	protected PasswordFieldEditor createPasswordFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final PasswordFieldEditor passwordFieldEditor;
		if (data != null && data.getTextWidth() != null) {
			passwordFieldEditor = new PasswordFieldEditor(name, label, data.getTextWidth(), parent);
		}
		else {
			passwordFieldEditor = new PasswordFieldEditor(name, label, parent);
		}
		if (data != null) {
			if (data.getEmptyStringAllowed() != null) {
				passwordFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
			}
			if (data.getTextLimit() != null) {
				passwordFieldEditor.setTextLimit(data.getTextLimit());
			}
		}
		return passwordFieldEditor;
	}

	protected RadioGroupFieldEditor createRadioGroupFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		if (data.getRadioUseGroup() != null) {
			return new RadioGroupFieldEditor(name, label, data.getRadioNumColumns(), data.getLabelsAndValues().toArray(), parent, data.getRadioUseGroup());
		}
		else {
			return new RadioGroupFieldEditor(name, label, data.getRadioNumColumns(), data.getLabelsAndValues().toArray(), parent);
		}
	}

	protected ScaleFieldEditor createScaleFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final ScaleFieldEditor scaleFieldEditor = new ScaleFieldEditor(name, label, parent);
		if (data != null) {
			if (data.getScaleMinimum() != null) {
				scaleFieldEditor.setMinimum(data.getScaleMinimum());
			}
			if (data.getScaleMaximum() != null) {
				scaleFieldEditor.setMaximum(data.getScaleMaximum());
			}
			if (data.getScaleIncrement() != null) {
				scaleFieldEditor.setIncrement(data.getScaleIncrement());
			}
			if (data.getScalePageIncrement() != null) {
				scaleFieldEditor.setPageIncrement(data.getScalePageIncrement());
			}
		}
		return scaleFieldEditor;
	}

	protected ScaleIntegerFieldEditor createScaleIntegerFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		int min = 0;
		int max = 10;
		int increment = 1;
		int pageIncrement = 1;

		if (data != null) {
			if (data.getScaleMinimum() != null) {
				min = data.getScaleMinimum();
			}
			if (data.getScaleMaximum() != null) {
				max = data.getScaleMaximum();
			}
			if (data.getScaleIncrement() != null) {
				increment = data.getScaleIncrement();
			}
			if (data.getScalePageIncrement() != null) {
				pageIncrement = data.getScalePageIncrement();
			}
		}
		return new ScaleIntegerFieldEditor(name, label, parent, min, max, increment, pageIncrement);
	}

	protected StringFieldEditor createStringFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final StringFieldEditor stringFieldEditor;
		if (data != null && data.getTextWidth() != null && data.getTextValidateStrategy() != null) {
			stringFieldEditor = new StringFieldEditor(name, label, data.getTextWidth(), data.getTextValidateStrategy(), parent);
		}
		else if (data != null && data.getTextValidateStrategy() != null) {
			stringFieldEditor = new StringFieldEditor(name, label, StringFieldEditor.UNLIMITED, data.getTextValidateStrategy(), parent);
		}
		else if (data != null && data.getTextWidth() != null) {
			stringFieldEditor = new StringFieldEditor(name, label, data.getTextWidth(), parent);
		}
		else {
			stringFieldEditor = new StringFieldEditor(name, label, parent);
		}
		if (data != null) {
			if (data.getEmptyStringAllowed() != null) {
				stringFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
			}
			if (data.getTextLimit() != null) {
				stringFieldEditor.setTextLimit(data.getTextLimit());
			}
		}
		return stringFieldEditor;
	}

	protected ValidatedComboFieldEditor createValidatedComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final ValidatedComboFieldEditor validatedComboFieldEditor = new ValidatedComboFieldEditor(name, label, data.getLabelsAndValues().toArray(), parent);
		if (data.getEmptyStringAllowed() != null) {
			validatedComboFieldEditor.setEmptyStringAllowed(data.getEmptyStringAllowed());
		}
		return validatedComboFieldEditor;
	}

	protected WrapStringFieldEditor createWrapStringFieldEditor(final String name, final String label, final Composite parent, final FieldEditorData data) {
		final WrapStringFieldEditor wrapStringFieldEditor;
		if (data != null && data.getTextHeight() != null) {
			wrapStringFieldEditor = new WrapStringFieldEditor(name, label, parent, data.getTextHeight());
		}
		else {
			wrapStringFieldEditor = new WrapStringFieldEditor(name, label, parent);
		}
		if (data != null && data.getTextLimit() != null) {
			wrapStringFieldEditor.setTextLimit(data.getTextLimit());
		}
		return wrapStringFieldEditor;
	}

}
