package io.github.albertus82.jface.preference;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Set;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import io.github.albertus82.jface.preference.FieldEditorDetails.FieldEditorDetailsBuilder;
import io.github.albertus82.jface.preference.PreferenceDetails.PreferenceDetailsBuilder;
import io.github.albertus82.jface.preference.field.BigDecimalComboFieldEditor;
import io.github.albertus82.jface.preference.field.BigDecimalFieldEditor;
import io.github.albertus82.jface.preference.field.BigIntegerComboFieldEditor;
import io.github.albertus82.jface.preference.field.BigIntegerFieldEditor;
import io.github.albertus82.jface.preference.field.ByteComboFieldEditor;
import io.github.albertus82.jface.preference.field.ByteFieldEditor;
import io.github.albertus82.jface.preference.field.DateFieldEditor;
import io.github.albertus82.jface.preference.field.DefaultBooleanFieldEditor;
import io.github.albertus82.jface.preference.field.DefaultComboFieldEditor;
import io.github.albertus82.jface.preference.field.DefaultRadioGroupFieldEditor;
import io.github.albertus82.jface.preference.field.DelimiterComboFieldEditor;
import io.github.albertus82.jface.preference.field.DoubleComboFieldEditor;
import io.github.albertus82.jface.preference.field.DoubleFieldEditor;
import io.github.albertus82.jface.preference.field.EditableComboFieldEditor;
import io.github.albertus82.jface.preference.field.EmailAddressesListEditor;
import io.github.albertus82.jface.preference.field.EnhancedDirectoryFieldEditor;
import io.github.albertus82.jface.preference.field.EnhancedFileFieldEditor;
import io.github.albertus82.jface.preference.field.EnhancedIntegerFieldEditor;
import io.github.albertus82.jface.preference.field.EnhancedStringFieldEditor;
import io.github.albertus82.jface.preference.field.FloatComboFieldEditor;
import io.github.albertus82.jface.preference.field.FloatFieldEditor;
import io.github.albertus82.jface.preference.field.IntegerComboFieldEditor;
import io.github.albertus82.jface.preference.field.LocalizedPathEditor;
import io.github.albertus82.jface.preference.field.LongComboFieldEditor;
import io.github.albertus82.jface.preference.field.LongFieldEditor;
import io.github.albertus82.jface.preference.field.PasswordFieldEditor;
import io.github.albertus82.jface.preference.field.ScaleIntegerFieldEditor;
import io.github.albertus82.jface.preference.field.ShortComboFieldEditor;
import io.github.albertus82.jface.preference.field.ShortFieldEditor;
import io.github.albertus82.jface.preference.field.UriListEditor;
import io.github.albertus82.jface.preference.field.ValidatedComboFieldEditor;
import io.github.albertus82.jface.preference.field.WrapStringFieldEditor;
import io.github.albertus82.jface.preference.page.IPageDefinition;

public enum MyPreferenceDisabled implements IPreference {

	STRING_1(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("string1").label("String 1").defaultValue("Validate on key up.").build(), new FieldEditorDetailsBuilder(EnhancedStringFieldEditor.class).disabled(true).emptyStringAllowed(false).build()),
	STRING_2(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("string2").label("String 2").defaultValue("Validate on focus lost.").build(), new FieldEditorDetailsBuilder(EnhancedStringFieldEditor.class).disabled(true).emptyStringAllowed(false).textValidateStrategy(StringFieldEditor.VALIDATE_ON_FOCUS_LOST).build()),
	STRING_EMPTY(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("stringEmpty").label("String Empty").defaultValue("Can be empty.").build(), new FieldEditorDetailsBuilder(EnhancedStringFieldEditor.class).disabled(true).emptyStringAllowed(true).build()),
	WRAP_STRING(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("wrapString").label("Wrap String").defaultValue("Long text here.").build(), new FieldEditorDetailsBuilder(WrapStringFieldEditor.class).disabled(true).emptyStringAllowed(false).build()),

