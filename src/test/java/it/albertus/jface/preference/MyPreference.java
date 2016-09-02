package it.albertus.jface.preference;

import it.albertus.jface.preference.FieldEditorDetails.FieldEditorDetailsBuilder;
import it.albertus.jface.preference.PreferenceDetails.PreferenceDetailsBuilder;
import it.albertus.jface.preference.field.DefaultBooleanFieldEditor;
import it.albertus.jface.preference.field.DefaultIntegerFieldEditor;
import it.albertus.jface.preference.field.PasswordFieldEditor;
import it.albertus.jface.preference.page.IPageDefinition;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;

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
