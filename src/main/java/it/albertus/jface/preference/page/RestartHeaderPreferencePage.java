package it.albertus.jface.preference.page;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class RestartHeaderPreferencePage extends BasePreferencePage {

	private String restartMessage = JFaceMessages.get("lbl.preferences.header.restart");

	@Override
	protected Control createHeader() {
		// return createNoteComposite(null, getFieldEditorParent(), message, "");
		final Label header = new Label(getFieldEditorParent(), SWT.WRAP);
		TextFormatter.setBoldFontStyle(header);
		header.setText(restartMessage);
		return header;
	}

	public String getRestartMessage() {
		return restartMessage;
	}

	public void setRestartMessage(final String restartMessage) {
		this.restartMessage = restartMessage;
	}

}
