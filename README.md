JFaceUtils
==========

[![Build Status](https://travis-ci.org/Albertus82/JFaceUtils.svg?branch=master)](https://travis-ci.org/Albertus82/JFaceUtils)

**Java SWT/JFace Utilities Library** with a **Preferences Framework**.

![Screenshot](https://cloud.githubusercontent.com/assets/8672431/18028808/b4825704-6c87-11e6-96db-79f1fc46f931.png)

## The Preferences Framework

The creation of a **preferences dialog** to manage the configuration of a SWT/JFace application can be a very annoying and time consuming task: you have to create every single field editor, they can be a lot and you may have to split them across multiple pages. Moreover, the basic JFace's `FieldEditor` classes aren't very flexible.

This framework will allow you to create a complete preferences dialog by writing only two enums, and includes several customizable `FieldEditor` classes with localization support and other useful features.

### Getting started

In order to open a preferences dialog, you must instantiate a [`Preferences`](src/main/java/it/albertus/jface/preference/Preferences.java) object and invoke one of its `openDialog` methods (e.g. from a `SelectionListener`). The [`Preferences`](src/main/java/it/albertus/jface/preference/Preferences.java) constructors take three or four arguments:
* [`IPageDefinition[]`](src/main/java/it/albertus/jface/preference/page/IPageDefinition.java): definitions of the pages that will contain the preference items;
* [`IPreference[]`](src/main/java/it/albertus/jface/preference/IPreference.java): the preference items;
* [`IPreferencesCallback`](src/main/java/it/albertus/jface/preference/IPreferencesCallback.java): the object that updates the application properties;
* `Image[]`: icons used for the preference dialogs (optional).

A convenient approach may be to implement [`IPageDefinition`](src/main/java/it/albertus/jface/preference/page/IPageDefinition.java) and [`IPreference`](src/main/java/it/albertus/jface/preference/IPreference.java) interfaces using enums, like in the following code examples.

#### Page definition enum

This is a very simple example of enum that implements [`IPageDefinition`](src/main/java/it/albertus/jface/preference/page/IPageDefinition.java):

```java
public enum MyPageDefinition implements IPageDefinition {

	TEXT(new PageDefinitionDetailsBuilder().nodeId("text").label("Text").build()),
	TEXT_NUMERIC(new PageDefinitionDetailsBuilder().nodeId("text.numeric").parent(TEXT).label("Numeric").build()),
	COMBO(new PageDefinitionDetailsBuilder().nodeId("combo").label("Combo").build()),
	COMBO_NUMERIC(new PageDefinitionDetailsBuilder().nodeId("combo.numeric").parent(COMBO).label("Numeric").build()),
	PAGE(new PageDefinitionDetailsBuilder().nodeId("page").label("Page").build()),
	VARIOUS(new PageDefinitionDetailsBuilder().nodeId("various").label("Various").build());

	private PageDefinitionDetails pageDefinitionDetails;

	MyPageDefinition() {
		this(new PageDefinitionDetailsBuilder().build());
	}

	MyPageDefinition(PageDefinitionDetails pageDefinitionDetails) {
		this.pageDefinitionDetails = pageDefinitionDetails;
	}

	@Override
	public String getNodeId() {
		return pageDefinitionDetails.getNodeId();
	}

	@Override
	public String getLabel() {
		return pageDefinitionDetails.getLabel().getString();
	}

	@Override
	public Class<? extends BasePreferencePage> getPageClass() {
		return pageDefinitionDetails.getPageClass();
	}

	@Override
	public IPageDefinition getParent() {
		return pageDefinitionDetails.getParent();
	}

	@Override
	public ImageDescriptor getImage() {
		return pageDefinitionDetails.getImage();
	}

}
```

You can surely improve this code, for example introducing localization and autodetermining `nodeId` values using the enum names. This example makes use of [`PageDefinitionDetails`](src/main/java/it/albertus/jface/preference/page/PageDefinitionDetails.java) helper class and its builder.

#### Preference enum

This is a simple example of enum that implements [`IPreference`](src/main/java/it/albertus/jface/preference/IPreference.java):

```java
public enum MyPreference implements IPreference {

	STRING(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("string").label("String").defaultValue("Hello World!").build(), new FieldEditorDetailsBuilder(DefaultStringFieldEditor.class).build()),
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
```

You can surely improve this code, for example introducing localization and autodetermining `name` values using the enum names. This example makes use of [`PreferenceDetails`](src/main/java/it/albertus/jface/preference/PreferenceDetails.java) and [`FieldEditorDetails`](src/main/java/it/albertus/jface/preference/FieldEditorDetails.java) helper classes and their respective builders.

#### Callback object

The interface [`IPreferencesCallback`](src/main/java/it/albertus/jface/preference/IPreferencesCallback.java) declares two methods:
* **`getFileName`**: must return the path and name of your configuration file.
* **`reload`**: must **reload the configuration file and update your in-memory configuration properties**, so that your application can see the updated values. This method is invoked automatically when necessary (callback).

You can manually implement [`IPreferencesCallback`](src/main/java/it/albertus/jface/preference/IPreferencesCallback.java) or [`PreferencesCallback`](src/main/java/it/albertus/jface/preference/PreferencesCallback.java) or use/extend [`PropertiesConfiguration`] (src/main/java/it/albertus/util/PropertiesConfiguration.java) or [`Configuration`](src/main/java/it/albertus/util/Configuration.java) depending on your needs.


### [`FieldEditorFactory`](src/main/java/it/albertus/jface/preference/FieldEditorFactory.java) extension

The [`IPreference`](src/main/java/it/albertus/jface/preference/IPreference.java) interface already declares a ready-to-use [`FieldEditorFactory`](src/main/java/it/albertus/jface/preference/FieldEditorFactory.java) variable:

```java
public static final FieldEditorFactory fieldEditorFactory = new FieldEditorFactory();
```

If you need to create your custom `FieldEditor` classes, you can extend [`FieldEditorFactory`](src/main/java/it/albertus/jface/preference/FieldEditorFactory.java) to add support of these new objects:

```java
public class MyFieldEditorFactory extends FieldEditorFactory {

	@Override
	public FieldEditor createFieldEditor(String name, String label, Composite parent, FieldEditorDetails details) {
		Class<? extends FieldEditor> type = details.getFieldEditorClass();

		if (MyCustomFieldEditor.class.equals(type)) {
			return new MyCustomFieldEditor(name, label, parent);
		}
		if (AnotherCustomFieldEditor.class.equals(type)) {
			return new AnotherCustomFieldEditor(name, label, details.getLabelsAndValues().toArray(), parent);
		}

		return super.createFieldEditor(name, label, parent, details);
	}
}
```

After that, you can use your new factory instead of the standard one made available by [`IPreference`](src/main/java/it/albertus/jface/preference/IPreference.java).