	BYTE(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("byte").label("Byte").defaultValue(123).build(), new FieldEditorDetailsBuilder(ByteFieldEditor.class).disabled(true).numberMinimum(-100).build()),
	SHORT(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("short").label("Short").defaultValue(1234).build(), new FieldEditorDetailsBuilder(ShortFieldEditor.class).disabled(true).emptyStringAllowed(true).build()),
	INTEGER(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("integer").label("Integer").defaultValue(12345).build(), new FieldEditorDetailsBuilder(EnhancedIntegerFieldEditor.class).disabled(true).emptyStringAllowed(true).numberMinimum(-67890).build()),
	LONG(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("long").label("Long").defaultValue(135791357913579L).build(), new FieldEditorDetailsBuilder(LongFieldEditor.class).disabled(true).emptyStringAllowed(true).numberMaximum(1000000000000000000L).build()),
	BIGINTEGER(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("bigInteger").label("BigInteger").defaultValue(-246802468024680L).build(), new FieldEditorDetailsBuilder(BigIntegerFieldEditor.class).disabled(true).build()),
	FLOAT(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).separate().name("float").label("Float").defaultValue(123.456f).build(), new FieldEditorDetailsBuilder(FloatFieldEditor.class).disabled(true).emptyStringAllowed(true).build()),
	DOUBLE(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("double").label("Double").defaultValue(24680.13579).build(), new FieldEditorDetailsBuilder(DoubleFieldEditor.class).disabled(true).emptyStringAllowed(true).build()),
	BIGDECIMAL(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("bigDecimal").label("BigDecimal").defaultValue(67890.12345).build(), new FieldEditorDetailsBuilder(BigDecimalFieldEditor.class).disabled(true).emptyStringAllowed(true).numberValidRange(-12345678, 87654321).build()),

	COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("combo").label("Combo").defaultValue("value 1").build(), new FieldEditorDetailsBuilder(DefaultComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("Label 1", "value 1").put("Label 2", "value 2")).build()),
	EDITABLE_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("editableCombo").label("Editable Combo").build(), new FieldEditorDetailsBuilder(EditableComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("Label 3", "value 3").put("Label 4", "value 4")).emptyStringAllowed(true).build()),
	VALIDATED_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("validatedCombo").label("Validated Combo").defaultValue("value 5").build(), new FieldEditorDetailsBuilder(ValidatedComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("Label 5", "value 5")).emptyStringAllowed(false).build()),
	DELIMITER_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("delimiterCombo").label("Delimiter Combo").defaultValue(" ").build(), new FieldEditorDetailsBuilder(DelimiterComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("Whitespace", " ").put("Tab", "\t").put("Comma", ",").put("Semicolon", ";")).emptyStringAllowed(false).build()),

	BYTE_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("byteCombo").label("Byte Combo").defaultValue(123).build(), new FieldEditorDetailsBuilder(ByteComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("label for 123", 123)).emptyStringAllowed(true).build()),
	SHORT_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("shortCombo").label("Short Combo").defaultValue(1234).build(), new FieldEditorDetailsBuilder(ShortComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("label for 321", 321)).numberMaximum(2000).build()),
	INTEGER_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("integerCombo").label("Integer Combo").defaultValue(12345).build(), new FieldEditorDetailsBuilder(IntegerComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("InTeGeR", 1)).numberValidRange(-67890, 67890).emptyStringAllowed(true).build()),
	LONG_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("longCombo").label("Long Combo").defaultValue(135791357913579L).build(), new FieldEditorDetailsBuilder(LongComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("long", 1)).emptyStringAllowed(false).build()),
	BIGINTEGER_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("bigIntegerCombo").label("BigInteger Combo").defaultValue(-246802468024680L).build(), new FieldEditorDetailsBuilder(BigIntegerComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("bigInteger", 1)).emptyStringAllowed(false).build()),
	FLOAT_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).separate().name("floatCombo").label("Float Combo").defaultValue(123.456f).build(), new FieldEditorDetailsBuilder(FloatComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("float", 1)).emptyStringAllowed(true).numberValidRange(-10000, 20000).build()),
	DOUBLE_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("doubleCombo").label("Double Combo").defaultValue(24680.13579).build(), new FieldEditorDetailsBuilder(DoubleComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("Double value", -10.5).put("invalid", 1234567890)).emptyStringAllowed(true).numberValidRange(-1000, 100000).build()),
	BIGDECIMAL_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("bigDecimalCombo").label("BigDecimal Combo").defaultValue(67890.12345).build(), new FieldEditorDetailsBuilder(BigDecimalComboFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("BigDecimal Value", -10.5).put("invalid", 1000000)).emptyStringAllowed(false).numberValidRange(-1000, 100000).textLimit(20).build()),

	BOOLEAN(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("boolean").label("Boolean").defaultValue(false).build(), new FieldEditorDetailsBuilder(DefaultBooleanFieldEditor.class).disabled(true).build()),
	COLOR(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("color").label("Color").defaultValue("255,0,0").build(), new FieldEditorDetailsBuilder(ColorFieldEditor.class).disabled(true).build()),
	PASSWORD(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("password").label("Password").build(), new FieldEditorDetailsBuilder(PasswordFieldEditor.class).disabled(true).build()),
	DATE_1(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("date1").label("Date 1").defaultValue("24/12/2015").build(), new FieldEditorDetailsBuilder(DateFieldEditor.class).disabled(true).datePattern("dd/MM/yyyy").dateFrom(new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime()).style(SWT.DROP_DOWN).build()),
	DATE_2(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("date2").label("Date 2").defaultValue("12/06/2015").build(), new FieldEditorDetailsBuilder(DateFieldEditor.class).disabled(true).datePattern("dd/MM/yyyy").dateFrom(new GregorianCalendar(2000, Calendar.JUNE, 15).getTime()).emptyStringAllowed(true).build()),
	SCALE(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("scale").label("Scale").defaultValue(25).build(), new FieldEditorDetailsBuilder(ScaleIntegerFieldEditor.class).disabled(true).scaleMinimum(0).scaleMaximum(100).scalePageIncrement(5).build()),
	RADIO(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("radio").label("Radio").build(), new FieldEditorDetailsBuilder(DefaultRadioGroupFieldEditor.class).disabled(true).labelsAndValues(new StaticLabelsAndValues("Label 6", "value 6").put("Label 7", "value 7")).radioNumColumns(2).radioUseGroup(true).build()),
	FILE(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("file").label("File").build(), new FieldEditorDetailsBuilder(EnhancedFileFieldEditor.class).disabled(true).fileExtensions(new String[] { "*.txt;*.TXT" }).build()),
	DIRECTORY(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("directory").label("Directory").build(), new FieldEditorDetailsBuilder(EnhancedDirectoryFieldEditor.class).disabled(true).build()),

	EMAIL(new PreferenceDetailsBuilder(MyPageDefinition.PAGE).name("emails").label("Emails").build(), new FieldEditorDetailsBuilder(EmailAddressesListEditor.class).disabled(true).icons(Display.getDefault().getSystemImage(SWT.ICON_WARNING)).build()),
	URI(new PreferenceDetailsBuilder(MyPageDefinition.PAGE).name("uris").label("URIs").build(), new FieldEditorDetailsBuilder(UriListEditor.class).disabled(true).icons(Display.getDefault().getSystemImage(SWT.ICON_INFORMATION)).build()),
	PATH(new PreferenceDetailsBuilder(MyPageDefinition.PAGE).name("path").label("Path").build(), new FieldEditorDetailsBuilder(LocalizedPathEditor.class).disabled(true).build());

	private static final FieldEditorFactory fieldEditorFactory = new FieldEditorFactory();

	private PreferenceDetails preferenceDetails;
	private FieldEditorDetails fieldEditorDetails;

	MyPreferenceDisabled(PreferenceDetails preferenceDetails, FieldEditorDetails fieldEditorDetails) {
		this.preferenceDetails = preferenceDetails;
		this.fieldEditorDetails = fieldEditorDetails;
	}

	@Override
	public String getName() {
		return preferenceDetails.getName();
	}

	@Override
	public String getLabel() {
		return preferenceDetails.getLabel().get();
	}

	@Override
	public IPageDefinition getPageDefinition() {
		return preferenceDetails.getPageDefinition();
	}

	@Override
	public String getDefaultValue() {
		return preferenceDetails.getDefaultValue();
	}

	@Override
	public IPreference getParent() {
		return preferenceDetails.getParent();
	}

	@Override
	public boolean isRestartRequired() {
		return preferenceDetails.isRestartRequired();
	}

	@Override
	public boolean isSeparate() {
		return preferenceDetails.isSeparate();
	}

	@Override
	public IPreference[] getChildren() {
		Set<MyPreferenceDisabled> preferences = EnumSet.noneOf(MyPreferenceDisabled.class);
		for (MyPreferenceDisabled item : MyPreferenceDisabled.values()) {
			if (this.equals(item.getParent())) {
				preferences.add(item);
			}
		}
		return preferences.toArray(new IPreference[preferences.size()]);
	}

	@Override
	public FieldEditor createFieldEditor(Composite parent) {
		return fieldEditorFactory.createFieldEditor(getName(), getLabel(), parent, fieldEditorDetails);
	}

}
