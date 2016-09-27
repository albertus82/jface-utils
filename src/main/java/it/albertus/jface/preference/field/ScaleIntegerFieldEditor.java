package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.TextFormatter;
import it.albertus.jface.listener.IntegerVerifyListener;
import it.albertus.util.Configured;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ScaleIntegerFieldEditor extends ScaleFieldEditor implements DefaultFieldEditor {

	private final Text text;

	private boolean defaultToolTip = true;
	private boolean boldCustomValues = true;

	public Text getTextControl() {
		return text;
	}

	public ScaleIntegerFieldEditor(final String name, final String labelText, final Composite parent, final int min, final int max, final int increment, final int pageIncrement) {
		super(name, labelText, parent, min, max, increment, pageIncrement);
		text = createTextControl(parent);
	}

	protected Text createTextControl(final Composite parent) {
		final Text text = new Text(parent, SWT.BORDER | SWT.TRAIL);
		final int widthHint = TextFormatter.getWidthHint(text, Integer.toString(getMaximum()).length(), SWT.BOLD);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).hint(widthHint, SWT.DEFAULT).applyTo(text);
		text.setTextLimit(Integer.toString(getMaximum()).length());
		text.addFocusListener(new TextFocusListener());
		text.addKeyListener(new TextKeyListener());
		text.addVerifyListener(new IntegerVerifyListener(new Configured<Boolean>() {
			@Override
			public Boolean getValue() {
				return false;
			}
		}));
		return text;
	}

	@Override
	protected void adjustForNumColumns(final int numColumns) {
		((GridData) scale.getLayoutData()).horizontalSpan = numColumns - (getNumberOfControls() - 1);
	}

	@Override
	public int getNumberOfControls() {
		return super.getNumberOfControls() + 1;
	}

	@Override
	protected void valueChanged() {
		super.valueChanged();
		updateText();
	}

	@Override
	protected void doLoad() {
		super.doLoad();
		setToolTipText();
		updateText();
	}

	protected String getDefaultValue() {
		final String defaultValue = getPreferenceStore().getDefaultString(getPreferenceName());
		try {
			Integer.parseInt(defaultValue);
			return defaultValue;
		}
		catch (final NumberFormatException nfe) {
			return "";
		}
	}

	@Override
	public void setEnabled(final boolean enabled, final Composite parent) {
		super.setEnabled(enabled, parent);
		this.scale.setEnabled(enabled);
		this.text.setEnabled(enabled);
	}

	protected void setToolTipText() {
		if (defaultToolTip) {
			final String defaultValue = getDefaultValue();
			if (text != null && !text.isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
				text.setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
			}
		}
	}

	protected void updateText() {
		if (scale != null && !scale.isDisposed()) {
			setText(scale.getSelection());
		}
	}

	protected void setText(final int value) {
		if (text != null && !text.isDisposed()) {
			text.setText(Integer.toString(value));
			TextFormatter.updateFontStyle(text, getPreferenceStore().getDefaultInt(getPreferenceName()));
		}
	}

	protected class TextKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(final KeyEvent ke) {
			if (boldCustomValues) {
				TextFormatter.updateFontStyle((Text) ke.widget, getPreferenceStore().getDefaultInt(getPreferenceName()));
			}
		}
	}

	protected class TextFocusListener extends FocusAdapter {
		@Override
		public void focusLost(final FocusEvent fe) {
			try {
				int textValue = Integer.parseInt(text.getText());
				if (textValue > getMaximum()) {
					textValue = getMaximum();
				}
				if (textValue < getMinimum()) {
					textValue = getMinimum();
				}
				setText(textValue);
				scale.setSelection(textValue);
			}
			catch (final RuntimeException re) {
				setText(scale.getSelection());
			}
		}
	}

	@Override
	public boolean isDefaultToolTip() {
		return defaultToolTip;
	}

	@Override
	public void setDefaultToolTip(final boolean defaultToolTip) {
		this.defaultToolTip = defaultToolTip;
	}

	@Override
	public boolean isBoldCustomValues() {
		return boldCustomValues;
	}

	@Override
	public void setBoldCustomValues(final boolean boldCustomValues) {
		this.boldCustomValues = boldCustomValues;
	}

}
