JFaceUtils
==========

[![Maven Central](https://img.shields.io/maven-central/v/io.github.albertus82/jface-utils)](https://search.maven.org/artifact/io.github.albertus82/jface-utils)
[![Build status](https://github.com/albertus82/jface-utils/actions/workflows/build.yml/badge.svg)](https://github.com/albertus82/jface-utils/actions)
[![Known Vulnerabilities](https://snyk.io/test/github/albertus82/jface-utils/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/albertus82/jface-utils?targetFile=pom.xml)

### Java SWT/JFace Utility Library including a Preferences Framework

This library is meant to support the development of small footprint Java desktop applications with graphical user interface. Some non GUI utility classes are also included, aiming to improve some basic Java features such as logging, configuration, I/O and the lightweight HTTP server.

## Usage

### Maven

> :warning: Starting from version 18.0.0, the **Maven Group ID** and the **Java base package name** changed from `it.albertus` to `io.github.albertus82` (to know the reasons behind this change, see [Choosing your Coordinates - The Central Repository Documentation](https://central.sonatype.org/publish/requirements/coordinates/)).

Add the following element to your project's `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.albertus82</groupId>
    <artifactId>jface-utils</artifactId>
    <version>19.0.3</version>
</dependency>
```

### Manual download

You can download the JARs from the [releases page](https://github.com/Albertus82/JFaceUtils/releases/latest).

## The Preferences Framework

The creation of a **preferences dialog** to manage the configuration of a SWT/JFace application can be an annoying and time consuming task: you have to create every single field; these fields could be a lot, so you might want to split them across multiple pages. Moreover, the basic JFace's `FieldEditor` classes aren't very flexible.

This framework will allow you to create a complete preferences dialog by writing only two enums, and includes several customizable `FieldEditor` classes with localization support and other useful features.

![Screenshot](https://cloud.githubusercontent.com/assets/8672431/19839894/0b8fe44a-9eeb-11e6-8ff4-c3b4f321c2b8.png)

### Getting started

In order to open a preferences dialog, you must instantiate a [`Preferences`](src/main/java/io/github/albertus82/jface/preference/Preferences.java) object and invoke one of its `openDialog` methods (e.g. from a `SelectionListener`). The [`Preferences`](src/main/java/io/github/albertus82/jface/preference/Preferences.java) constructors take three or four arguments:
* [`IPageDefinition[]`](src/main/java/io/github/albertus82/jface/preference/page/IPageDefinition.java): definitions of the pages that will contain the preference items;
* [`IPreference[]`](src/main/java/io/github/albertus82/jface/preference/IPreference.java): the preference items;
* [`IPreferencesCallback`](src/main/java/io/github/albertus82/jface/preference/IPreferencesCallback.java): the object that updates the application properties;
* `Image[]`: icons used for the preference dialogs (optional).

A convenient approach may be to implement [`IPageDefinition`](src/main/java/io/github/albertus82/jface/preference/page/IPageDefinition.java) and [`IPreference`](src/main/java/io/github/albertus82/jface/preference/IPreference.java) interfaces using enums, like in the following code examples.

#### Page definition enum

This is a very simple example of enum that implements [`IPageDefinition`](src/main/java/io/github/albertus82/jface/preference/page/IPageDefinition.java):

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
        return pageDefinitionDetails.getLabel().get();
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

You can surely improve this code, for example introducing localization and autodetermining `nodeId` values using the enum names. This example makes use of [`PageDefinitionDetails`](src/main/java/io/github/albertus82/jface/preference/page/PageDefinitionDetails.java) helper class and its builder.

#### Preference enum

This is a simple example of enum that implements [`IPreference`](src/main/java/io/github/albertus82/jface/preference/IPreference.java):

```java
public enum MyPreference implements IPreference {

    STRING(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("string").label("String").defaultValue("Hello World!").build(), new FieldEditorDetailsBuilder(DefaultStringFieldEditor.class).build()),
    WRAP_STRING(new PreferenceDetailsBuilder(MyPageDefinition.TEXT).name("wrapString").label("Wrap String").defaultValue("Long text here.").build(), new FieldEditorDetailsBuilder(WrapStringFieldEditor.class).build()),

    INTEGER(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("integer").label("Integer").defaultValue(12345).build(), new FieldEditorDetailsBuilder(EnhancedIntegerFieldEditor.class).emptyStringAllowed(true).numberMinimum(-67890).build()),
    DOUBLE(new PreferenceDetailsBuilder(MyPageDefinition.TEXT_NUMERIC).name("double").label("Double").defaultValue(24680.13579).build(), new FieldEditorDetailsBuilder(DoubleFieldEditor.class).emptyStringAllowed(true).build()),

    COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("combo").label("Combo").defaultValue("value 1").build(), new FieldEditorDetailsBuilder(DefaultComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("Label 1", "value 1").put("Label 2", "value 2")).build()),
    VALIDATED_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO).name("validatedCombo").label("Validated Combo").defaultValue("value 5").build(), new FieldEditorDetailsBuilder(ValidatedComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("Label 5", "value 5")).emptyStringAllowed(false).build()),

    FLOAT_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).separate().name("floatCombo").label("Float Combo").defaultValue(123.456f).build(), new FieldEditorDetailsBuilder(FloatComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("float", 1)).emptyStringAllowed(true).numberValidRange(-10000, 20000).build()),
    BIGDECIMAL_COMBO(new PreferenceDetailsBuilder(MyPageDefinition.COMBO_NUMERIC).name("bigDecimalCombo").label("BigDecimal Combo").defaultValue(67890.12345).build(), new FieldEditorDetailsBuilder(BigDecimalComboFieldEditor.class).labelsAndValues(new StaticLabelsAndValues("BigDecimal Value", -10.5).put("invalid", 1000000)).emptyStringAllowed(false).numberValidRange(-1000, 100000).textLimit(20).build()),

    BOOLEAN(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("boolean").label("Boolean").defaultValue(false).build(), new FieldEditorDetailsBuilder(DefaultBooleanFieldEditor.class).build()),
    COLOR(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("color").label("Color").defaultValue("255,0,0").build(), new FieldEditorDetailsBuilder(ColorFieldEditor.class).build()),
    PASSWORD(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("password").label("Password").build(), new FieldEditorDetailsBuilder(PasswordFieldEditor.class).build()),
    DATE(new PreferenceDetailsBuilder(MyPageDefinition.VARIOUS).name("date").label("Date").defaultValue("24/12/2015").build(), new FieldEditorDetailsBuilder(DateFieldEditor.class).datePattern("dd/MM/yyyy").dateFrom(new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime()).style(SWT.DROP_DOWN).build()),

    EMAIL(new PreferenceDetailsBuilder(MyPageDefinition.PAGE).name("emails").label("Emails").build(), new FieldEditorDetailsBuilder(EmailAddressesListEditor.class).build()),
    URI(new PreferenceDetailsBuilder(MyPageDefinition.PAGE).name("uris").label("URIs").build(), new FieldEditorDetailsBuilder(UriListEditor.class).build());

    private static final FieldEditorFactory fieldEditorFactory = new FieldEditorFactory();

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
        Set<MyPreference> preferences = EnumSet.noneOf(MyPreference.class);
        for (MyPreference item : MyPreference.values()) {
            if (this.equals(item.getParent())) {
                preferences.add(item);
            }
        }
        return preferences.toArray(new IPreference[] {});
    }

    @Override
    public FieldEditor createFieldEditor(Composite parent) {
        return fieldEditorFactory.createFieldEditor(getName(), getLabel(), parent, fieldEditorDetails);
    }
}
```

You can surely improve this code, for example introducing localization and autodetermining `name` values using the enum names. This example makes use of [`PreferenceDetails`](src/main/java/io/github/albertus82/jface/preference/PreferenceDetails.java) and [`FieldEditorDetails`](src/main/java/io/github/albertus82/jface/preference/FieldEditorDetails.java) helper classes and their respective builders.

#### Callback object

The interface [`IPreferencesCallback`](src/main/java/io/github/albertus82/jface/preference/IPreferencesCallback.java) declares two methods:
* **`getFileName`**: must return the path and name of your configuration file.
* **`reload`**: must **reload the configuration file and update your in-memory configuration properties**, so that your application can see the updated values. This method is invoked automatically when necessary (callback).

You can manually implement [`IPreferencesCallback`](src/main/java/io/github/albertus82/jface/preference/IPreferencesCallback.java) or use/extend [`PropertiesConfiguration`](src/main/java/io/github/albertus82/util/config/PropertiesConfiguration.java) or [`Configuration`](src/main/java/io/github/albertus82/util/config/Configuration.java) depending on your needs.


### [`FieldEditorFactory`](src/main/java/io/github/albertus82/jface/preference/FieldEditorFactory.java)

The [`FieldEditorFactory`](src/main/java/io/github/albertus82/jface/preference/FieldEditorFactory.java) helps you to create `FieldEditor` objects, as you saw in the previous [`IPreference`](src/main/java/io/github/albertus82/jface/preference/IPreference.java) example.

By default, custom values are presented in *bold* format. If you don't like this behaviour, you can disable it invoking the `setBoldCustomValues(boolean)` method of [`FieldEditorFactory`](src/main/java/io/github/albertus82/jface/preference/FieldEditorFactory.java):

```java
public enum MyPreference implements IPreference {

    /* Enum values... */

    private static final FieldEditorFactory fieldEditorFactory = new FieldEditorFactory();

    static {
        fieldEditorFactory.setBoldCustomValues(false);
    }
}
```

#### Extension

If you need to create your custom `FieldEditor` classes, you can extend [`FieldEditorFactory`](src/main/java/io/github/albertus82/jface/preference/FieldEditorFactory.java) to add support for these new objects:

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

After that, you can use your new factory instead of the default one.

## macOS integration with [`CocoaUIEnhancer`](src/main/java/io/github/albertus82/jface/cocoa/CocoaUIEnhancer.java)

The [`CocoaUIEnhancer`](src/main/java/io/github/albertus82/jface/cocoa/CocoaUIEnhancer.java) class provides a hook to connect the **Preferences**, **About** and **Quit** menu items of the **macOS** application menu.
This is a modified version of the [`CocoaUIEnhancer`](http://www.transparentech.com/files/CocoaUIEnhancer.java) class available at [TransparenTech](http://www.transparentech.com/opensource/cocoauienhancer), and it is released under the Eclipse Public License ([EPL](https://www.eclipse.org/legal/epl-v10.html)).

In order to better integrate your JFace application with macOS, you should first call the following static methods of `Display` before its creation:

```java
Display.setAppName("My JFace Application");
Display.setAppVersion("1.2.3");
```

Next, you can use [`CocoaUIEnhancer`](src/main/java/io/github/albertus82/jface/cocoa/CocoaUIEnhancer.java) before opening the shell:

```java
if (Util.isCocoa()) {
    new CocoaUIEnhancer(getShell().getDisplay()).hookApplicationMenu(new CloseListener(), new AboutListener(), new PreferencesListener());
}
```

The `hookApplicationMenu` method is overloaded in order to accept **SWT Listeners** or **JFace Actions** for **About** and **Preferences** functions. When one argument is `null`, then the respective menu item will be disabled; so, for instance, if your application does not have a preferences management, you can pass `null` in place of `preferencesListener` or `preferencesAction` and the **Preferences...** menu item will be grayed out.

## SWT Closeable Resources

SWT uses operating system resources to deliver its native graphics and widget functionality. These resources should be freed when no longer needed, and the traditional way to do it is calling the `dispose()` method on the objects that represent the resources, however this approach may be error prone.

Now, if you need to instantiate a `Widget`, `Resource` (like a `GC`), `Device` or `Clipboard` and you want to make sure that its resources will be released after use, you can use the `Closeable` wrappers available in the `io.github.albertus82.jface.closeable` package with a *try-for-resources* statement. The wrapped object will be disposed automatically after the `try` block like any other *closeable* resource, so you will not have to invoke its `dispose()` method.

### Example

#### Without `Closeable` wrapper - *not recommended*:

```java
GC gc = null;
try {
    gc = new GC(canvas);
    Rectangle canvasBounds = canvas.getBounds();
    gc.fillRectangle(0, 0, canvasBounds.width, canvasBounds.height);
}
finally {
    if (gc != null)
        gc.dispose();
}
```

#### *try-for-resources* with `Closeable` wrapper:

```java
try (CloseableResource<GC> wrapper = new CloseableResource<>(new GC(canvas))) {
    GC gc = wrapper.getResource();
    Rectangle canvasBounds = canvas.getBounds();
    gc.fillRectangle(0, 0, canvasBounds.width, canvasBounds.height);
}
```
