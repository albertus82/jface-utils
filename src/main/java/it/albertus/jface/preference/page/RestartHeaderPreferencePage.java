package it.albertus.jface.preference.page;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;
import it.albertus.util.Localized;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class RestartHeaderPreferencePage extends BasePreferencePage {

	private Localized restartMessage = new Localized() {
		@Override
		public String getString() {
			return JFaceMessages.get("lbl.preferences.header.restart");
		}
	};

	@Override
	protected Control createHeader() {
		// return createNoteComposite(null, getFieldEditorParent(), message, "");
		final Label header = new Label(getFieldEditorParent(), SWT.WRAP);
		TextFormatter.setBoldFontStyle(header);
		header.setText(restartMessage.getString());
		return header;
	}

	public String getRestartMessage() {
		return restartMessage.getString();
	}

	public void setRestartMessage(final Localized restartMessage) {
		this.restartMessage = restartMessage;
	}

	public void setRestartMessage(final String restartMessage) {
		this.restartMessage = new Localized() {
			@Override
			public String getString() {
				return restartMessage;
			}
		};
	}

}
