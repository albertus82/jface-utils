package it.albertus.jface;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

import it.albertus.util.logging.LoggerFactory;

/** Executes code in the user-interface thread. */
public class DisplayThreadExecutor implements Executor {

	private static final Logger logger = LoggerFactory.getLogger(DisplayThreadExecutor.class);

	private final Widget widget;
	private final boolean async;

	public DisplayThreadExecutor(final Widget widget) {
		this(widget, false);
	}

	public DisplayThreadExecutor(final Widget widget, final boolean async) {
		this.widget = widget;
		this.async = async;
	}

	public Widget getWidget() {
		return widget;
	}

	public boolean isAsync() {
		return async;
	}

	protected void onError(final Exception e) {
		if (e != null && !(e instanceof SWTException)) {
			logger.log(Level.SEVERE, "Error in SWT thread", e);
		}
	}

	@Override
	public void execute(final Runnable command) {
		if (widget != null) {
			try {
				if (widget.getDisplay().equals(Display.getCurrent())) {
					command.run();
				}
				else {
					final Runnable runnable = new Runnable() {
						@Override
						public void run() {
							try {
								command.run();
							}
							catch (final RuntimeException e) {
								onError(e);
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
			catch (final RuntimeException e) {
				onError(e);
			}
		}

	}

}
