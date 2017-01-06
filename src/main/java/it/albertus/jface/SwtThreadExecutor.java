package it.albertus.jface;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

import it.albertus.util.logging.LoggerFactory;

/** Executes code in the user-interface thread. */
public abstract class SwtThreadExecutor {
	
	private static final Logger logger = LoggerFactory.getLogger(SwtThreadExecutor.class);

	private final Widget widget;
	private final boolean async;

	public SwtThreadExecutor(final Widget widget) {
		this(widget, false);
	}

	public SwtThreadExecutor(final Widget widget, final boolean async) {
		this.widget = widget;
		this.async = async;
	}

	public Widget getWidget() {
		return widget;
	}

	protected abstract void run();

	protected void onError(final Exception exception) {
		if (exception != null && !(exception instanceof SWTException)) {
			logger.log(Level.SEVERE, "Error in SWT thread", exception);
		}
	}

	public void start() {
		if (widget != null) {
			try {
				if (widget.getDisplay().equals(Display.getCurrent())) {
					run();
				}
				else {
					final Runnable runnable = new Runnable() {
						@Override
						public void run() {
							try {
								SwtThreadExecutor.this.run();
							}
							catch (final Exception exception) {
								onError(exception);
							}
						}
					};
					if (async) {
						widget.getDisplay().asyncExec(runnable);
					}
					else {
						widget.getDisplay().syncExec(runnable);
					}
				}
			}
			catch (final Exception exception) {
				onError(exception);
			}
		}
	}

}
