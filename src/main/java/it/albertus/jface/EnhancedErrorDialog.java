package it.albertus.jface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import it.albertus.util.ExceptionUtils;
import it.albertus.util.IOUtils;
import it.albertus.util.logging.LoggerFactory;

public class EnhancedErrorDialog extends ErrorDialog {

	private static final Logger logger = LoggerFactory.getLogger(EnhancedErrorDialog.class);

	private final Image[] images;

	public EnhancedErrorDialog(final Shell parent, final String title, final String message, final IStatus status, final Image[] images, final int displayMask) {
		super(parent, title, message, status, displayMask);
		this.message = message == null ? status.getMessage() : JFaceMessages.get("lbl.error.dialog.reason", message, status.getMessage());
		this.images = images;
	}

	public static int openError(final Shell parent, final String title, final String message, final IStatus status, final Image[] images) {
		return openError(parent, title, message, status, images, IStatus.OK | IStatus.INFO | IStatus.WARNING | IStatus.ERROR);
	}

	public static int openError(final Shell parent, final String title, final String message, final IStatus status, final Image[] images, final int displayMask) {
		final EnhancedErrorDialog dialog = new EnhancedErrorDialog(parent, title, message, status, images, displayMask);
		return dialog.open();
	}

	public static int openError(final Shell parent, final String title, final String message, final int severity, final Throwable e, final Image[] images) {
		final MultiStatus status = EnhancedErrorDialog.createMultiStatus(severity, e);
		return openError(parent, title, message, status, images);
	}

	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		if (images != null && images.length > 0) {
			shell.setImages(images);
		}
	}

	public static MultiStatus createMultiStatus(final int severity, final Throwable throwable) {
		final Collection<IStatus> childStatuses = new ArrayList<IStatus>();

		StringReader sr = null;
		BufferedReader br = null;
		try {
			sr = new StringReader(ExceptionUtils.getStackTrace(throwable));
			br = new BufferedReader(sr);
			String line;
			while ((line = br.readLine()) != null) {
				final IStatus status = new Status(severity, throwable.getClass().getName(), line);
				childStatuses.add(status);
			}
		}
		catch (final IOException ioe) {
			logger.log(Level.WARNING, ioe.getLocalizedMessage() != null ? ioe.getLocalizedMessage() : ioe.getMessage(), ioe);
		}
		finally {
			IOUtils.closeQuietly(br, sr);
		}

		return new MultiStatus(EnhancedErrorDialog.class.getPackage().getName(), severity, childStatuses.toArray(new IStatus[0]), throwable.toString(), throwable);
	}

}
