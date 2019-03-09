package it.albertus.jface.maps;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.SwtUtils;
import it.albertus.util.logging.LoggerFactory;

public class MapBoundsDialogCreationHelper {

	private static final Logger logger = LoggerFactory.getLogger(MapBoundsDialogCreationHelper.class);

	private final MapBoundsDialog dialog;

	public MapBoundsDialogCreationHelper(final MapBoundsDialog dialog) {
		this.dialog = dialog;
	}

	public Layout getLayout() {
		final int defaultMargin = new GridLayout().marginHeight;
		return GridLayoutFactory.swtDefaults().margins(0, 0).extendedMargins(0, 0, 0, defaultMargin).create();
	}

	public Browser createBrowser(final Composite parent, final URI uri) {
		final Composite borderComposite = new Composite(parent, SWT.NONE);
		borderComposite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(borderComposite);
		GridLayoutFactory.swtDefaults().applyTo(borderComposite);
		final Browser browser = new Browser(borderComposite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);
		browser.setUrl(uri != null ? uri.toString() : "");
		return browser;
	}

	public Composite createButtonBox(final Browser browser) {
		final Shell shell = browser.getShell();
		final Composite buttonComposite = new Composite(shell, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(buttonComposite);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(buttonComposite);

		final Button confirmButton = new Button(buttonComposite, SWT.PUSH);
		confirmButton.setText(JFaceMessages.get("lbl.button.confirm"));
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.FILL).grab(true, false).minSize(SwtUtils.convertHorizontalDLUsToPixels(confirmButton, IDialogConstants.BUTTON_WIDTH), SWT.DEFAULT).applyTo(confirmButton);
		confirmButton.addSelectionListener(new ButtonListener(Window.OK, browser));

		final Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText(JFaceMessages.get("lbl.button.cancel"));
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.FILL).grab(true, false).minSize(SwtUtils.convertHorizontalDLUsToPixels(cancelButton, IDialogConstants.BUTTON_WIDTH), SWT.DEFAULT).applyTo(cancelButton);
		cancelButton.addSelectionListener(new ButtonListener(Window.CANCEL, browser));

		shell.setDefaultButton(confirmButton);
		return buttonComposite;
	}

	private class ButtonListener extends SelectionAdapter {

		private final int buttonCode;
		private final Browser browser;

		private ButtonListener(final int buttonCode, final Browser browser) {
			this.buttonCode = buttonCode;
			this.browser = browser;
		}

		@Override
		public void widgetSelected(final SelectionEvent event) {
			try {
				if (buttonCode == Window.OK) {
					dialog.setOptionValues(browser);
					dialog.setBoundValues(browser);
				}
				dialog.setReturnCode(buttonCode);
			}
			catch (final SWTException e) {
				logger.log(Level.FINE, e.toString(), e);
			}
			catch (final Exception e) {
				logger.log(Level.SEVERE, JFaceMessages.get("err.map.retrieve"), e);
			}
			finally {
				final Shell shell = browser.getShell();
				if (shell != null && !shell.isDisposed()) {
					shell.close();
				}
			}
		}
	}

}
