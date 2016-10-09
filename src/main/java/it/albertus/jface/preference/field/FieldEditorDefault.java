package it.albertus.jface.preference.field;

import it.albertus.jface.FontFormatter;

public interface FieldEditorDefault {

	FontFormatter textFormatter = new FontFormatter(FieldEditorDefault.class.getName());

	boolean isDefaultToolTip();

	boolean isBoldCustomValues();

	void setDefaultToolTip(boolean defaultToolTip);

	void setBoldCustomValues(boolean boldCustomValues);

}
