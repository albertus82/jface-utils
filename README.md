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

A convenient approach may be the use of enums for [`Page`](src/main/java/it/albertus/jface/preference/page/Page.java) and [`Preference`](src/main/java/it/albertus/jface/preference/Preference.java) objects, like in the following code samples.

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

	private MyApplicationPage(String nodeId, String label, Class<? extends AbstractPreferencePage> pageClass, Page parent) {
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

	public static Page forClass(final Class<? extends AbstractPreferencePage> clazz) {
		if (clazz != null) {
			for (final MyApplicationPage page : MyApplicationPage.values()) {
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

```java

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
			return new MyCustomFieldEditor(name, label, data.getLabelsAndValues().toArray(), parent);
		}
		if (AnotherCustomFieldEditor.class.equals(type)) {
			return new AnotherCustomFieldEditor(name, label, parent);
		}
		return super.createFieldEditor(type, name, label, parent, data);
	}
}
```
