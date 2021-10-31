package io.github.albertus82.jface;

import static io.github.albertus82.jface.DisplayThreadExecutor.Mode.ASYNC;
import static io.github.albertus82.jface.DisplayThreadExecutor.Mode.SYNC;

import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

import io.github.albertus82.util.logging.LoggerFactory;

/** Executes code in the user-interface thread. */
public class DisplayThreadExecutor implements Executor {

	public enum Mode {
		SYNC,
		ASYNC
	}

	private static final Logger log = LoggerFactory.getLogger(DisplayThreadExecutor.class);

	private final Widget widget;
	private final Mode mode;

	public DisplayThreadExecutor(final Widget widget, final Mode mode) {
		if (widget == null) {
			throw new NullPointerException("widget cannot be null");
		}
		if (mode == null) {
			throw new NullPointerException("mode cannot be null");
		}
		this.widget = widget;
		this.mode = mode;
	}

	/**
	 * @param widget the widget whose display is used for the execution
	 * 
	 * @deprecated Use {@link #DisplayThreadExecutor(Widget, Mode)}.
	 */
	@Deprecated
	public DisplayThreadExecutor(final Widget widget) {
		this(widget, false);
	}

	/**
	 * @param widget the widget whose display is used for the execution
	 * @param async the executor mode: true for async, false for sync
	 * 
	 * @deprecated Use {@link #DisplayThreadExecutor(Widget, Mode)}.
	 */
	@Deprecated
	public DisplayThreadExecutor(final Widget widget, final boolean async) {
		this.widget = widget;
		this.mode = async ? ASYNC : SYNC;
	}

	public Widget getWidget() {
		return widget;
	}

	public Mode getMode() {
		return mode;
	}

	/**
	 * @return true if the mode is asynchronous, otherwise false.
	 * 
	 * @deprecated Use {@link #getMode()}.
	 */
	@Deprecated
	public boolean isAsync() {
		return ASYNC.equals(mode);
	}

	protected void onError(final Exception e) {
		if (e != null && !(e instanceof SWTException)) {
			log.log(Level.SEVERE, "Error in SWT thread:", e);
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
					switch (mode) {
					case ASYNC:
						widget.getDisplay().asyncExec(runnable);
						break;
					case SYNC:
						widget.getDisplay().syncExec(runnable);
						break;
					default:
						throw new UnsupportedOperationException("Invalid mode: " + mode);
					}
				}
			}
			catch (final RuntimeException e) {
				onError(e);
			}
		}

	}

}
