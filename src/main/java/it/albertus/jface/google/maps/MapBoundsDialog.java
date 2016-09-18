package it.albertus.jface.google.maps;

import it.albertus.jface.JFaceMessages;

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

public class MapBoundsDialog extends MapDialog {

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
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.FILL).grab(true, false).minSize(BUTTON_WIDTH, SWT.DEFAULT).applyTo(confirmButton);
		confirmButton.setFocus();
		confirmButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				try {
					getOptions().setCenterLat(((Number) browser.evaluate("return map.getCenter().lat();")).floatValue());
					getOptions().setCenterLng(((Number) browser.evaluate("return map.getCenter().lng();")).floatValue());
					getOptions().setZoom(((Number) browser.evaluate("return map.getZoom();")).intValue());
					bounds.setNorthEastLat((Double) browser.evaluate("return map.getBounds().getNorthEast().lat();"));
					bounds.setSouthWestLat((Double) browser.evaluate("return map.getBounds().getSouthWest().lat();"));
					bounds.setNorthEastLng((Double) browser.evaluate("return map.getBounds().getNorthEast().lng();"));
					bounds.setSouthWestLng((Double) browser.evaluate("return map.getBounds().getSouthWest().lng();"));
					setReturnCode(SWT.OK);
				}
				catch (final SWTException swte) {/* Ignore */}
				catch (final Exception e) {
					e.printStackTrace();
				}
				finally {
					shell.close();
				}
			}
		});

		final Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText(JFaceMessages.get("lbl.button.cancel"));
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.FILL).grab(true, false).minSize(BUTTON_WIDTH, SWT.DEFAULT).applyTo(cancelButton);
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
		return new Point(Math.min(packedShellSize.x * 3, normalShellSize.x), Math.min(packedShellSize.y * 3, normalShellSize.y));
	}

	@Override
	protected Layout getLayout() {
		return GridLayoutFactory.swtDefaults().create();
	}

	public MapBounds getBounds() {
		return bounds;
	}

}
