JFaceUtils
==========

[![Build Status](https://travis-ci.org/Albertus82/JFaceUtils.svg?branch=master)](https://travis-ci.org/Albertus82/JFaceUtils)

**Java SWT/JFace Utilities Library** with a **Preferences Framework**.

![Screenshot](https://cloud.githubusercontent.com/assets/8672431/18028808/b4825704-6c87-11e6-96db-79f1fc46f931.png)

## Example of [`FieldEditorFactory`](src/main/java/it/albertus/jface/preference/FieldEditorFactory.java) extension

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

