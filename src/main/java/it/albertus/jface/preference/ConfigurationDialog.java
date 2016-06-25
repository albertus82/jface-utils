package it.albertus.jface.preference;

import it.albertus.jface.JFaceResources;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ConfigurationDialog extends PreferenceDialog {

	private final Image[] images;

	public ConfigurationDialog(final Shell parentShell, final PreferenceManager manager, final Image[] images) {
		super(parentShell, manager);
		this.images = images;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(JFaceResources.get("lbl.preferences.title"));
		if (images != null) {
			newShell.setImages(images);
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		final Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setText(JFaceResources.get("lbl.preferences.button.ok"));

		final Button cancelButton = getButton(IDialogConstants.CANCEL_ID);
		cancelButton.setText(JFaceResources.get("lbl.preferences.button.cancel"));
	}

}
