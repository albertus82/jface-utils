package it.albertus.jface.preference;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.preference.field.BigDecimalComboFieldEditor;
import it.albertus.jface.preference.field.BigDecimalFieldEditor;
import it.albertus.jface.preference.field.BigIntegerComboFieldEditor;
import it.albertus.jface.preference.field.BigIntegerFieldEditor;
import it.albertus.jface.preference.field.ByteComboFieldEditor;
import it.albertus.jface.preference.field.ByteFieldEditor;
import it.albertus.jface.preference.field.DateFieldEditor;
import it.albertus.jface.preference.field.DefaultBooleanFieldEditor;
import it.albertus.jface.preference.field.DefaultComboFieldEditor;
import it.albertus.jface.preference.field.DefaultRadioGroupFieldEditor;
import it.albertus.jface.preference.field.DelimiterComboFieldEditor;
import it.albertus.jface.preference.field.DoubleComboFieldEditor;
import it.albertus.jface.preference.field.DoubleFieldEditor;
import it.albertus.jface.preference.field.EditableComboFieldEditor;
import it.albertus.jface.preference.field.EmailAddressesListEditor;
import it.albertus.jface.preference.field.EnhancedDirectoryFieldEditor;
import it.albertus.jface.preference.field.EnhancedFileFieldEditor;
import it.albertus.jface.preference.field.EnhancedIntegerFieldEditor;
import it.albertus.jface.preference.field.EnhancedStringFieldEditor;
import it.albertus.jface.preference.field.FieldEditorDefault;
import it.albertus.jface.preference.field.FloatComboFieldEditor;
import it.albertus.jface.preference.field.FloatFieldEditor;
import it.albertus.jface.preference.field.IntegerComboFieldEditor;
import it.albertus.jface.preference.field.LocalizedPathEditor;
import it.albertus.jface.preference.field.LongComboFieldEditor;
import it.albertus.jface.preference.field.LongFieldEditor;
import it.albertus.jface.preference.field.PasswordFieldEditor;
import it.albertus.jface.preference.field.ScaleIntegerFieldEditor;
import it.albertus.jface.preference.field.ShortComboFieldEditor;
import it.albertus.jface.preference.field.ShortFieldEditor;
import it.albertus.jface.preference.field.UriListEditor;
import it.albertus.jface.preference.field.ValidatedComboFieldEditor;
import it.albertus.jface.preference.field.WrapStringFieldEditor;

public class FieldEditorFactory {

	private boolean boldCustomValues = true;

	public boolean isBoldCustomValues() {
		return boldCustomValues;
	}

	public void setBoldCustomValues(final boolean boldCustomValues) {
		this.boldCustomValues = boldCustomValues;
	}

	public FieldEditor createFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final FieldEditor fieldEditor;

		final Class<? extends FieldEditor> type = details.getFieldEditorClass();

