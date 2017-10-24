package it.albertus.jface.preference;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import it.albertus.jface.EnhancedErrorDialog;
import it.albertus.jface.JFaceMessages;
import it.albertus.jface.preference.page.BasePreferencePage;
import it.albertus.util.logging.LoggerFactory;

public class EnhancedPreferenceDialog extends PreferenceDialog {

	private static final Logger logger = LoggerFactory.getLogger(EnhancedPreferenceDialog.class);

	private final String title;
	private final Image[] images;

	public EnhancedPreferenceDialog(final Shell parentShell, final PreferenceManager manager, final String title, final Image... images) {
		super(parentShell, manager);
		this.title = title;
		this.images = images;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		if (title != null) {
			newShell.setText(title);
		}
		if (images != null && images.length > 0) {
			newShell.setImages(images);
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		final Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setText(JFaceMessages.get("lbl.button.ok"));

		final Button cancelButton = getButton(IDialogConstants.CANCEL_ID);
		cancelButton.setText(JFaceMessages.get("lbl.button.cancel"));
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

	@Override
	protected void constrainShellSize() {
		super.constrainShellSize();
		if (Util.isCocoa()) {
			final Tree tree = getTreeViewer().getTree();
			final FontDescriptor treeFontDescriptor = FontDescriptor.createFrom(JFaceResources.getBannerFont()).setStyle(SWT.NORMAL);
			final Font treeFont = treeFontDescriptor.createFont(tree.getDisplay());
			updateTreeFont(treeFont);
			tree.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					treeFontDescriptor.destroyFont(treeFont);
				}
			});
		}
	}

	@Override
	protected void handleSave() {
		final Iterator<IPreferenceNode> nodes = getPreferenceManager().getElements(PreferenceManager.PRE_ORDER).iterator();
		while (nodes.hasNext()) {
			final IPreferenceNode node = nodes.next();
			final IPreferencePage page = node.getPage();
			if (page instanceof PreferencePage) {
				final IPreferenceStore store = ((PreferencePage) page).getPreferenceStore();
				if (store != null && store.needsSaving() && store instanceof IPersistentPreferenceStore) {
					try {
						((IPersistentPreferenceStore) store).save();
					}
					catch (final IOException ioe) {
						final String message = JFaceMessages.get("err.preferences.save");
						logger.log(Level.SEVERE, message, ioe);
						EnhancedErrorDialog.openError(getShell(), title, message, IStatus.ERROR, ioe, new Image[] { Display.getCurrent().getSystemImage(SWT.ICON_ERROR) });
					}
				}
			}
		}
	}

}
