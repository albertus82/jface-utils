package it.albertus.jface.decoration;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

import it.albertus.jface.listener.DecorationModifyListener;
import it.albertus.jface.validation.ControlValidator;
import it.albertus.util.ISupplier;
import it.albertus.util.Localized;

public class ControlValidatorDecoration {

	/** {@code SWT.TOP | SWT.LEFT} */
	public static final int DEFAULT_STYLE = SWT.TOP | SWT.LEFT;

	/** FieldDecorationRegistry.DEC_ERROR */
	public static final String DEFAULT_TYPE = FieldDecorationRegistry.DEC_ERROR;

	protected final ControlValidator<?> validator;
	protected final ISupplier<String> message;
	protected final int style;
	protected final String type;

	public ControlValidatorDecoration(final ControlValidator<?> validator, final ISupplier<String> message, final int style, final String type) {
		this.validator = validator;
		this.message = message;
		this.style = style;
		this.type = type;

		final Control control = validator.getControl();
		final ControlDecoration controlDecorator = new ControlDecoration(control, style);
		controlDecorator.hide();
		final Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(type).getImage();
		controlDecorator.setImage(image);
		adjustLayoutData(control, image);
		control.addListener(SWT.Modify, new DecorationModifyListener(controlDecorator, validator, message));
	}

	public ControlValidatorDecoration(final ControlValidator<?> validator, final ISupplier<String> message) {
		this(validator, message, DEFAULT_STYLE, DEFAULT_TYPE);
	}

	public ControlValidatorDecoration(final ControlValidator<?> validator, final String message) {
		this(validator, new Localized() {
			@Override
			public String getString() {
				return message;
			}
		});
	}

	protected void adjustLayoutData(final Control control, final Image image) {
		// The default implementation does nothing. This method can be overridden.
	}

}
