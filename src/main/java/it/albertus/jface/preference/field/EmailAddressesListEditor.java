package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class EmailAddressesListEditor extends LocalizedListEditor {

	public static final String EMAIL_ADDRESSES_SPLIT_REGEX = "[,;\\s]+";
	public static final char SEPARATOR = ',';

	private final Image[] images;

	public EmailAddressesListEditor(final String name, final String labelText, final Composite parent, final Integer horizontalSpan, final Image[] images) {
		super(name, labelText, parent, horizontalSpan);
		this.images = images;
	}

	@Override
	protected String createList(final String[] emailAddresses) {
		final StringBuilder list = new StringBuilder();
		if (emailAddresses != null) {
			for (int index = 0; index < emailAddresses.length; index++) {
				list.append(emailAddresses[index].trim());
				if (index != emailAddresses.length - 1) {
					list.append(SEPARATOR);
				}
			}
		}
		return list.toString();
	}

	@Override
	protected String getNewInputObject() {
		final EmailAddressDialog emailAddressDialog = new EmailAddressDialog(getShell());
		emailAddressDialog.create();
		if (emailAddressDialog.open() == Window.OK) {
			return emailAddressDialog.getEmailAddress();
		}
		return null;
	}

	@Override
	protected String[] parseString(final String stringList) {
		if (stringList != null && !stringList.isEmpty()) {
			return stringList.trim().split(EMAIL_ADDRESSES_SPLIT_REGEX);
		}
		else {
			return new String[] {};
		}
	}

	protected class EmailAddressDialog extends TitleAreaDialog {

		private static final String REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		private static final int MAX_LENGTH = 254;

		private Text textEmailAddress;
		private Button okButton;
		private Button cancelButton;
		private String emailAddress;

		public EmailAddressDialog(final Shell parentShell) {
			super(parentShell);
		}

		@Override
		protected void configureShell(final Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(JFaceMessages.get("lbl.preferences.email.add.title"));
			if (images != null) {
				newShell.setImages(images);
			}
		}

		@Override
		public void create() {
			super.create();
			setTitle(JFaceMessages.get("lbl.preferences.email.add.title"));
			setMessage(JFaceMessages.get("lbl.preferences.email.add.message"), IMessageProvider.INFORMATION);
		}

		@Override
		protected Composite createDialogArea(final Composite parent) {
			final Composite area = (Composite) super.createDialogArea(parent);
			final Composite container = new Composite(area, SWT.NONE);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
			GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).applyTo(container);

			final Label labelName = new Label(container, SWT.NONE);
			labelName.setText(JFaceMessages.get("lbl.preferences.email.add.address"));
			GridDataFactory.swtDefaults().applyTo(labelName);

			textEmailAddress = new Text(container, SWT.BORDER);
			GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(textEmailAddress);
			textEmailAddress.setTextLimit(MAX_LENGTH);
			textEmailAddress.addKeyListener(new TextKeyListener());

			return area;
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			super.createButtonsForButtonBar(parent);

			okButton = getButton(IDialogConstants.OK_ID);
			okButton.setText(JFaceMessages.get("lbl.button.ok"));
			okButton.setEnabled(false);

			cancelButton = getButton(IDialogConstants.CANCEL_ID);
			cancelButton.setText(JFaceMessages.get("lbl.button.cancel"));
		}

		@Override
		protected boolean isResizable() {
			return true;
		}

		@Override
		protected void okPressed() {
			emailAddress = textEmailAddress.getText().trim();
			super.okPressed();
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		private class TextKeyListener extends KeyAdapter {
			@Override
			public void keyReleased(final KeyEvent ke) {
				if (textEmailAddress.getText().trim().isEmpty() || !textEmailAddress.getText().trim().toLowerCase().matches(REGEX)) {
					if (okButton.isEnabled()) {
						okButton.setEnabled(false);
					}
				}
				else {
					if (!okButton.isEnabled()) {
						okButton.setEnabled(true);
					}
				}
			}
		}
	}

}
