package it.albertus.jface.preference;

import it.albertus.jface.JFaceResources;
import it.albertus.jface.preference.page.BasePreferencePage;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
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
		okButton.setText(JFaceResources.get("lbl.button.ok"));

		final Button cancelButton = getButton(IDialogConstants.CANCEL_ID);
		cancelButton.setText(JFaceResources.get("lbl.button.cancel"));
	}

	@Override
	protected boolean showPage(final IPreferenceNode node) {
		boolean success = super.showPage(node);
		final IPreferencePage currentPage = getCurrentPage();
		if (currentPage instanceof BasePreferencePage) {
			((BasePreferencePage) currentPage).updateCrossChildrenStatus();
		}
		return success;
	}

}
