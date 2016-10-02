package it.albertus.jface.preference;

import it.albertus.jface.preference.FieldEditorDetails.FieldEditorDetailsBuilder;
import it.albertus.jface.preference.PreferenceDetails.PreferenceDetailsBuilder;
import it.albertus.jface.preference.field.BigDecimalComboFieldEditor;
import it.albertus.jface.preference.field.BigDecimalFieldEditor;
import it.albertus.jface.preference.field.BigIntegerComboFieldEditor;
import it.albertus.jface.preference.field.BigIntegerFieldEditor;
import it.albertus.jface.preference.field.DateFieldEditor;
import it.albertus.jface.preference.field.DefaultBooleanFieldEditor;
import it.albertus.jface.preference.field.DefaultComboFieldEditor;
import it.albertus.jface.preference.field.DefaultRadioGroupFieldEditor;
import it.albertus.jface.preference.field.EnhancedStringFieldEditor;
import it.albertus.jface.preference.field.DoubleComboFieldEditor;
import it.albertus.jface.preference.field.DoubleFieldEditor;
import it.albertus.jface.preference.field.EditableComboFieldEditor;
import it.albertus.jface.preference.field.EmailAddressesListEditor;
import it.albertus.jface.preference.field.EnhancedIntegerFieldEditor;
import it.albertus.jface.preference.field.FloatComboFieldEditor;
import it.albertus.jface.preference.field.FloatFieldEditor;
import it.albertus.jface.preference.field.IntegerComboFieldEditor;
import it.albertus.jface.preference.field.LongComboFieldEditor;
import it.albertus.jface.preference.field.LongFieldEditor;
import it.albertus.jface.preference.field.PasswordFieldEditor;
import it.albertus.jface.preference.field.ScaleIntegerFieldEditor;
import it.albertus.jface.preference.field.UriListEditor;
import it.albertus.jface.preference.field.ValidatedComboFieldEditor;
import it.albertus.jface.preference.field.WrapStringFieldEditor;
import it.albertus.jface.preference.page.IPageDefinition;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Set;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public enum MyPreference implements IPreference {

	STRING(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("string").label("String").defaultValue("Hello World!").build(), new FieldEditorDetailsBuilder(EnhancedStringFieldEditor.class).emptyStringAllowed(false).build()),
	WRAP_STRING(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("wrapString").label("Wrap String").defaultValue("Long text here.").build(), new FieldEditorDetailsBuilder(WrapStringFieldEditor.class).build()),

	INTEGER(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("integer").label("Integer").defaultValue(12345).build(), new FieldEditorDetailsBuilder(EnhancedIntegerFieldEditor.class).emptyStringAllowed(true).numberMinimum(-67890).build()),
	LONG(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("long").label("Long").defaultValue(135791357913579L).build(), new FieldEditorDetailsBuilder(LongFieldEditor.class).emptyStringAllowed(true).numberMaximum(1000000000000000000L).build()),
	BIGINTEGER(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("bigInteger").label("BigInteger").defaultValue(-246802468024680L).build(), new FieldEditorDetailsBuilder(BigIntegerFieldEditor.class).build()),
	FLOAT(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).separate().name("float").label("Float").defaultValue(123.456f).build(), new FieldEditorDetailsBuilder(FloatFieldEditor.class).emptyStringAllowed(true).build()),
	DOUBLE(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("double").label("Double").defaultValue(24680.13579).build(), new FieldEditorDetailsBuilder(DoubleFieldEditor.class).emptyStringAllowed(true).build()),
	BIGDECIMAL(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("bigDecimal").label("BigDecimal").defaultValue(67890.12345).build(), new FieldEditorDetailsBuilder(BigDecimalFieldEditor.class).emptyStringAllowed(true).numberValidRange(-12345678, 87654321).build()),

	COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("combo").label("Combo").defaultValue("value 1").build(), new FieldEditorDetailsBuilder(DefaultComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("Label 1", "value 1").put("Label 2", "value 2")).build()),
	EDITABLE_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("editableCombo").label("Editable Combo").build(), new FieldEditorDetailsBuilder(EditableComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("Label 3", "value 3").put("Label 4", "value 4")).emptyStringAllowed(true).build()),
	VALIDATED_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("validatedCombo").label("Validated Combo").defaultValue("value 5").build(), new FieldEditorDetailsBuilder(ValidatedComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("Label 5", "value 5")).emptyStringAllowed(false).build()),

	INTEGER_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("integerCombo").label("Integer Combo").defaultValue(12345).build(), new FieldEditorDetailsBuilder(IntegerComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("InTeGeR", 1)).numberValidRange(-67890, 67890).emptyStringAllowed(true).build()),
	LONG_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("longCombo").label("Long Combo").defaultValue(135791357913579L).build(), new FieldEditorDetailsBuilder(LongComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("long", 1)).emptyStringAllowed(false).build()),
	BIGINTEGER_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("bigIntegerCombo").label("BigInteger Combo").defaultValue(-246802468024680L).build(), new FieldEditorDetailsBuilder(BigIntegerComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("bigInteger", 1)).emptyStringAllowed(false).build()),
	FLOAT_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).separate().name("floatCombo").label("Float Combo").defaultValue(123.456f).build(), new FieldEditorDetailsBuilder(FloatComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("float", 1)).emptyStringAllowed(true).numberValidRange(-10000, 20000).build()),
	DOUBLE_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("doubleCombo").label("Double Combo").defaultValue(24680.13579).build(), new FieldEditorDetailsBuilder(DoubleComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("Double value", -10.5).put("invalid", 1234567890)).emptyStringAllowed(true).numberValidRange(-1000, 100000).build()),
	BIGDECIMAL_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("bigDecimalCombo").label("BigDecimal Combo").defaultValue(67890.12345).build(), new FieldEditorDetailsBuilder(BigDecimalComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("BigDecimal Value", -10.5).put("invalid", 1000000)).emptyStringAllowed(false).numberValidRange(-1000, 100000).textLimit(20).build()),

	BOOLEAN(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("boolean").label("Boolean").defaultValue(false).build(), new FieldEditorDetailsBuilder(DefaultBooleanFieldEditor.class).build()),
	COLOR(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("color").label("Color").defaultValue("255,0,0").build(), new FieldEditorDetailsBuilder(ColorFieldEditor.class).build()),
	PASSWORD(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("password").label("Password").build(), new FieldEditorDetailsBuilder(PasswordFieldEditor.class).build()),
	DATE(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("date").label("Date").defaultValue("24/12/2015").build(), new FieldEditorDetailsBuilder(DateFieldEditor.class).datePattern("dd/MM/yyyy").dateFrom(new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime()).style(SWT.DROP_DOWN).build()),
	SCALE(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("scale").label("Scale").defaultValue(25).build(), new FieldEditorDetailsBuilder(ScaleIntegerFieldEditor.class).scaleMinimum(0).scaleMaximum(100).scalePageIncrement(5).build()),
	RADIO(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("radio").label("Radio").build(), new FieldEditorDetailsBuilder(DefaultRadioGroupFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("Label 6", "value 6").put("Label 7", "value 7")).radioNumColumns(2).radioUseGroup(true).build()),

	EMAIL(new PreferenceDetailsBuilder(MyPageDefinition.PAGE).name("emails").label("Emails").build(), new FieldEditorDetailsBuilder(EmailAddressesListEditor.class).build()),
	URI(new PreferenceDetailsBuilder(MyPageDefinition.PAGE).name("uris").label("URIs").build(), new FieldEditorDetailsBuilder(UriListEditor.class).build());

	private PreferenceDetails preferenceDetails;
	private FieldEditorDetails fieldEditorDetails;

	MyPreference(PreferenceDetails preferenceDetails, FieldEditorDetails fieldEditorDetails) {
		this.preferenceDetails = preferenceDetails;
		this.fieldEditorDetails = fieldEditorDetails;
	}

	@Override
	public String getName() {
		return preferenceDetails.getName();
	}

	@Override
	public String getLabel() {
		return preferenceDetails.getLabel().getString();
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
	public Set<? extends IPreference> getChildren() {
		Set<MyPreference> preferences = EnumSet.noneOf(MyPreference.class);
		for (MyPreference item : MyPreference.values()) {
			if (this.equals(item.getParent())) {
				preferences.add(item);
			}
		}
		return preferences;
	}

	@Override
	public FieldEditor createFieldEditor(Composite parent) {
		return fieldEditorFactory.createFieldEditor(getName(), getLabel(), parent, fieldEditorDetails);
	}

}
