JFaceUtils
==========

[![Build Status](https://travis-ci.org/Albertus82/JFaceUtils.svg?branch=master)](https://travis-ci.org/Albertus82/JFaceUtils)

**Java SWT/JFace Utilities Library** with a **Preferences Framework**.

![Screenshot](https://cloud.githubusercontent.com/assets/8672431/18028808/b4825704-6c87-11e6-96db-79f1fc46f931.png)

## Preferences Framework

### Getting started

In order to open a Preferences dialog, you must instantiate a [`Preferences`](src/main/java/it/albertus/jface/preference/Preferences.java) object (e.g. from a `SelectionListener`) and invoke its `open` method. The [`Preferences`](src/main/java/it/albertus/jface/preference/Preferences.java) constructors takes three or four arguments:
* [`Configuration`](src/main/java/it/albertus/util/Configuration.java): the application's configuration object;
* [`Page[]`](src/main/java/it/albertus/jface/preference/page/Page.java): preference pages that contains preference items;
* [`Preference[]`](src/main/java/it/albertus/jface/preference/Preference.java): preference items;
* `Image[]`: icons used for the Preference dialogs (optional).

A convenient approach may be to use enums for [`Page`](src/main/java/it/albertus/jface/preference/page/Page.java) and [`Preference`](src/main/java/it/albertus/jface/preference/Preference.java) objects, like in the following code samples.

#### Page enum

This is a very simple example of enum that implements [`Page`](src/main/java/it/albertus/jface/preference/page/Page.java). You can surely improve it, for example introducing localization, autodetermining `nodeId` values using the enum names, adding an overloaded constructor that doesn't require the `parent` argument, and so on.

```java
public enum MyApplicationPage implements Page {

	GENERAL("general", "General", GeneralPreferencePage.class, null),
	APPEARANCE("appearance", "Appearance", AppearancePreferencePage.class, null),
	COLORS("colors", "Colors", ColorsPreferencePage.class, APPEARANCE);

	private String nodeId;
	private String label;
	private Class<? extends AbstractPreferencePage> pageClass;
	private Page parent;

	MyApplicationPage(String nodeId, String label, Class<? extends AbstractPreferencePage> pageClass, Page parent) {
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
	public Page getParent() {
		return parent;
	}

	public static Page forClass(Class<? extends AbstractPreferencePage> clazz) {
		if (clazz != null) {
			for (MyApplicationPage page : MyApplicationPage.values()) {
				if (clazz.equals(page.pageClass)) {
					return page;
				}
			}
		}
		return null;
	}
}
```

#### Preference enum

This is a very simple example of enum that implements [`Preference`](src/main/java/it/albertus/jface/preference/Preference.java). You can surely improve it, for example introducing localization, autodetermining `configurationKey` values using the enum names, adding an overloaded constructor that doesn't require the `fieldEditorData` argument, and so on. This example makes use of [`PreferenceData`](src/main/java/it/albertus/jface/preference/PreferenceData.java) and [`FieldEditorData`](src/main/java/it/albertus/jface/preference/FieldEditorData.java) helper classes and their respective builders.

```java
public enum MyApplicationPreference implements Preference {

	AUTHENTICATION(MyApplicationPage.GENERAL, DefaultBooleanFieldEditor.class, new PreferenceDataBuilder().configurationKey("authentication").label("Enable authentication").defaultValue(true).restartRequired().build(), null),
	PASSWORD(MyApplicationPage.GENERAL, PasswordFieldEditor.class, new PreferenceDataBuilder().configurationKey("password").label("Password").parent(AUTHENTICATION).build(), null),
	PORT(MyApplicationPage.GENERAL, DefaultIntegerFieldEditor.class, new PreferenceDataBuilder().configurationKey("port").label("Port").separator().defaultValue(8080).build(), new FieldEditorDataBuilder().integerValidRange(1, 65535).build()),
	DEBUG(MyApplicationPage.GENERAL, DefaultBooleanFieldEditor.class, new PreferenceDataBuilder().configurationKey("debug").label("Enable debug mode").separator().defaultValue(false).build(), null),
	CONFIRM_CLOSE(MyApplicationPage.APPEARANCE, DefaultBooleanFieldEditor.class, new PreferenceDataBuilder().configurationKey("confirmClose").label("Confirm close").defaultValue(false).build(), null),
	FONT_COLOR(MyApplicationPage.COLORS, ColorFieldEditor.class, new PreferenceDataBuilder().configurationKey("fontColor").label("Font color").defaultValue("255,0,0").build(), null),
	BACKGROUND_COLOR(MyApplicationPage.COLORS, ColorFieldEditor.class, new PreferenceDataBuilder().configurationKey("backgroundColor").label("Background color").defaultValue("255,255,255").build(), null);

	private static final FieldEditorFactory fieldEditorFactory = new FieldEditorFactory();

	private Page page;
	private Class<? extends FieldEditor> fieldEditorType;
	private PreferenceData preferenceData;
	private FieldEditorData fieldEditorData;

	MyApplicationPreference(Page page, Class<? extends FieldEditor> fieldEditorType, PreferenceData preferenceData, FieldEditorData fieldEditorData) {
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
		Set<MyApplicationPreference> preferences = EnumSet.noneOf(MyApplicationPreference.class);
		for (MyApplicationPreference item : MyApplicationPreference.values()) {
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

	public static Preference forConfigurationKey(String configurationKey) {
		if (configurationKey != null) {
			for (MyApplicationPreference preference : MyApplicationPreference.values()) {
				if (configurationKey.equals(preference.getConfigurationKey())) {
					return preference;
				}
			}
		}
		return null;
	}
}
```

#### PreferencePage classes

```java

```

### Example of [`FieldEditorFactory`](src/main/java/it/albertus/jface/preference/FieldEditorFactory.java) extension

```java
public class MyApplicationFieldEditorFactory extends FieldEditorFactory {

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
