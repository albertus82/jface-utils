package it.albertus.jface.sysinfo;

import javax.annotation.Nullable;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.SwtUtils;

public class PropertyDetailDialog extends Dialog {

	private static final String LBL_SYSTEM_INFO_TABLE_VALUE = "lbl.system.info.table.value";
	private static final String LBL_SYSTEM_INFO_TABLE_KEY = "lbl.system.info.table.key";
	private static final String LBL_BUTTON_CLOSE = "lbl.button.close";

	private static final int MONITOR_SIZE_DIVISOR = 3;
	private static final int MAX_TEXT_HEIGHT = 5;
	private static final int WRAP_LENGTH = 80;

	protected static class TextKeyListener extends KeyAdapter {
		private final Text text;

		public TextKeyListener(final Text text) {
			this.text = text;
		}

		@Override
		public void keyPressed(@Nullable final KeyEvent e) {
			if (e != null && SWT.MOD1 == e.stateMask) {
				if (SwtUtils.KEY_COPY == e.keyCode) {
					text.copy();
				}
				else if (SwtUtils.KEY_SELECT_ALL == e.keyCode) {
					text.selectAll();
				}
			}
		}
	}

	@Nullable
	protected final String key;

	protected final String value;

	public PropertyDetailDialog(final Shell shell, @Nullable final String key, final String value) {
		super(shell, SWT.SHEET);
		this.key = key;
		this.value = value;
		setText(JFaceMessages.get("lbl.system.info.detail.dialog.title"));
	}

	public PropertyDetailDialog(final Shell shell, final String item) {
		this(shell, null, item);
	}

	public void open() {
		final Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		shell.setImage(shell.getDisplay().getSystemImage(SWT.ICON_INFORMATION));
		createContents(shell);
		constrainShellSize(shell);
		shell.open();
	}

	protected void adjustTextHeight(final Text text, final int height) {
		if (text.getLayoutData() instanceof GridData) {
			final GridData gd = (GridData) text.getLayoutData();
			gd.heightHint = text.getLineHeight() * height;
			gd.widthHint = 0;
		}
	}

	protected void createContents(final Shell shell) {
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(shell);

		if (key != null) {
			final Label labelKey = new Label(shell, SWT.NONE);
			labelKey.setText(JFaceMessages.get(LBL_SYSTEM_INFO_TABLE_KEY));
			GridDataFactory.swtDefaults().applyTo(labelKey);

			final Text textKey = new Text(shell, key.length() <= WRAP_LENGTH ? SWT.BORDER | SWT.READ_ONLY : SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
			textKey.setText(key);
			textKey.setEditable(false);
			textKey.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			textKey.addKeyListener(new TextKeyListener(textKey));
			if (key.length() <= WRAP_LENGTH) {
				GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(textKey);
			}
			else {
				GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(textKey);
				adjustTextHeight(textKey, Math.min(MAX_TEXT_HEIGHT, 2 + key.length() / WRAP_LENGTH));
			}
		}

		final Label labelValue = new Label(shell, SWT.NONE);
		labelValue.setText(JFaceMessages.get(LBL_SYSTEM_INFO_TABLE_VALUE));
		GridDataFactory.swtDefaults().applyTo(labelValue);

		final Text textValue = new Text(shell, value.length() <= WRAP_LENGTH ? SWT.BORDER | SWT.READ_ONLY : SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		textValue.setText(value);
		textValue.setEditable(false);
		textValue.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		textValue.addKeyListener(new TextKeyListener(textValue));

		if (value.length() <= WRAP_LENGTH) {
			GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(textValue);
		}
		else {
			GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(textValue);
			adjustTextHeight(textValue, Math.min(MAX_TEXT_HEIGHT, 2 + value.length() / WRAP_LENGTH));
		}

		final Button closeButton = new Button(shell, SWT.PUSH);
		closeButton.setText(JFaceMessages.get(LBL_BUTTON_CLOSE));
		final int buttonWidth = SwtUtils.convertHorizontalDLUsToPixels(closeButton, IDialogConstants.BUTTON_WIDTH);
		GridDataFactory.swtDefaults().span(2, 1).align(SWT.CENTER, SWT.CENTER).grab(true, false).minSize(buttonWidth, SWT.DEFAULT).applyTo(closeButton);
		closeButton.setFocus();
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent se) {
				shell.close();
			}
		});
		shell.setDefaultButton(closeButton);
	}

	protected void constrainShellSize(final Shell shell) {
		final Point packedSize = getMinimumSize(shell);
		final Rectangle screen = shell.getMonitor().getClientArea(); // available area
		shell.setSize(Math.max(packedSize.x, screen.width / MONITOR_SIZE_DIVISOR), packedSize.y);
		shell.setMinimumSize(shell.getMinimumSize().x, packedSize.y);
	}

	protected Point getMinimumSize(final Shell shell) {
		return shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
	}

}
