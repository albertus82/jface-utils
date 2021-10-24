package io.github.albertus82.jface.listener;

import javax.annotation.Nullable;

import org.eclipse.swt.events.VerifyEvent;

import io.github.albertus82.util.ISupplier;
import io.github.albertus82.util.Supplier;

/** Accepts only numeric inputs and trims automatically. */
abstract class AbstractNumberVerifyListener<T extends Number> extends TrimVerifyListener {

	protected final ISupplier<Boolean> allowNegatives;

	protected AbstractNumberVerifyListener(final ISupplier<Boolean> allowNegatives) {
		this.allowNegatives = allowNegatives;
	}

	protected AbstractNumberVerifyListener(final boolean allowNegatives) {
		this(new Supplier<Boolean>() {
			@Override
			public Boolean get() {
				return allowNegatives;
			}
		});
	}

	@Override
	public void verifyText(final VerifyEvent ve) {
		super.verifyText(ve); // Trim
		if (ve.text.length() > 0 && !isNumeric(ve.text)) {
			ve.doit = false;
		}
	}

	protected abstract boolean isNumeric(@Nullable String string);

	protected abstract T parseNumber(String string);

}
