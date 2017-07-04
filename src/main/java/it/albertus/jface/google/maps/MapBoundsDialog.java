package it.albertus.jface.google.maps;

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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.SwtUtils;
import it.albertus.util.logging.LoggerFactory;

public class MapBoundsDialog extends MapDialog {

	private static final Logger logger = LoggerFactory.getLogger(MapBoundsDialog.class);

	private static final int SHELL_SIZE_FACTOR = 3;

	private final MapBounds bounds = new MapBounds();

	public MapBoundsDialog(final Shell parent) {
		super(parent);
	}

	public MapBoundsDialog(final Shell parent, final int style) {
		super(parent, style);
	}

	@Override
	public Composite createButtonBox(final Shell shell, final Browser browser) {
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
					getOptions().setZoom(((Number) browser.evaluate("return map.getZoom();")).intValue());
					getOptions().setCenterLat((Double) browser.evaluate("return map.getCenter().lat();"));
					getOptions().setCenterLng((Double) browser.evaluate("return map.getCenter().lng();"));
					bounds.setNorthEastLat((Double) browser.evaluate("return map.getBounds().getNorthEast().lat();"));
					bounds.setSouthWestLat((Double) browser.evaluate("return map.getBounds().getSouthWest().lat();"));
					bounds.setNorthEastLng((Double) browser.evaluate("return map.getBounds().getNorthEast().lng();"));
					bounds.setSouthWestLng((Double) browser.evaluate("return map.getBounds().getSouthWest().lng();"));
					setReturnCode(SWT.OK);
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

	@Override
	protected Point getSize(final Shell shell) {
		final Point normalShellSize = shell.getSize();
		final Point packedShellSize = getMinimumSize(shell);
		return new Point(Math.min(packedShellSize.x * SHELL_SIZE_FACTOR, normalShellSize.x), Math.min(packedShellSize.y * SHELL_SIZE_FACTOR, normalShellSize.y));
	}

	@Override
	protected Layout getLayout() {
		return GridLayoutFactory.swtDefaults().create();
	}

	public MapBounds getBounds() {
		return bounds;
	}

}
