package it.albertus.jface.decoration;

import it.albertus.jface.validation.TextValidator;
import it.albertus.util.Localized;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

public class TextDecoration {

	public static final int DEFAULT_STYLE = SWT.TOP | SWT.LEFT;
	public static final String DEFAULT_TYPE = FieldDecorationRegistry.DEC_ERROR;

	private final TextValidator validator;
	private final Localized message;
	private final int style;
	private final String type;

	public TextDecoration(final TextValidator validator, final Localized message, final int style, final String type) {
		this.validator = validator;
		this.message = message;
		this.style = style;
		this.type = type;
		applyTo(validator.getText());
	}

	public TextDecoration(final TextValidator validator, final Localized message) {
		this.validator = validator;
		this.message = message;
		this.style = DEFAULT_STYLE;
		this.type = DEFAULT_TYPE;
		applyTo(validator.getText());
	}

	public TextDecoration(final TextValidator validator, final String message) {
		this(validator, new Localized() {
			@Override
			public String getString() {
				return message;
			}
		});
	}

	private void applyTo(final Text text) {
		final ControlDecoration controlDecorator = new ControlDecoration(text, style);
		controlDecorator.hide();
		final Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(type).getImage();
		controlDecorator.setImage(image);
		text.addKeyListener(new TextDecorationKeyListener(controlDecorator, validator, message));
		adjustLayoutData(text, image);
	}

	protected void adjustLayoutData(final Text text, final Image image) {
		if (text.getLayoutData() instanceof GridData) {
			((GridData) text.getLayoutData()).horizontalIndent = image.getBounds().width;
		}
	}

	public int getStyle() {
		return style;
	}

	public String getType() {
		return type;
	}

	public TextValidator getValidator() {
		return validator;
	}

	public Localized getMessage() {
		return message;
	}

}
