package it.albertus.jface.preference.field;

import it.albertus.jface.Formatter;

public interface FieldEditorDefault {

	Formatter formatter = new Formatter(FieldEditorDefault.class);

	boolean isDefaultToolTip();

	boolean isBoldCustomValues();

	void setDefaultToolTip(boolean defaultToolTip);

	void setBoldCustomValues(boolean boldCustomValues);

}
