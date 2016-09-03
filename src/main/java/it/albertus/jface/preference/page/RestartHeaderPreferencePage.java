package it.albertus.jface.preference.page;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class RestartHeaderPreferencePage extends BasePreferencePage {

	@Override
	protected Control createHeader() {
		// return createNoteComposite(null, getFieldEditorParent(), JFaceResources.get("lbl.preferences.header.restart"), "");
		final Label header = new Label(getFieldEditorParent(), SWT.WRAP);
		TextFormatter.setBoldFontStyle(header);
		header.setText(JFaceMessages.get("lbl.preferences.header.restart"));
		return header;
	}

}
