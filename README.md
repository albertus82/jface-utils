JFaceUtils
==========

[![Build Status](https://travis-ci.org/Albertus82/JFaceUtils.svg?branch=master)](https://travis-ci.org/Albertus82/JFaceUtils)

**Java SWT/JFace Utilities Library** with a **Preferences Framework**.

![Screenshot](https://cloud.githubusercontent.com/assets/8672431/18028808/b4825704-6c87-11e6-96db-79f1fc46f931.png)

## Preferences Framework

### Getting started

In order to open a Preferences dialog, you must instantiate a [`Preferences`](src/main/java/it/albertus/jface/preference/Preferences.java) object and invoke one of its `openDialog` method (e.g. from a `SelectionListener`). The [`Preferences`](src/main/java/it/albertus/jface/preference/Preferences.java) constructors take three or four arguments:
* [`Configuration`](src/main/java/it/albertus/util/Configuration.java): the application's configuration object;
* [`PageDefinition[]`](src/main/java/it/albertus/jface/preference/page/PageDefinition.java): preference pages that contains preference items;
* [`Preference[]`](src/main/java/it/albertus/jface/preference/Preference.java): preference items;
* `Image[]`: icons used for the Preference dialogs (optional).

A convenient approach may be to use enums for [`PageDefinition`](src/main/java/it/albertus/jface/preference/page/PageDefinition.java) and [`Preference`](src/main/java/it/albertus/jface/preference/Preference.java) objects, like in the following code samples.

#### PageDefinition enum

This is a very simple example of enum that implements [`PageDefinition`](src/main/java/it/albertus/jface/preference/page/PageDefinition.java):

```java
public enum MyPageDefinition implements PageDefinition {

	GENERAL("general", "General", GeneralPreferencePage.class, null),
	APPEARANCE("appearance", "Appearance", AppearancePreferencePage.class, null),
	COLORS("colors", "Colors", ColorsPreferencePage.class, APPEARANCE);

	private String nodeId;
	private String label;
	private Class<? extends AbstractPreferencePage> pageClass;
	private PageDefinition parent;

	MyPageDefinition(String nodeId, String label, Class<? extends AbstractPreferencePage> pageClass, PageDefinition parent) {
		this.nodeId = nodeId;
		this.label = label;
		this.pageClass = pageClass;
		this.parent = parent;
	}

	@Override
	public String getNodeId() {
		return nodeId;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Class<? extends AbstractPreferencePage> getPageClass() {
		return pageClass;
	}

	@Override
	public PageDefinition getParent() {
		return parent;
	}
}
```

You can surely improve this code, for example introducing localization, autodetermining `nodeId` values using the enum names, adding an overloaded constructor that doesn't require the `parent` argument, and so on.

#### Preference enum

This is a very simple example of enum that implements [`Preference`](src/main/java/it/albertus/jface/preference/Preference.java).

```java
public enum MyPreference implements Preference {

	AUTHENTICATION(MyPageDefinition.GENERAL, DefaultBooleanFieldEditor.class, new PreferenceDataBuilder().configurationKey("authentication").label("Enable authentication").defaultValue(true).restartRequired().build(), null),
	PASSWORD(MyPageDefinition.GENERAL, PasswordFieldEditor.class, new PreferenceDataBuilder().configurationKey("password").label("Password").parent(AUTHENTICATION).build(), null),
	PORT(MyPageDefinition.GENERAL, DefaultIntegerFieldEditor.class, new PreferenceDataBuilder().configurationKey("port").label("Port").separator().defaultValue(8080).build(), new FieldEditorDataBuilder().integerValidRange(1, 65535).build()),
	DEBUG(MyPageDefinition.GENERAL, DefaultBooleanFieldEditor.class, new PreferenceDataBuilder().configurationKey("debug").label("Enable debug mode").separator().defaultValue(false).build(), null),
	CONFIRM_CLOSE(MyPageDefinition.APPEARANCE, DefaultBooleanFieldEditor.class, new PreferenceDataBuilder().configurationKey("confirmClose").label("Confirm close").defaultValue(false).build(), null),
	FONT_COLOR(MyPageDefinition.COLORS, ColorFieldEditor.class, new PreferenceDataBuilder().configurationKey("fontColor").label("Font color").defaultValue("255,0,0").build(), null),
	BACKGROUND_COLOR(MyPageDefinition.COLORS, ColorFieldEditor.class, new PreferenceDataBuilder().configurationKey("backgroundColor").label("Background color").defaultValue("255,255,255").build(), null);

	private static final FieldEditorFactory fieldEditorFactory = new FieldEditorFactory();

	private PageDefinition pageDefinition;
	private Class<? extends FieldEditor> fieldEditorType;
	private PreferenceData preferenceData;
	private FieldEditorData fieldEditorData;

	MyPreference(Page page, Class<? extends FieldEditor> fieldEditorType, PreferenceData preferenceData, FieldEditorData fieldEditorData) {
		this.page = page;
		this.fieldEditorType = fieldEditorType;
		this.preferenceData = preferenceData;
		this.fieldEditorData = fieldEditorData;
	}

	@Override
	public String getConfigurationKey() {
		return preferenceData.getConfigurationKey();
	}

	@Override
	public String getLabel() {
		return preferenceData.getLabel().getString();
	}

	@Override
	public Page getPage() {
		return page;
	}

	@Override
	public String getDefaultValue() {
		return preferenceData.getDefaultValue();
	}

	@Override
	public Preference getParent() {
		return preferenceData.getParent();
	}

	@Override
	public boolean isRestartRequired() {
		return preferenceData.isRestartRequired();
	}

	@Override
	public boolean hasSeparator() {
		return preferenceData.hasSeparator();
	}

	@Override
	public Set<? extends Preference> getChildren() {
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
		return fieldEditorFactory.createFieldEditor(fieldEditorType, getConfigurationKey(), getLabel(), parent, fieldEditorData);
	}
}
```

You can surely improve this code, for example introducing localization, autodetermining `configurationKey` values using the enum names, adding an overloaded constructor that doesn't require the `fieldEditorData` argument, and so on. This example makes use of [`PreferenceData`](src/main/java/it/albertus/jface/preference/PreferenceData.java) and [`FieldEditorData`](src/main/java/it/albertus/jface/preference/FieldEditorData.java) helper classes and their respective builders.

#### PreferencePage classes

This is a very simple example of class that extends [`AbstractPreferencePage`](src/main/java/it/albertus/jface/preference/page/AbstractPreferencePage.java). You will need one class per [`PageDefinition`](src/main/java/it/albertus/jface/preference/page/PageDefinition.java) enum constant. The two constructor signatures are mandatory. Lastly, `getPageDefinition` method must return the enum constant associated with this page class.

```java
public class GeneralPreferencePage extends AbstractPreferencePage {

	public GeneralPreferencePage() {
		super(MyConfiguration.getInstance(), MyPreference.values());
	}

	protected GeneralPreferencePage(final int style) {
		super(MyConfiguration.getInstance(), MyPreference.values(), style);
	}

	@Override
	public PageDefinition getPageDefinition() {
		return MyPageDefinition.GENERAL;
	}
}
```

You can easily improve this architecture by writing another abstract class that extends [`AbstractPreferencePage`](src/main/java/it/albertus/jface/preference/page/AbstractPreferencePage.java) and recalls the super constructors with that static parameters; you can also write a static lookup method in your [`PageDefinition`](src/main/java/it/albertus/jface/preference/page/PageDefinition.java) enum so that you can override the `getPageDefinition` method directly in your abstract superclass.

### Example of [`FieldEditorFactory`](src/main/java/it/albertus/jface/preference/FieldEditorFactory.java) extension

```java
public class MyFieldEditorFactory extends FieldEditorFactory {

	@Override
	public FieldEditor createFieldEditor(Class<? extends FieldEditor> type, String name, String label, Composite parent, FieldEditorData data) {
		if (MyCustomFieldEditor.class.equals(type)) {
			return new MyCustomFieldEditor(name, label, parent);
		}
		if (AnotherCustomFieldEditor.class.equals(type)) {
			return new AnotherCustomFieldEditor(name, label, data.getLabelsAndValues().toArray(), parent);
		}
		return super.createFieldEditor(type, name, label, parent, data);
	}
}
```
