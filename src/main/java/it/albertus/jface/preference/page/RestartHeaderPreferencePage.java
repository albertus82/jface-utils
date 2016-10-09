package it.albertus.jface.preference.page;

import org.eclipse.swt.widgets.Control;

import it.albertus.jface.JFaceMessages;

public class RestartHeaderPreferencePage extends BasePreferencePage {

	private String restartMessage = JFaceMessages.get("lbl.preferences.header.restart");

	@Override
	protected Control createHeader() {
		return createNoteComposite(null, getFieldEditorParent(), restartMessage, "");
	}

	public String getRestartMessage() {
		return restartMessage;
	}

	public void setRestartMessage(final String restartMessage) {
		this.restartMessage = restartMessage;
	}

}
