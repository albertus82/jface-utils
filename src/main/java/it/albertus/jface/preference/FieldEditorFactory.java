package it.albertus.jface.preference;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.preference.field.BigDecimalComboFieldEditor;
import it.albertus.jface.preference.field.BigDecimalFieldEditor;
import it.albertus.jface.preference.field.BigIntegerComboFieldEditor;
import it.albertus.jface.preference.field.BigIntegerFieldEditor;
import it.albertus.jface.preference.field.DateFieldEditor;
import it.albertus.jface.preference.field.DefaultBooleanFieldEditor;
import it.albertus.jface.preference.field.DefaultComboFieldEditor;
import it.albertus.jface.preference.field.DefaultRadioGroupFieldEditor;
import it.albertus.jface.preference.field.DefaultStringFieldEditor;
import it.albertus.jface.preference.field.DelimiterComboFieldEditor;
import it.albertus.jface.preference.field.DoubleComboFieldEditor;
import it.albertus.jface.preference.field.DoubleFieldEditor;
import it.albertus.jface.preference.field.EditableComboFieldEditor;
import it.albertus.jface.preference.field.EmailAddressesListEditor;
import it.albertus.jface.preference.field.EnhancedDirectoryFieldEditor;
import it.albertus.jface.preference.field.EnhancedFileFieldEditor;
import it.albertus.jface.preference.field.EnhancedIntegerFieldEditor;
import it.albertus.jface.preference.field.FloatComboFieldEditor;
import it.albertus.jface.preference.field.FloatFieldEditor;
import it.albertus.jface.preference.field.IntegerComboFieldEditor;
import it.albertus.jface.preference.field.LocalizedPathEditor;
import it.albertus.jface.preference.field.LongComboFieldEditor;
import it.albertus.jface.preference.field.LongFieldEditor;
import it.albertus.jface.preference.field.PasswordFieldEditor;
import it.albertus.jface.preference.field.ScaleIntegerFieldEditor;
import it.albertus.jface.preference.field.UriListEditor;
import it.albertus.jface.preference.field.ValidatedComboFieldEditor;
import it.albertus.jface.preference.field.WrapStringFieldEditor;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class FieldEditorFactory {

	public FieldEditor createFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final Class<? extends FieldEditor> type = details.getFieldEditorClass();

		if (BigIntegerComboFieldEditor.class.equals(type)) {
			return createBigIntegerComboFieldEditor(name, label, parent, details);
		}
		if (BigDecimalComboFieldEditor.class.equals(type)) {
			return createBigDecimalComboFieldEditor(name, label, parent, details);
		}
		if (BooleanFieldEditor.class.equals(type)) {
			return new BooleanFieldEditor(name, label, parent);
		}
		if (ColorFieldEditor.class.equals(type)) {
			return new ColorFieldEditor(name, label, parent);
		}
		if (ComboFieldEditor.class.equals(type)) {
			return new ComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		}
		if (BigDecimalFieldEditor.class.equals(type)) {
			return createBigDecimalFieldEditor(name, label, parent, details);
		}
		if (BigIntegerFieldEditor.class.equals(type)) {
			return createBigIntegerFieldEditor(name, label, parent, details);
		}
		if (DefaultBooleanFieldEditor.class.equals(type)) {
			return new DefaultBooleanFieldEditor(name, label, parent);
		}
		if (DefaultComboFieldEditor.class.equals(type)) {
			return new DefaultComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		}
		if (DateFieldEditor.class.equals(type)) {
			return createDateFieldEditor(name, label, parent, details);
		}
		if (EnhancedDirectoryFieldEditor.class.equals(type)) {
			return createEnhancedDirectoryFieldEditor(name, label, parent, details);
		}
		if (DoubleFieldEditor.class.equals(type)) {
			return createDoubleFieldEditor(name, label, parent, details);
		}
		if (EnhancedFileFieldEditor.class.equals(type)) {
			return createEnhancedFileFieldEditor(name, label, parent, details);
		}
		if (FloatFieldEditor.class.equals(type)) {
			return createFloatFieldEditor(name, label, parent, details);
		}
		if (EnhancedIntegerFieldEditor.class.equals(type)) {
			return createEnhancedIntegerFieldEditor(name, label, parent, details);
		}
		if (LongFieldEditor.class.equals(type)) {
			return createLongFieldEditor(name, label, parent, details);
		}
		if (DefaultRadioGroupFieldEditor.class.equals(type)) {
			return createDefaultRadioGroupFieldEditor(name, label, parent, details);
		}
		if (DefaultStringFieldEditor.class.equals(type)) {
			return createDefaultStringFieldEditor(name, label, parent, details);
		}
		if (DelimiterComboFieldEditor.class.equals(type)) {
			return createDelimiterComboFieldEditor(name, label, parent, details);
		}
		if (DirectoryFieldEditor.class.equals(type)) {
			return createDirectoryFieldEditor(name, label, parent, details);
		}
		if (DoubleComboFieldEditor.class.equals(type)) {
			return createDoubleComboFieldEditor(name, label, parent, details);
		}
		if (EditableComboFieldEditor.class.equals(type)) {
			return new EditableComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		}
		if (EmailAddressesListEditor.class.equals(type)) {
			return createEmailAddressesListEditor(name, label, parent, details);
		}
		if (FileFieldEditor.class.equals(type)) {
			return createFileFieldEditor(name, label, parent, details);
		}
		if (FloatComboFieldEditor.class.equals(type)) {
			return createFloatComboFieldEditor(name, label, parent, details);
		}
		if (FontFieldEditor.class.equals(type)) {
			return createFontFieldEditor(name, label, parent, details);
		}
		if (IntegerFieldEditor.class.equals(type)) {
			return createIntegerFieldEditor(name, label, parent, details);
		}
		if (IntegerComboFieldEditor.class.equals(type)) {
			return createIntegerComboFieldEditor(name, label, parent, details);
		}
		if (LocalizedPathEditor.class.equals(type)) {
			return createLocalizedPathEditor(name, label, parent, details);
		}
		if (LongComboFieldEditor.class.equals(type)) {
			return createLongComboFieldEditor(name, label, parent, details);
		}
		if (PasswordFieldEditor.class.equals(type)) {
			return createPasswordFieldEditor(name, label, parent, details);
		}
		if (PathEditor.class.equals(type)) {
			return new PathEditor(name, label, details != null && details.getDirectoryDialogMessage() != null ? details.getDirectoryDialogMessage().toString() : null, parent);
		}
		if (RadioGroupFieldEditor.class.equals(type)) {
			return createRadioGroupFieldEditor(name, label, parent, details);
		}
		if (ScaleFieldEditor.class.equals(type)) {
			return createScaleFieldEditor(name, label, parent, details);
		}
		if (ScaleIntegerFieldEditor.class.equals(type)) {
			return createScaleIntegerFieldEditor(name, label, parent, details);
		}
		if (StringFieldEditor.class.equals(type)) {
			return createStringFieldEditor(name, label, parent, details);
		}
		if (UriListEditor.class.equals(type)) {
			return createUriListEditor(name, label, parent, details);
		}
		if (ValidatedComboFieldEditor.class.equals(type)) {
			return createValidatedComboFieldEditor(name, label, parent, details);
		}
		if (WrapStringFieldEditor.class.equals(type)) {
			return createWrapStringFieldEditor(name, label, parent, details);
		}
		throw new IllegalStateException("Unsupported FieldEditor: " + type);
	}

	protected BigIntegerComboFieldEditor createBigIntegerComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final BigIntegerComboFieldEditor fieldEditor = new BigIntegerComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		if (details.getNumberMinimum() == null || details.getNumberMinimum() instanceof BigInteger) {
			fieldEditor.setMinValidValue((BigInteger) details.getNumberMinimum());
		}
		else {
			fieldEditor.setMinValidValue(BigInteger.valueOf(details.getNumberMinimum().longValue()));
		}
		if (details.getNumberMaximum() == null || details.getNumberMaximum() instanceof BigInteger) {
			fieldEditor.setMaxValidValue((BigInteger) details.getNumberMaximum());
		}
		else {
			fieldEditor.setMaxValidValue(BigInteger.valueOf(details.getNumberMaximum().longValue()));
		}
		if (details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

	protected BigDecimalComboFieldEditor createBigDecimalComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final BigDecimalComboFieldEditor fieldEditor = new BigDecimalComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		if (details.getNumberMinimum() == null || details.getNumberMinimum() instanceof BigDecimal) {
			fieldEditor.setMinValidValue((BigDecimal) details.getNumberMinimum());
		}
		else {
			fieldEditor.setMinValidValue(BigDecimal.valueOf(details.getNumberMinimum().doubleValue()));
		}
		if (details.getNumberMaximum() == null || details.getNumberMaximum() instanceof BigDecimal) {
			fieldEditor.setMaxValidValue((BigDecimal) details.getNumberMaximum());
		}
		else {
			fieldEditor.setMaxValidValue(BigDecimal.valueOf(details.getNumberMaximum().doubleValue()));
		}
		if (details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

	protected BigDecimalFieldEditor createBigDecimalFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final BigDecimalFieldEditor fieldEditor = new BigDecimalFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			final Number min = details.getNumberMinimum();
			if (min != null) {
				fieldEditor.setMinValidValue(min instanceof BigDecimal ? (BigDecimal) min : BigDecimal.valueOf(min.doubleValue()));
			}
			final Number max = details.getNumberMaximum();
			if (max != null) {
				fieldEditor.setMaxValidValue(max instanceof BigDecimal ? (BigDecimal) max : BigDecimal.valueOf(max.doubleValue()));
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected BigIntegerFieldEditor createBigIntegerFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final BigIntegerFieldEditor fieldEditor = new BigIntegerFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			final Number min = details.getNumberMinimum();
			if (min != null) {
				fieldEditor.setMinValidValue(min instanceof BigInteger ? (BigInteger) min : BigInteger.valueOf(min.longValue()));
			}
			final Number max = details.getNumberMaximum();
			if (max != null) {
				fieldEditor.setMaxValidValue(max instanceof BigInteger ? (BigInteger) max : BigInteger.valueOf(max.longValue()));
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected DateFieldEditor createDateFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final DateFieldEditor fieldEditor;
		if (details.getTextWidth() != null && details.getTextValidateStrategy() != null) {
			fieldEditor = new DateFieldEditor(name, label, details.getDatePattern(), details.getTextWidth(), details.getTextValidateStrategy(), parent);
		}
		else if (details.getTextValidateStrategy() != null) {
			fieldEditor = new DateFieldEditor(name, label, details.getDatePattern(), StringFieldEditor.UNLIMITED, details.getTextValidateStrategy(), parent);
		}
		else if (details.getTextWidth() != null) {
			fieldEditor = new DateFieldEditor(name, label, details.getDatePattern(), details.getTextWidth(), parent);
		}
		else {
			fieldEditor = new DateFieldEditor(name, label, details.getDatePattern(), parent);
		}
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		if (details.getDateFrom() != null) {
			fieldEditor.setMinValidValue(details.getDateFrom());
		}
		if (details.getDateTo() != null) {
			fieldEditor.setMaxValidValue(details.getDateTo());
		}
		if (details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

	protected EnhancedDirectoryFieldEditor createEnhancedDirectoryFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final EnhancedDirectoryFieldEditor fieldEditor = new EnhancedDirectoryFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getDirectoryDialogMessage() != null) {
				fieldEditor.setDialogMessage(details.getDirectoryDialogMessage());
			}
		}
		return fieldEditor;
	}

	protected DoubleFieldEditor createDoubleFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final DoubleFieldEditor fieldEditor = new DoubleFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getNumberMinimum() != null) {
				fieldEditor.setMinValidValue(details.getNumberMinimum().doubleValue());
			}
			if (details.getNumberMaximum() != null) {
				fieldEditor.setMaxValidValue(details.getNumberMaximum().doubleValue());
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected EnhancedFileFieldEditor createEnhancedFileFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final EnhancedFileFieldEditor fieldEditor;
		if (details != null && details.getFileEnforceAbsolute() != null) {
			fieldEditor = new EnhancedFileFieldEditor(name, label, details.getFileEnforceAbsolute(), parent);
		}
		else {
			fieldEditor = new EnhancedFileFieldEditor(name, label, parent);
		}
		if (details != null) {
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getFileExtensions() != null && details.getFileExtensions().length != 0) {
				fieldEditor.setFileExtensions(details.getFileExtensions());
			}
		}
		return fieldEditor;
	}

	protected FloatFieldEditor createFloatFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final FloatFieldEditor fieldEditor = new FloatFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getNumberMinimum() != null) {
				fieldEditor.setMinValidValue(details.getNumberMinimum().floatValue());
			}
			if (details.getNumberMaximum() != null) {
				fieldEditor.setMaxValidValue(details.getNumberMaximum().floatValue());
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected EnhancedIntegerFieldEditor createEnhancedIntegerFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final EnhancedIntegerFieldEditor fieldEditor = new EnhancedIntegerFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getNumberMinimum() != null) {
				fieldEditor.setMinValidValue(details.getNumberMinimum().intValue());
			}
			if (details.getNumberMaximum() != null) {
				fieldEditor.setMaxValidValue(details.getNumberMaximum().intValue());
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected LongFieldEditor createLongFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final LongFieldEditor fieldEditor = new LongFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getNumberMinimum() != null) {
				fieldEditor.setMinValidValue(details.getNumberMinimum().longValue());
			}
			if (details.getNumberMaximum() != null) {
				fieldEditor.setMaxValidValue(details.getNumberMaximum().longValue());
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected DefaultRadioGroupFieldEditor createDefaultRadioGroupFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		if (details.getRadioUseGroup() != null) {
			return new DefaultRadioGroupFieldEditor(name, label, details.getRadioNumColumns(), details.getLabelsAndValues().toArray(), parent, details.getRadioUseGroup());
		}
		else {
			return new DefaultRadioGroupFieldEditor(name, label, details.getRadioNumColumns(), details.getLabelsAndValues().toArray(), parent);
		}
	}

	protected DefaultStringFieldEditor createDefaultStringFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final DefaultStringFieldEditor fieldEditor;
		if (details != null && details.getTextWidth() != null && details.getTextValidateStrategy() != null) {
			fieldEditor = new DefaultStringFieldEditor(name, label, details.getTextWidth(), details.getTextValidateStrategy(), parent);
		}
		else if (details != null && details.getTextValidateStrategy() != null) {
			fieldEditor = new DefaultStringFieldEditor(name, label, StringFieldEditor.UNLIMITED, details.getTextValidateStrategy(), parent);
		}
		else if (details != null && details.getTextWidth() != null) {
			fieldEditor = new DefaultStringFieldEditor(name, label, details.getTextWidth(), parent);
		}
		else {
			fieldEditor = new DefaultStringFieldEditor(name, label, parent);
		}
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected DelimiterComboFieldEditor createDelimiterComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final DelimiterComboFieldEditor fieldEditor = new DelimiterComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		return fieldEditor;
	}

	protected DirectoryFieldEditor createDirectoryFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final DirectoryFieldEditor fieldEditor = new DirectoryFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
		}
		return fieldEditor;
	}

	protected DoubleComboFieldEditor createDoubleComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final DoubleComboFieldEditor fieldEditor = new DoubleComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		if (details.getNumberMinimum() != null) {
			fieldEditor.setMinValidValue(details.getNumberMinimum().doubleValue());
		}
		if (details.getNumberMaximum() != null) {
			fieldEditor.setMaxValidValue(details.getNumberMaximum().doubleValue());
		}
		if (details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

	protected EmailAddressesListEditor createEmailAddressesListEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		if (details != null) {
			return new EmailAddressesListEditor(name, label, parent, details.getHorizontalSpan(), details.getIcons());
		}
		else {
			return new EmailAddressesListEditor(name, label, parent, null, null);
		}
	}

	protected FileFieldEditor createFileFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final FileFieldEditor fieldEditor;
		if (details != null && details.getFileEnforceAbsolute() != null) {
			fieldEditor = new FileFieldEditor(name, label, details.getFileEnforceAbsolute(), parent);
		}
		else {
			fieldEditor = new FileFieldEditor(name, label, parent);
		}
		if (details != null) {
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getFileExtensions() != null && details.getFileExtensions().length != 0) {
				fieldEditor.setFileExtensions(details.getFileExtensions());
			}
		}
		return fieldEditor;
	}

	protected FloatComboFieldEditor createFloatComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final FloatComboFieldEditor fieldEditor = new FloatComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		if (details.getNumberMinimum() != null) {
			fieldEditor.setMinValidValue(details.getNumberMinimum().floatValue());
		}
		if (details.getNumberMaximum() != null) {
			fieldEditor.setMaxValidValue(details.getNumberMaximum().floatValue());
		}
		if (details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

	protected FontFieldEditor createFontFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final FontFieldEditor fieldEditor;
		if (details != null && details.getFontPreviewAreaText() != null) {
			fieldEditor = new FontFieldEditor(name, label, details.getFontPreviewAreaText().getString(), parent);
		}
		else {
			fieldEditor = new FontFieldEditor(name, label, parent);
		}
		if (details != null && details.getFontChangeButtonText() != null) {
			fieldEditor.setChangeButtonText(details.getFontChangeButtonText().getString());
		}
		else {
			fieldEditor.setChangeButtonText(JFaceMessages.get("lbl.button.change"));
		}
		return fieldEditor;
	}

	protected IntegerComboFieldEditor createIntegerComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final IntegerComboFieldEditor fieldEditor = new IntegerComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		if (details.getNumberMinimum() != null) {
			fieldEditor.setMinValidValue(details.getNumberMinimum().intValue());
		}
		if (details.getNumberMaximum() != null) {
			fieldEditor.setMaxValidValue(details.getNumberMaximum().intValue());
		}
		if (details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

	protected IntegerFieldEditor createIntegerFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final IntegerFieldEditor fieldEditor = new IntegerFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getNumberMinimum() != null && details.getNumberMaximum() != null) {
				fieldEditor.setValidRange(details.getNumberMinimum().intValue(), details.getNumberMaximum().intValue());
				fieldEditor.setTextLimit(Math.max(Integer.valueOf(details.getNumberMaximum().intValue()).toString().length(), Integer.valueOf(details.getNumberMinimum().intValue()).toString().length()));
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected LocalizedPathEditor createLocalizedPathEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final LocalizedPathEditor fieldEditor;
		if (details != null) {
			fieldEditor = new LocalizedPathEditor(name, label, details.getDirectoryDialogMessage(), parent, details.getHorizontalSpan());
		}
		else {
			fieldEditor = new LocalizedPathEditor(name, label, null, parent);
		}
		return fieldEditor;
	}

	protected LongComboFieldEditor createLongComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final LongComboFieldEditor fieldEditor = new LongComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		if (details.getNumberMinimum() != null) {
			fieldEditor.setMinValidValue(details.getNumberMinimum().longValue());
		}
		if (details.getNumberMaximum() != null) {
			fieldEditor.setMaxValidValue(details.getNumberMaximum().longValue());
		}
		if (details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

	protected PasswordFieldEditor createPasswordFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final PasswordFieldEditor fieldEditor;
		if (details != null && details.getTextWidth() != null) {
			fieldEditor = new PasswordFieldEditor(name, label, details.getTextWidth(), parent);
		}
		else {
			fieldEditor = new PasswordFieldEditor(name, label, parent);
		}
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected RadioGroupFieldEditor createRadioGroupFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		if (details.getRadioUseGroup() != null) {
			return new RadioGroupFieldEditor(name, label, details.getRadioNumColumns(), details.getLabelsAndValues().toArray(), parent, details.getRadioUseGroup());
		}
		else {
			return new RadioGroupFieldEditor(name, label, details.getRadioNumColumns(), details.getLabelsAndValues().toArray(), parent);
		}
	}

	protected ScaleFieldEditor createScaleFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final ScaleFieldEditor fieldEditor = new ScaleFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getScaleMinimum() != null) {
				fieldEditor.setMinimum(details.getScaleMinimum());
			}
			if (details.getScaleMaximum() != null) {
				fieldEditor.setMaximum(details.getScaleMaximum());
			}
			if (details.getScaleIncrement() != null) {
				fieldEditor.setIncrement(details.getScaleIncrement());
			}
			if (details.getScalePageIncrement() != null) {
				fieldEditor.setPageIncrement(details.getScalePageIncrement());
			}
		}
		return fieldEditor;
	}

	protected ScaleIntegerFieldEditor createScaleIntegerFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		int min = 0;
		int max = 10;
		int increment = 1;
		int pageIncrement = 1;

		if (details != null) {
			if (details.getScaleMinimum() != null) {
				min = details.getScaleMinimum();
			}
			if (details.getScaleMaximum() != null) {
				max = details.getScaleMaximum();
			}
			if (details.getScaleIncrement() != null) {
				increment = details.getScaleIncrement();
			}
			if (details.getScalePageIncrement() != null) {
				pageIncrement = details.getScalePageIncrement();
			}
		}
		return new ScaleIntegerFieldEditor(name, label, parent, min, max, increment, pageIncrement);
	}

	protected StringFieldEditor createStringFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final StringFieldEditor fieldEditor;
		if (details != null && details.getTextWidth() != null && details.getTextValidateStrategy() != null) {
			fieldEditor = new StringFieldEditor(name, label, details.getTextWidth(), details.getTextValidateStrategy(), parent);
		}
		else if (details != null && details.getTextValidateStrategy() != null) {
			fieldEditor = new StringFieldEditor(name, label, StringFieldEditor.UNLIMITED, details.getTextValidateStrategy(), parent);
		}
		else if (details != null && details.getTextWidth() != null) {
			fieldEditor = new StringFieldEditor(name, label, details.getTextWidth(), parent);
		}
		else {
			fieldEditor = new StringFieldEditor(name, label, parent);
		}
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected UriListEditor createUriListEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		if (details != null) {
			return new UriListEditor(name, label, parent, details.getHorizontalSpan(), details.getIcons());
		}
		else {
			return new UriListEditor(name, label, parent, null, null);
		}
	}

	protected ValidatedComboFieldEditor createValidatedComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final ValidatedComboFieldEditor fieldEditor = new ValidatedComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		return fieldEditor;
	}

	protected WrapStringFieldEditor createWrapStringFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final WrapStringFieldEditor fieldEditor;
		if (details != null && details.getTextHeight() != null) {
			fieldEditor = new WrapStringFieldEditor(name, label, parent, details.getTextHeight());
		}
		else {
			fieldEditor = new WrapStringFieldEditor(name, label, parent);
		}
		if (details != null && details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

}