		if (BigIntegerComboFieldEditor.class.equals(type)) {
			fieldEditor = createBigIntegerComboFieldEditor(name, label, parent, details);
		}
		else if (BigDecimalComboFieldEditor.class.equals(type)) {
			fieldEditor = createBigDecimalComboFieldEditor(name, label, parent, details);
		}
		else if (BooleanFieldEditor.class.equals(type)) {
			fieldEditor = new BooleanFieldEditor(name, label, parent);
		}
		else if (ColorFieldEditor.class.equals(type)) {
			fieldEditor = new ColorFieldEditor(name, label, parent);
		}
		else if (ComboFieldEditor.class.equals(type)) {
			fieldEditor = new ComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		}
		else if (BigDecimalFieldEditor.class.equals(type)) {
			fieldEditor = createBigDecimalFieldEditor(name, label, parent, details);
		}
		else if (BigIntegerFieldEditor.class.equals(type)) {
			fieldEditor = createBigIntegerFieldEditor(name, label, parent, details);
		}
		else if (DefaultBooleanFieldEditor.class.equals(type)) {
			fieldEditor = new DefaultBooleanFieldEditor(name, label, parent);
		}
		else if (DefaultComboFieldEditor.class.equals(type)) {
			fieldEditor = new DefaultComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		}
		else if (DateFieldEditor.class.equals(type)) {
			fieldEditor = createDateFieldEditor(name, label, parent, details);
		}
		else if (EnhancedDirectoryFieldEditor.class.equals(type)) {
			fieldEditor = createEnhancedDirectoryFieldEditor(name, label, parent, details);
		}
		else if (DoubleFieldEditor.class.equals(type)) {
			fieldEditor = createDoubleFieldEditor(name, label, parent, details);
		}
		else if (EnhancedFileFieldEditor.class.equals(type)) {
			fieldEditor = createEnhancedFileFieldEditor(name, label, parent, details);
		}
		else if (FloatFieldEditor.class.equals(type)) {
			fieldEditor = createFloatFieldEditor(name, label, parent, details);
		}
		else if (EnhancedIntegerFieldEditor.class.equals(type)) {
			fieldEditor = createEnhancedIntegerFieldEditor(name, label, parent, details);
		}
		else if (LongFieldEditor.class.equals(type)) {
			fieldEditor = createLongFieldEditor(name, label, parent, details);
		}
		else if (ShortFieldEditor.class.equals(type)) {
			fieldEditor = createShortFieldEditor(name, label, parent, details);
		}
		else if (ByteFieldEditor.class.equals(type)) {
			fieldEditor = createByteFieldEditor(name, label, parent, details);
		}
		else if (DefaultRadioGroupFieldEditor.class.equals(type)) {
			fieldEditor = createDefaultRadioGroupFieldEditor(name, label, parent, details);
		}
		else if (EnhancedStringFieldEditor.class.equals(type)) {
			fieldEditor = createEnhancedStringFieldEditor(name, label, parent, details);
		}
		else if (DelimiterComboFieldEditor.class.equals(type)) {
			fieldEditor = createDelimiterComboFieldEditor(name, label, parent, details);
		}
		else if (DirectoryFieldEditor.class.equals(type)) {
			fieldEditor = createDirectoryFieldEditor(name, label, parent, details);
		}
		else if (DoubleComboFieldEditor.class.equals(type)) {
			fieldEditor = createDoubleComboFieldEditor(name, label, parent, details);
		}
		else if (EditableComboFieldEditor.class.equals(type)) {
			fieldEditor = new EditableComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		}
		else if (EmailAddressesListEditor.class.equals(type)) {
			fieldEditor = createEmailAddressesListEditor(name, label, parent, details);
		}
		else if (FileFieldEditor.class.equals(type)) {
			fieldEditor = createFileFieldEditor(name, label, parent, details);
		}
		else if (FloatComboFieldEditor.class.equals(type)) {
			fieldEditor = createFloatComboFieldEditor(name, label, parent, details);
		}
		else if (FontFieldEditor.class.equals(type)) {
			fieldEditor = createFontFieldEditor(name, label, parent, details);
		}
		else if (IntegerFieldEditor.class.equals(type)) {
			fieldEditor = createIntegerFieldEditor(name, label, parent, details);
		}
		else if (IntegerComboFieldEditor.class.equals(type)) {
			fieldEditor = createIntegerComboFieldEditor(name, label, parent, details);
		}
		else if (LocalizedPathEditor.class.equals(type)) {
			fieldEditor = createLocalizedPathEditor(name, label, parent, details);
		}
		else if (LongComboFieldEditor.class.equals(type)) {
			fieldEditor = createLongComboFieldEditor(name, label, parent, details);
		}
		else if (PasswordFieldEditor.class.equals(type)) {
			fieldEditor = createPasswordFieldEditor(name, label, parent, details);
		}
		else if (PathEditor.class.equals(type)) {
			fieldEditor = new PathEditor(name, label, details != null && details.getDirectoryDialogMessage() != null ? details.getDirectoryDialogMessage().toString() : null, parent);
		}
		else if (RadioGroupFieldEditor.class.equals(type)) {
			fieldEditor = createRadioGroupFieldEditor(name, label, parent, details);
		}
		else if (ScaleFieldEditor.class.equals(type)) {
			fieldEditor = createScaleFieldEditor(name, label, parent, details);
		}
		else if (ScaleIntegerFieldEditor.class.equals(type)) {
			fieldEditor = createScaleIntegerFieldEditor(name, label, parent, details);
		}
		else if (ShortComboFieldEditor.class.equals(type)) {
			fieldEditor = createShortComboFieldEditor(name, label, parent, details);
		}
		else if (ByteComboFieldEditor.class.equals(type)) {
			fieldEditor = createByteComboFieldEditor(name, label, parent, details);
		}
		else if (StringFieldEditor.class.equals(type)) {
			fieldEditor = createStringFieldEditor(name, label, parent, details);
		}
		else if (UriListEditor.class.equals(type)) {
			fieldEditor = createUriListEditor(name, label, parent, details);
		}
		else if (ValidatedComboFieldEditor.class.equals(type)) {
			fieldEditor = createValidatedComboFieldEditor(name, label, parent, details);
		}
		else if (WrapStringFieldEditor.class.equals(type)) {
			fieldEditor = createWrapStringFieldEditor(name, label, parent, details);
		}
		else {
			throw new IllegalStateException("Unsupported FieldEditor: " + type);
		}

		postConstruct(fieldEditor, details);

