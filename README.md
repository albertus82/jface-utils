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

#### PreferencePage classes

```java

```

#### Page enum

```java

```

#### Preference enum

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
