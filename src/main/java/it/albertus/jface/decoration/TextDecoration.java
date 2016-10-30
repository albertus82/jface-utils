package it.albertus.jface.decoration;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.listener.DecorationKeyListener;
import it.albertus.jface.validation.ControlValidator;
import it.albertus.util.Localized;

public class TextDecoration {

	/** {@code SWT.TOP | SWT.LEFT} */
	public static final int DEFAULT_STYLE = SWT.TOP | SWT.LEFT;

	/** FieldDecorationRegistry.DEC_ERROR */
	public static final String DEFAULT_TYPE = FieldDecorationRegistry.DEC_ERROR;

	private final ControlValidator<Text> validator;
	private final Localized message;
	private final int style;
	private final String type;

	public TextDecoration(final ControlValidator<Text> validator, final Localized message, final int style, final String type) {
		this.validator = validator;
		this.message = message;
		this.style = style;
		this.type = type;
		applyTo(validator.getControl());
	}

	public TextDecoration(final ControlValidator<Text> validator, final Localized message) {
		this(validator, message, DEFAULT_STYLE, DEFAULT_TYPE);
		applyTo(validator.getControl());
	}

	public TextDecoration(final ControlValidator<Text> validator, final String message) {
		this(validator, new Localized() {
			@Override
			public String getString() {
				return message;
			}
		});
	}

	private void applyTo(final Control control) {
		final ControlDecoration controlDecorator = new ControlDecoration(control, style);
		controlDecorator.hide();
		final Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(type).getImage();
		controlDecorator.setImage(image);
		adjustLayoutData(control, image);
		control.addKeyListener(new DecorationKeyListener(controlDecorator, validator, message));
	}

	protected void adjustLayoutData(final Control control, final Image image) {}

	public int getStyle() {
		return style;
	}

	public String getType() {
		return type;
	}

	public ControlValidator<Text> getValidator() {
		return validator;
	}

	public Localized getMessage() {
		return message;
	}

}
