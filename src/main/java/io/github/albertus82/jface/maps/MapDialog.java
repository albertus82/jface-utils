package io.github.albertus82.jface.maps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import io.github.albertus82.jface.JFaceMessages;
import io.github.albertus82.jface.SwtUtils;
import io.github.albertus82.util.IOUtils;
import io.github.albertus82.util.logging.LoggerFactory;

public abstract class MapDialog extends Dialog implements LineParser {

	private static final Logger log = LoggerFactory.getLogger(MapDialog.class);

	public static final String OPTIONS_PLACEHOLDER = "/* {{options}} */";
	public static final String MARKERS_PLACEHOLDER = "/* {{markers}} */";
	public static final String OTHER_PLACEHOLDER = "/* {{other}} */";

	protected static final String HTML_FILE_NAME = "map.html";

	private final Set<MapMarker> markers = new HashSet<>();

	private volatile int returnCode = Window.CANCEL;

	private Image[] images;

	protected MapDialog(final Shell parent) {
		this(parent, SWT.SHEET | SWT.RESIZE | SWT.MAX);
	}

	protected MapDialog(final Shell parent, final int style) {
		super(parent, style);
	}

	public Composite createButtonBox(final Browser browser) {
		final Shell shell = browser.getShell();
		final Composite buttonComposite = new Composite(shell, SWT.NONE);
		GridLayoutFactory.swtDefaults().applyTo(buttonComposite);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).applyTo(buttonComposite);

		final Button closeButton = new Button(buttonComposite, SWT.PUSH);
		closeButton.setText(JFaceMessages.get("lbl.button.close"));
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.FILL).grab(true, false).minSize(SwtUtils.convertHorizontalDLUsToPixels(closeButton, IDialogConstants.BUTTON_WIDTH), SWT.DEFAULT).applyTo(closeButton);
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				setReturnCode(Window.CANCEL);
				shell.close();
			}
		});

		shell.setDefaultButton(closeButton);
		return buttonComposite;
	}

	public int open() {
		setReturnCode(Window.CANCEL);
		final Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		final Image[] icons = getImages();
		if (icons != null && icons.length > 0) {
			shell.setImages(icons);
		}
		createContents(shell);
		final Point minimumSize = getMinimumSize(shell);
		shell.setSize(getSize(shell));
		shell.setMinimumSize(minimumSize);
		shell.open();
		final Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return getReturnCode();
	}

	protected Point getSize(final Shell shell) {
		final Point normalShellSize = shell.getSize();
		final int size = Math.min(normalShellSize.x, normalShellSize.y);
		return new Point(size, size);
	}

	protected Point getMinimumSize(final Shell shell) {
		return shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
	}

	protected Layout getLayout() {
		return GridLayoutFactory.swtDefaults().create();
	}

	protected void createContents(final Shell shell) {
		shell.setLayout(getLayout());
		final Browser browser = createBrowser(shell);
		createButtonBox(browser);
	}

	protected Browser createBrowser(final Composite parent) {
		final Browser browser = new Browser(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);
		final URI pageUri = getMapPage(parent);
		browser.setUrl(pageUri != null ? pageUri.toString() : "");
		return browser;
	}

	public static URI getMapPage(final Control control, final InputStream is, final LineParser lineParser) {
		URI pageUrl = null;
		File tempFile = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			br = new BufferedReader(isr);
			tempFile = File.createTempFile(UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ROOT), ".html");
			fw = new FileWriter(tempFile);
			bw = new BufferedWriter(fw);
			String line;
			while ((line = br.readLine()) != null) {
				line = lineParser.parseLine(line);
				if (line != null) {
					line = line.trim();
					log.fine(line);
					bw.write(line);
					bw.newLine();
				}
			}
			pageUrl = tempFile.toURI();
		}
		catch (final Exception e) {
			log.log(Level.SEVERE, JFaceMessages.get("err.map.open"), e);
		}
		finally {
			IOUtils.closeQuietly(bw, fw, br, isr);
		}

		if (tempFile != null) {
			final File fileToDelete = tempFile;
			control.addListener(SWT.Close, new Listener() {
				@Override
				public void handleEvent(final Event event) {
					try {
						if (!fileToDelete.delete()) {
							fileToDelete.deleteOnExit();
						}
					}
					catch (final RuntimeException re) {
						log.log(Level.WARNING, JFaceMessages.get("err.delete.temp", fileToDelete), re);
					}
				}
			});
		}
		return pageUrl;
	}

	protected URI getMapPage(final Control control) {
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream(HTML_FILE_NAME);
			return getMapPage(control, is, this);
		}
		finally {
			IOUtils.closeQuietly(is);
		}
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(final int returnCode) {
		this.returnCode = returnCode;
	}

	public Image[] getImages() {
		return images;
	}

	public void setImages(final Image[] images) {
		this.images = images;
	}

	public Set<MapMarker> getMarkers() {
		return markers;
	}

	public abstract MapOptions getOptions();

}
