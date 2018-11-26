package it.albertus.jface.maps;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.SwtUtils;
import it.albertus.util.logging.LoggerFactory;

public class MapBoundsDialogCommon {

	private static final Logger logger = LoggerFactory.getLogger(MapBoundsDialogCommon.class);

	public static final byte BORDER_THICKNESS_DLUS = 2;

	private MapBoundsDialogCommon() {
		throw new IllegalAccessError();
	}

	public static Browser createBrowser(final Composite parent, final URL url) {
		final Composite borderComposite = new Composite(parent, SWT.NONE);
		borderComposite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(borderComposite);
		final int pixels = SwtUtils.convertHorizontalDLUsToPixels(borderComposite, BORDER_THICKNESS_DLUS);
		GridLayoutFactory.swtDefaults().margins(pixels, pixels).applyTo(borderComposite);
		final Browser browser = new Browser(borderComposite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);
		browser.setUrl(url != null ? url.toString() : "");
		return browser;
	}

	public static Composite createButtonBox(final Shell shell, final Browser browser, final MapBoundsDialog dialog) {
		final Composite buttonComposite = new Composite(shell, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(buttonComposite);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(buttonComposite);

		final Button confirmButton = new Button(buttonComposite, SWT.PUSH);
		confirmButton.setText(JFaceMessages.get("lbl.button.confirm"));
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.FILL).grab(true, false).minSize(SwtUtils.convertHorizontalDLUsToPixels(confirmButton, IDialogConstants.BUTTON_WIDTH), SWT.DEFAULT).applyTo(confirmButton);
		confirmButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				try {
					dialog.setOptionValues(browser);
					dialog.setBoundValues(browser);
					dialog.setReturnCode(SWT.OK);
				}
				catch (final SWTException se) {
					logger.log(Level.FINE, se.toString(), se);
				}
				catch (final Exception e) {
					logger.log(Level.SEVERE, JFaceMessages.get("err.map.retrieve"), e);
				}
				finally {
					shell.close();
				}
			}
		});

		final Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText(JFaceMessages.get("lbl.button.cancel"));
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.FILL).grab(true, false).minSize(SwtUtils.convertHorizontalDLUsToPixels(cancelButton, IDialogConstants.BUTTON_WIDTH), SWT.DEFAULT).applyTo(cancelButton);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				shell.close();
			}
		});

		shell.setDefaultButton(confirmButton);
		return buttonComposite;
	}

}