		return fieldEditor;
	}

	protected void postConstruct(final FieldEditor fieldEditor, final FieldEditorDetails details) {
		if (details != null && fieldEditor instanceof FieldEditorDefault) {
			final FieldEditorDefault fieldEditorDefault = (FieldEditorDefault) fieldEditor;
			if (details.getDefaultToolTip() != null) {
				fieldEditorDefault.setDefaultToolTip(details.getDefaultToolTip());
			}
			if (!isBoldCustomValues()) {
				fieldEditorDefault.setBoldCustomValues(false);
			}
			else if (details.getBoldCustomValues() != null) {
				fieldEditorDefault.setBoldCustomValues(details.getBoldCustomValues());
			}
		}
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

	protected ByteComboFieldEditor createByteComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final ByteComboFieldEditor fieldEditor = new ByteComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		if (details.getNumberMinimum() != null) {
			fieldEditor.setMinValidValue(details.getNumberMinimum().byteValue());
		}
		if (details.getNumberMaximum() != null) {
			fieldEditor.setMaxValidValue(details.getNumberMaximum().byteValue());
		}
		if (details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

	protected ByteFieldEditor createByteFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final ByteFieldEditor fieldEditor = new ByteFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getNumberMinimum() != null) {
				fieldEditor.setMinValidValue(details.getNumberMinimum().byteValue());
			}
			if (details.getNumberMaximum() != null) {
				fieldEditor.setMaxValidValue(details.getNumberMaximum().byteValue());
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
	}

	protected DateFieldEditor createDateFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final DateFieldEditor fieldEditor;
		final int style = details.getStyle() != null ? details.getStyle() : SWT.NONE;
		if (details.getTextWidth() != null && details.getTextValidateStrategy() != null) {
			fieldEditor = new DateFieldEditor(name, label, details.getDatePattern(), style, details.getTextWidth(), details.getTextValidateStrategy(), parent);
		}
		else if (details.getTextValidateStrategy() != null) {
			fieldEditor = new DateFieldEditor(name, label, details.getDatePattern(), style, StringFieldEditor.UNLIMITED, details.getTextValidateStrategy(), parent);
		}
		else if (details.getTextWidth() != null) {
			fieldEditor = new DateFieldEditor(name, label, details.getDatePattern(), style, details.getTextWidth(), parent);
		}
		else {
			fieldEditor = new DateFieldEditor(name, label, details.getDatePattern(), style, parent);
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

	protected EnhancedStringFieldEditor createEnhancedStringFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final EnhancedStringFieldEditor fieldEditor;
		if (details != null && details.getTextWidth() != null && details.getTextValidateStrategy() != null) {
			fieldEditor = new EnhancedStringFieldEditor(name, label, details.getTextWidth(), details.getTextValidateStrategy(), parent);
		}
		else if (details != null && details.getTextValidateStrategy() != null) {
			fieldEditor = new EnhancedStringFieldEditor(name, label, StringFieldEditor.UNLIMITED, details.getTextValidateStrategy(), parent);
		}
		else if (details != null && details.getTextWidth() != null) {
			fieldEditor = new EnhancedStringFieldEditor(name, label, details.getTextWidth(), parent);
		}
		else {
			fieldEditor = new EnhancedStringFieldEditor(name, label, parent);
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
				fieldEditor.setTextLimit(Math.max(Integer.toString(details.getNumberMaximum().intValue()).length(), Integer.toString(details.getNumberMinimum().intValue()).length()));
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

	protected ShortComboFieldEditor createShortComboFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final ShortComboFieldEditor fieldEditor = new ShortComboFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		if (details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		if (details.getNumberMinimum() != null) {
			fieldEditor.setMinValidValue(details.getNumberMinimum().shortValue());
		}
		if (details.getNumberMaximum() != null) {
			fieldEditor.setMaxValidValue(details.getNumberMaximum().shortValue());
		}
		if (details.getTextLimit() != null) {
			fieldEditor.setTextLimit(details.getTextLimit());
		}
		return fieldEditor;
	}

	protected ShortFieldEditor createShortFieldEditor(final String name, final String label, final Composite parent, final FieldEditorDetails details) {
		final ShortFieldEditor fieldEditor = new ShortFieldEditor(name, label, parent);
		if (details != null) {
			if (details.getEmptyStringAllowed() != null) {
				fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
			}
			if (details.getNumberMinimum() != null) {
				fieldEditor.setMinValidValue(details.getNumberMinimum().shortValue());
			}
			if (details.getNumberMaximum() != null) {
				fieldEditor.setMaxValidValue(details.getNumberMaximum().shortValue());
			}
			if (details.getTextLimit() != null) {
				fieldEditor.setTextLimit(details.getTextLimit());
			}
		}
		return fieldEditor;
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
		if (details != null && details.getEmptyStringAllowed() != null) {
			fieldEditor.setEmptyStringAllowed(details.getEmptyStringAllowed());
		}
		return fieldEditor;
	}

}
