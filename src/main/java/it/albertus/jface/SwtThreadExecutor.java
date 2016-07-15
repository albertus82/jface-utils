package it.albertus.jface;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

/** Executes code in the user-interface thread. */
public abstract class SwtThreadExecutor {

	private final Widget widget;
	private final boolean async;

	public SwtThreadExecutor(final Widget widget) {
		this(widget, false);
	}

	public SwtThreadExecutor(final Widget widget, final boolean async) {
		this.widget = widget;
		this.async = async;
	}

	public final Widget getWidget() {
		return widget;
	}

	protected abstract void run();

	protected void onError(final Exception exception) {
		if (!(exception instanceof SWTException)) {
			if (exception != null) {
				exception.printStackTrace();
			}
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
