JFaceUtils
==========

[![Build Status](https://travis-ci.org/Albertus82/JFaceUtils.svg?branch=master)](https://travis-ci.org/Albertus82/JFaceUtils)

**Java SWT/JFace Utilities Library** with a **Preferences Framework**.

![Screenshot](https://cloud.githubusercontent.com/assets/8672431/18028808/b4825704-6c87-11e6-96db-79f1fc46f931.png)

## The Preferences Framework

The creation of a **configuration dialog** for a SWT/JFace application can be a very annoying and time consuming task: you have to create every single field editor, they can be a lot and you may have to split them across multiple pages. Moreover, the basic JFaces' `FieldEditor` classes aren't very flexible. This framework will allow you to create a complete configuration dialog writing only two enums, and includes several customizable `FieldEditor` classes with localization support and other useful features.

### Getting started

In order to open a Preferences dialog, you must instantiate a [`Preferences`](src/main/java/it/albertus/jface/preference/Preferences.java) object and invoke one of its `openDialog` method (e.g. from a `SelectionListener`). The [`Preferences`](src/main/java/it/albertus/jface/preference/Preferences.java) constructors take three or four arguments:
* [`Configuration`](src/main/java/it/albertus/util/Configuration.java): the application's configuration object;
* [`IPageDefinition[]`](src/main/java/it/albertus/jface/preference/page/IPageDefinition.java): definitions of the pages that will contain the preference items;
* [`IPreference[]`](src/main/java/it/albertus/jface/preference/IPreference.java): the preference items;
* `Image[]`: icons used for the preference dialogs (optional).

A convenient approach may be to implement [`IPageDefinition`](src/main/java/it/albertus/jface/preference/page/IPageDefinition.java) and [`IPreference`](src/main/java/it/albertus/jface/preference/IPreference.java) interfaces using enums, like in the following code examples.

#### Page definition enum

This is a very simple example of enum that implements [`IPageDefinition`](src/main/java/it/albertus/jface/preference/page/IPageDefinition.java):

```java
public enum MyPageDefinition implements IPageDefinition {

	GENERAL(new PageDefinitionDetailsBuilder().nodeId("general").label("General").pageClass(GeneralPreferencePage.class).build()),
	APPEARANCE(new PageDefinitionDetailsBuilder().nodeId("appearance").label("Appearance").pageClass(RestartHeaderPreferencePage.class).build()),
	COLORS(new PageDefinitionDetailsBuilder().nodeId("appearance.colors").label("Colors").pageClass(ColorsPreferencePage.class).parent(APPEARANCE).build());

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

	AUTHENTICATION(new PreferenceDetailsBuilder(MyPageDefinition.GENERAL).name("authentication").label("Enable authentication").defaultValue(true).restartRequired().build(), new FieldEditorDetailsBuilder(DefaultBooleanFieldEditor.class).build()),
	PASSWORD(new PreferenceDetailsBuilder(MyPageDefinition.GENERAL).name("password").label("Password").parent(AUTHENTICATION).build(), new FieldEditorDetailsBuilder(PasswordFieldEditor.class).build()),
	PORT(new PreferenceDetailsBuilder(MyPageDefinition.GENERAL).name("port").label("Port").separate().defaultValue(8080).build(), new FieldEditorDetailsBuilder(DefaultIntegerFieldEditor.class).integerValidRange(1, 65535).build()),
	DEBUG(new PreferenceDetailsBuilder(MyPageDefinition.GENERAL).name("debug").label("Enable debug mode").separate().defaultValue(false).build(), new FieldEditorDetailsBuilder(DefaultBooleanFieldEditor.class).build()),
	CONFIRM_CLOSE(new PreferenceDetailsBuilder(MyPageDefinition.APPEARANCE).name("confirmClose").label("Confirm close").defaultValue(false).build(), new FieldEditorDetailsBuilder(DefaultBooleanFieldEditor.class).build()),
	FONT_COLOR(new PreferenceDetailsBuilder(MyPageDefinition.COLORS).name("fontColor").label("Font color").defaultValue("255,0,0").build(), new FieldEditorDetailsBuilder(ColorFieldEditor.class).build()),
	BACKGROUND_COLOR(new PreferenceDetailsBuilder(MyPageDefinition.COLORS).name("backgroundColor").label("Background color").defaultValue("255,255,255").build(), new FieldEditorDetailsBuilder(ColorFieldEditor.class).build());

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
	public IPreferencePageDefinition getPageDefinition() {
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

### [`FieldEditorFactory`](src/main/java/it/albertus/jface/preference/FieldEditorFactory.java) extension

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
