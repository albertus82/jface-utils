package it.albertus.jface.preference;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

import it.albertus.jface.preference.page.IPageDefinition;

/**
 * <p>
 * This is a simple example of enum that implements
 * {@link it.albertus.jface.preference.IPreference IPreference}:
 * </p>
 * 
 * <pre>
 * public enum MyPreference implements IPreference {
 * 
 * 	STRING(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("string").label("String").defaultValue("Hello World!").build(), new FieldEditorDetailsBuilder(DefaultStringFieldEditor.class).build()),
 * 	WRAP_STRING(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("wrapString").label("Wrap String").defaultValue("Long text here.").build(), new FieldEditorDetailsBuilder(WrapStringFieldEditor.class).build()),
 * 
 * 	INTEGER(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("integer").label("Integer").defaultValue(12345).build(), new FieldEditorDetailsBuilder(EnhancedIntegerFieldEditor.class).emptyStringAllowed(true).numberMinimum(-67890).build()),
 * 	DOUBLE(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("double").label("Double").defaultValue(24680.13579).build(), new FieldEditorDetailsBuilder(DoubleFieldEditor.class).emptyStringAllowed(true).build()),
 * 
 * 	COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("combo").label("Combo").defaultValue("value 1").build(), new FieldEditorDetailsBuilder(DefaultComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("Label 1", "value 1").put("Label 2", "value 2")).build()),
 * 	VALIDATED_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("validatedCombo").label("Validated Combo").defaultValue("value 5").build(), new FieldEditorDetailsBuilder(ValidatedComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("Label 5", "value 5")).emptyStringAllowed(false).build()),
 * 
 * 	FLOAT_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).separate().name("floatCombo").label("Float Combo").defaultValue(123.456f).build(), new FieldEditorDetailsBuilder(FloatComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("float", 1)).emptyStringAllowed(true).numberValidRange(-10000, 20000).build()),
 * 	BIGDECIMAL_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("bigDecimalCombo").label("BigDecimal Combo").defaultValue(67890.12345).build(), new FieldEditorDetailsBuilder(BigDecimalComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("BigDecimal Value", -10.5).put("invalid", 1000000)).emptyStringAllowed(false).numberValidRange(-1000, 100000).textLimit(20).build()),
 * 
 * 	BOOLEAN(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("boolean").label("Boolean").defaultValue(false).build(), new FieldEditorDetailsBuilder(DefaultBooleanFieldEditor.class).build()),
 * 	COLOR(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("color").label("Color").defaultValue("255,0,0").build(), new FieldEditorDetailsBuilder(ColorFieldEditor.class).build()),
 * 	PASSWORD(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("password").label("Password").build(), new FieldEditorDetailsBuilder(PasswordFieldEditor.class).build()),
 * 	DATE(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("date").label("Date").defaultValue("24/12/2015").build(), new FieldEditorDetailsBuilder(DateFieldEditor.class).datePattern("dd/MM/yyyy").dateFrom(new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime()).style(SWT.DROP_DOWN).build()),
 * 
 * 	EMAIL(new PreferenceDetailsBuilder(MyPageDefinition.PAGE).name("emails").label("Emails").build(), new FieldEditorDetailsBuilder(EmailAddressesListEditor.class).build()),
 * 	URI(new PreferenceDetailsBuilder(MyPageDefinition.PAGE).name("uris").label("URIs").build(), new FieldEditorDetailsBuilder(UriListEditor.class).build());
 * 
 * 	private PreferenceDetails preferenceDetails;
 * 	private FieldEditorDetails fieldEditorDetails;
 * 
 * 	MyPreference(PreferenceDetails preferenceDetails, FieldEditorDetails fieldEditorDetails) {
 * 		this.preferenceDetails = preferenceDetails;
 * 		this.fieldEditorDetails = fieldEditorDetails;
 * 	}
 * 
 * 	&#64;Override
 * 	public String getName() {
 * 		return preferenceDetails.getName();
 * 	}
 * 
 * 	&#64;Override
 * 	public String getLabel() {
 * 		return preferenceDetails.getLabel().getString();
 * 	}
 * 
 * 	&#64;Override
 * 	public IPageDefinition getPageDefinition() {
 * 		return preferenceDetails.getPageDefinition();
 * 	}
 * 
 * 	&#64;Override
 * 	public String getDefaultValue() {
 * 		return preferenceDetails.getDefaultValue();
 * 	}
 * 
 * 	&#64;Override
 * 	public IPreference getParent() {
 * 		return preferenceDetails.getParent();
 * 	}
 * 
 * 	&#64;Override
 * 	public boolean isRestartRequired() {
 * 		return preferenceDetails.isRestartRequired();
 * 	}
 * 
 * 	&#64;Override
 * 	public boolean isSeparate() {
 * 		return preferenceDetails.isSeparate();
 * 	}
 * 
 * 	&#64;Override
 * 	public Set<? extends IPreference> getChildren() {
 * 		Set<MyPreference> preferences = EnumSet.noneOf(MyPreference.class);
 * 		for (MyPreference item : MyPreference.values()) {
 * 			if (this.equals(item.getParent())) {
 * 				preferences.add(item);
 * 			}
 * 		}
 * 		return preferences;
 * 	}
 * 
 * 	&#64;Override
 * 	public FieldEditor createFieldEditor(Composite parent) {
 * 		return fieldEditorFactory.createFieldEditor(getName(), getLabel(), parent, fieldEditorDetails);
 * 	}
 * }
 * </pre>
 * <p>
 * You can surely improve this code, for example introducing localization and
 * autodetermining <em>name</em> values using the enum names. This example makes
 * use of {@link it.albertus.jface.preference.PreferenceDetails
 * PreferenceDetails} and {@link it.albertus.jface.preference.FieldEditorDetails
 * FieldEditorDetails} helper classes and their respective builders.
 * </p>
 */
public interface IPreference {

	FieldEditorFactory fieldEditorFactory = new FieldEditorFactory();

	String getName();

	String getLabel();

	IPageDefinition getPageDefinition();

	String getDefaultValue();

	boolean isSeparate();

	IPreference getParent();

	boolean isRestartRequired();

	IPreference[] getChildren();

	FieldEditor createFieldEditor(Composite parent);

}
