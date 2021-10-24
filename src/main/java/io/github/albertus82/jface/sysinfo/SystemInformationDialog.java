package io.github.albertus82.jface.sysinfo;

import java.lang.management.ManagementPermission;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import io.github.albertus82.jface.EnhancedErrorDialog;
import io.github.albertus82.jface.JFaceMessages;
import io.github.albertus82.jface.SwtUtils;
import io.github.albertus82.util.MapUtils;
import io.github.albertus82.util.NewLine;
import io.github.albertus82.util.logging.LoggerFactory;

public class SystemInformationDialog extends Dialog {

	private static final String LBL_BUTTON_CLOSE = "lbl.button.close";
	private static final String LBL_MENU_ITEM_OPEN = "lbl.menu.item.open";
	private static final String LBL_MENU_ITEM_COPY = "lbl.menu.item.copy";
	private static final String LBL_MENU_ITEM_SELECT_ALL = "lbl.menu.item.select.all";
	private static final String LBL_SYSTEM_INFO_DIALOG_TITLE = "lbl.system.info.dialog.title";
	private static final String LBL_SYSTEM_INFO_NOT_AVAILABLE = "lbl.system.info.notAvailable";
	private static final String LBL_SYSTEM_INFO_TAB_ENV = "lbl.system.info.tab.env";
	private static final String LBL_SYSTEM_INFO_TAB_JVMARGS = "lbl.system.info.tab.jvmArgs";
	private static final String LBL_SYSTEM_INFO_TAB_PROPERTIES = "lbl.system.info.tab.properties";
	private static final String LBL_SYSTEM_INFO_TABLE_KEY = "lbl.system.info.table.key";
	private static final String LBL_SYSTEM_INFO_TABLE_VALUE = "lbl.system.info.table.value";
	private static final String LBL_SYSTEM_INFO_EXPORT_ENV = "lbl.system.info.export.env";
	private static final String LBL_SYSTEM_INFO_EXPORT_JVMARGS = "lbl.system.info.export.jvmArgs";
	private static final String LBL_SYSTEM_INFO_EXPORT_PROPERTIES = "lbl.system.info.export.properties";
	private static final String ERR_SYSTEM_INFO_EXPORT = "err.system.info.export";

	private static final float MONITOR_SIZE_DIVISOR = 1.67f;

	private static final String TABLE_ITEM_FONT_SYMBOLIC_NAME = SystemInformationDialog.class.getName();

	private static final Logger log = LoggerFactory.getLogger(SystemInformationDialog.class);

	/** The map contaning the system properties (can be null). */
	@Nullable
	protected final Map<String, String> properties;

	/** The map containing the environment variables (can be null). */
	@Nullable
	protected final Map<String, String> env;

	/** The collection containing the JVM arguments (can be null). */
	@Nullable
	protected final Iterable<String> jvmArgs;

	/**
	 * Determine if the dialog can be populated with informations. The presence of a
	 * {@link SecurityManager} could limit the informations that can be retrieved.
	 * 
	 * @return {@code true} if there are informations to show, otherwise
	 *         {@code false}.
	 */
	public static boolean isAvailable() {
		final SecurityManager sm = System.getSecurityManager();
		if (sm == null) {
			return true;
		}
		try {
			sm.checkPropertiesAccess(); // system properties
			return true;
		}
		catch (final SecurityException e) {
			log.log(Level.FINE, "The calling thread does not have permission to access the system properties:", e);
		}
		try {
			sm.checkPermission(new RuntimePermission("getenv.*")); // environment variables
			return true;
		}
		catch (final SecurityException e) {
			log.log(Level.FINE, "Access to the environment variables is not permitted based on the current security policy:", e);
		}
		try {
			sm.checkPermission(new ManagementPermission("monitor")); // jvm args
			return true;
		}
		catch (final SecurityException e) {
			log.log(Level.FINE, "Access to the JVM arguments is not permitted based on the current security policy:", e);
		}
		return false;
	}

	/**
	 * If informations are available, opens a <em>System Information</em> dialog,
	 * otherwise opens a info message box.
	 * 
	 * @param shell the parent shell
	 * 
	 * @see #isAvailable()
	 */
	public static void open(final Shell shell) {
		try {
			shell.setCursor(shell.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));
			final SystemInformationGatherer gatherer = new SystemInformationGatherer();
			ModalContext.run(gatherer, true, new NullProgressMonitor(), shell.getDisplay());
			if (gatherer.getProperties() != null || gatherer.getEnv() != null || gatherer.getJvmArgs() != null) {
				new SystemInformationDialog(shell, gatherer.getProperties(), gatherer.getEnv(), gatherer.getJvmArgs()).open();
			}
			else {
				final MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION);
				box.setText(JFaceMessages.get(LBL_SYSTEM_INFO_DIALOG_TITLE));
				box.setMessage(JFaceMessages.get(LBL_SYSTEM_INFO_NOT_AVAILABLE));
				box.open();
			}
		}
		catch (final Exception e) { // NOSONAR Either re-interrupt this method or rethrow the "InterruptedException" that can be caught here. "InterruptedException" should not be ignored (java:S2142)
			throw new IllegalStateException(e);
		}
		finally {
			shell.setCursor(null);
		}
	}

	/**
	 * Constructs a new instance of the <em>System Information</em> dialog with one
	 * or more collections of data.
	 * 
	 * @param parent the parent control
	 * @param properties a map contaning the system properties (can be null)
	 * @param env a map containing the environment variables (can be null)
	 * @param jvmArgs a collection containing the JVM arguments (can be null)
	 * 
	 * @see #SystemInformationDialog(Shell, int, Map, Map, Collection)
	 * @see org.eclipse.swt.widgets.Dialog#Dialog(Shell, int)
	 */
	protected SystemInformationDialog(final Shell parent, @Nullable final Map<String, String> properties, @Nullable final Map<String, String> env, @Nullable final Iterable<String> jvmArgs) {
		this(parent, SWT.SHEET | SWT.RESIZE | SWT.MAX, properties, env, jvmArgs);
	}

	/**
	 * Constructs a new instance of the <em>System Information</em> dialog with
	 * custom style and one or more collections of data.
	 *
	 * @param parent the parent control
	 * @param style the style of the dialog
	 * @param properties a map contaning the system properties (can be null)
	 * @param env a map containing the environment variables (can be null)
	 * @param jvmArgs a collection containing the JVM arguments (can be null)
	 * 
	 * @see org.eclipse.swt.widgets.Dialog#Dialog(Shell, int)
	 */
	protected SystemInformationDialog(final Shell parent, final int style, @Nullable final Map<String, String> properties, @Nullable final Map<String, String> env, @Nullable final Iterable<String> jvmArgs) {
		super(parent, style);
		this.properties = properties;
		this.env = env;
		this.jvmArgs = jvmArgs;
		setText(JFaceMessages.get(LBL_SYSTEM_INFO_DIALOG_TITLE));
	}

	/** Opens this <em>System Information</em> dialog. */
	protected void open() {
		final Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		shell.setImage(shell.getDisplay().getSystemImage(SWT.ICON_INFORMATION));
		createMenuBar(shell);
		createContents(shell);
		constrainShellSize(shell);
		shell.open();
	}

	protected void createMenuBar(final Shell shell) {
		final Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);

		final Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		MenuItem fileMenuHeader = new MenuItem(bar, SWT.CASCADE);
		fileMenuHeader.setText(JFaceMessages.get("lbl.menu.item.file"));
		fileMenuHeader.setMenu(fileMenu);

		final MenuItem exportMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		exportMenuItem.setText(JFaceMessages.get("lbl.menu.item.export") + SwtUtils.getMod1ShortcutLabel(SwtUtils.KEY_SAVE));
		exportMenuItem.setAccelerator(SWT.MOD1 | SwtUtils.KEY_SAVE);
		exportMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				export(shell);
			}
		});

		new MenuItem(fileMenu, SWT.SEPARATOR);

		final MenuItem closeMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		closeMenuItem.setText(JFaceMessages.get("lbl.menu.item.close"));
		closeMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				shell.close();
			}
		});
	}

	protected void constrainShellSize(final Shell shell) {
		final Rectangle screen = shell.getMonitor().getClientArea(); // available area
		shell.setSize((int) (screen.width / MONITOR_SIZE_DIVISOR), (int) (screen.height / MONITOR_SIZE_DIVISOR));
	}

	protected void createContents(final Shell shell) {
		GridLayoutFactory.swtDefaults().applyTo(shell);
		final TabFolder folder = new TabFolder(shell, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(folder);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(folder);

		if (properties != null) {
			final TabItem propertiesTab = new TabItem(folder, SWT.NONE);
			propertiesTab.setText(JFaceMessages.get(LBL_SYSTEM_INFO_TAB_PROPERTIES));
			final Table propertiesTable = createTable(folder, properties);
			propertiesTab.setControl(propertiesTable);
		}

		if (env != null) {
			final TabItem envTab = new TabItem(folder, SWT.NONE);
			envTab.setText(JFaceMessages.get(LBL_SYSTEM_INFO_TAB_ENV));
			final Table envTable = createTable(folder, env);
			envTab.setControl(envTable);
		}

		if (jvmArgs != null) {
			final TabItem jvmArgsTab = new TabItem(folder, SWT.NONE);
			jvmArgsTab.setText(JFaceMessages.get(LBL_SYSTEM_INFO_TAB_JVMARGS));
			final List jvmArgsList = createList(folder, jvmArgs);
			jvmArgsTab.setControl(jvmArgsList);
		}

		createButtons(shell);
	}

	protected Table createTable(final Composite parent, final Map<String, String> map) {
		final Table table = new Table(parent, SWT.FULL_SELECTION | SWT.MULTI);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);

		for (final String title : new String[] { JFaceMessages.get(LBL_SYSTEM_INFO_TABLE_KEY), JFaceMessages.get(LBL_SYSTEM_INFO_TABLE_VALUE) }) {
			final TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
		final FontRegistry fontRegistry = JFaceResources.getFontRegistry();
		for (final Entry<String, String> entry : map.entrySet()) {
			final TableItem item = new TableItem(table, SWT.NONE);
			if (!fontRegistry.hasValueFor(TABLE_ITEM_FONT_SYMBOLIC_NAME)) {
				fontRegistry.put(TABLE_ITEM_FONT_SYMBOLIC_NAME, item.getFont().getFontData());
			}
			item.setFont(0, fontRegistry.getBold(TABLE_ITEM_FONT_SYMBOLIC_NAME));
			item.setText(new String[] { entry.getKey(), entry.getValue() });
		}
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}
		table.pack();

		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(@Nullable final KeyEvent e) {
				if (e != null && SWT.MOD1 == e.stateMask) {
					if (SwtUtils.KEY_COPY == e.keyCode) {
						copy(table);
					}
					else if (SwtUtils.KEY_SELECT_ALL == e.keyCode) {
						table.selectAll();
					}
				}
			}
		});

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(@Nullable final SelectionEvent e) {
				if (table.getSelectionIndex() != -1) {
					new PropertyDetailDialog(table.getShell(), table.getItem(table.getSelectionIndex()).getText(0), table.getItem(table.getSelectionIndex()).getText(1)).open();
				}
			}
		});

		createTableContextMenu(table);

		return table;
	}

	protected Menu createTableContextMenu(final Table table) {
		final Menu contextMenu = new Menu(table);

		// Open...
		final MenuItem openMenuItem = new MenuItem(contextMenu, SWT.PUSH);
		openMenuItem.setText(JFaceMessages.get(LBL_MENU_ITEM_OPEN));
		openMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent e) {
				if (table.getSelectionIndex() != -1) {
					new PropertyDetailDialog(table.getShell(), table.getItem(table.getSelectionIndex()).getText(0), table.getItem(table.getSelectionIndex()).getText(1)).open();
				}
			}
		});
		contextMenu.setDefaultItem(openMenuItem);

		new MenuItem(contextMenu, SWT.SEPARATOR);

		// Copy...
		final MenuItem copyMenuItem = new MenuItem(contextMenu, SWT.PUSH);
		copyMenuItem.setText(JFaceMessages.get(LBL_MENU_ITEM_COPY) + SwtUtils.getMod1ShortcutLabel(SwtUtils.KEY_COPY));
		copyMenuItem.setAccelerator(SWT.MOD1 | SwtUtils.KEY_COPY); // dummy
		copyMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent e) {
				copy(table);
			}
		});

		new MenuItem(contextMenu, SWT.SEPARATOR);

		// Select all...
		final MenuItem selectAllMenuItem = new MenuItem(contextMenu, SWT.PUSH);
		selectAllMenuItem.setText(JFaceMessages.get(LBL_MENU_ITEM_SELECT_ALL) + SwtUtils.getMod1ShortcutLabel(SwtUtils.KEY_SELECT_ALL));
		selectAllMenuItem.setAccelerator(SWT.MOD1 | SwtUtils.KEY_SELECT_ALL); // dummy
		selectAllMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent e) {
				table.selectAll();
			}
		});

		table.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(@Nullable final MenuDetectEvent e) {
				copyMenuItem.setEnabled(canCopy(table));
				selectAllMenuItem.setEnabled(table.getItemCount() > 0);
				contextMenu.setVisible(true);
			}
		});

		return contextMenu;
	}

	protected List createList(final Composite parent, final Iterable<String> jvmArgs) {
		final List list = new List(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(list);

		for (final String arg : jvmArgs) {
			list.add(arg);
		}
		list.pack();

		list.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(@Nullable final TraverseEvent e) {
				if (e != null && SWT.TRAVERSE_RETURN == e.detail && list.getSelectionIndex() != -1) {
					new PropertyDetailDialog(list.getShell(), list.getItem(list.getSelectionIndex())).open();
				}
			}
		});

		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(@Nullable final KeyEvent e) {
				if (e != null && SWT.MOD1 == e.stateMask) {
					if (SwtUtils.KEY_COPY == e.keyCode) {
						copy(list);
					}
					else if (SwtUtils.KEY_SELECT_ALL == e.keyCode) {
						list.selectAll();
					}
				}
			}
		});

		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(@Nullable final SelectionEvent e) {
				if (list.getSelectionIndex() != -1) {
					new PropertyDetailDialog(list.getShell(), list.getItem(list.getSelectionIndex())).open();
				}
			}
		});

		createListContextMenu(list);

		return list;
	}

	protected Menu createListContextMenu(final List list) {
		final Menu contextMenu = new Menu(list);

		// Open...
		final MenuItem openMenuItem = new MenuItem(contextMenu, SWT.PUSH);
		openMenuItem.setText(JFaceMessages.get(LBL_MENU_ITEM_OPEN));
		openMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent e) {
				if (list.getSelectionIndex() != -1) {
					new PropertyDetailDialog(list.getShell(), list.getItem(list.getSelectionIndex())).open();
				}
			}
		});
		contextMenu.setDefaultItem(openMenuItem);

		new MenuItem(contextMenu, SWT.SEPARATOR);

		// Copy...
		final MenuItem copyMenuItem = new MenuItem(contextMenu, SWT.PUSH);
		copyMenuItem.setText(JFaceMessages.get(LBL_MENU_ITEM_COPY) + SwtUtils.getMod1ShortcutLabel(SwtUtils.KEY_COPY));
		copyMenuItem.setAccelerator(SWT.MOD1 | SwtUtils.KEY_COPY); // dummy
		copyMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent e) {
				copy(list);
			}
		});

		new MenuItem(contextMenu, SWT.SEPARATOR);

		// Select all...
		final MenuItem selectAllMenuItem = new MenuItem(contextMenu, SWT.PUSH);
		selectAllMenuItem.setText(JFaceMessages.get(LBL_MENU_ITEM_SELECT_ALL) + SwtUtils.getMod1ShortcutLabel(SwtUtils.KEY_SELECT_ALL));
		selectAllMenuItem.setAccelerator(SWT.MOD1 | SwtUtils.KEY_SELECT_ALL); // dummy
		selectAllMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent e) {
				list.selectAll();
			}
		});

		list.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(@Nullable final MenuDetectEvent e) {
				copyMenuItem.setEnabled(canCopy(list));
				selectAllMenuItem.setEnabled(list.getItemCount() > 0);
				contextMenu.setVisible(true);
			}
		});

		return contextMenu;
	}

	protected Button createButtons(final Shell shell) {
		final Button closeButton = new Button(shell, SWT.PUSH);
		closeButton.setText(JFaceMessages.get(LBL_BUTTON_CLOSE));
		final int buttonWidth = SwtUtils.convertHorizontalDLUsToPixels(closeButton, IDialogConstants.BUTTON_WIDTH);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).minSize(buttonWidth, SWT.DEFAULT).applyTo(closeButton);
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent e) {
				shell.close();
			}
		});
		return closeButton;
	}

	/**
	 * Copies the current selection to the clipboard.
	 * 
	 * @param table the data source
	 */
	protected void copy(final Table table) {
		if (canCopy(table)) {
			final StringBuilder data = new StringBuilder();

			for (int row = 0; row < table.getSelectionCount(); row++) {
				for (int col = 0; col < table.getColumnCount(); col++) {
					data.append(table.getSelection()[row].getText(col));
					if (col == 0) {
						data.append('=');
					}
					else if (row != table.getSelectionCount() - 1) {
						data.append(NewLine.SYSTEM_LINE_SEPARATOR);
					}
				}
			}

			Clipboard clipboard = null;
			try {
				clipboard = new Clipboard(table.getDisplay());
				clipboard.setContents(new String[] { data.toString() }, new TextTransfer[] { TextTransfer.getInstance() });
			}
			finally {
				if (clipboard != null) {
					clipboard.dispose();
				}
			}
		}
	}

	/**
	 * Copies the current selection to the clipboard.
	 * 
	 * @param list the data source
	 */
	protected void copy(final List list) {
		if (canCopy(list)) {
			final StringBuilder data = new StringBuilder();

			for (int row = 0; row < list.getSelectionCount(); row++) {
				data.append(list.getSelection()[row]);
				if (row != list.getSelectionCount() - 1) {
					data.append(NewLine.SYSTEM_LINE_SEPARATOR);
				}
			}

			Clipboard clipboard = null;
			try {
				clipboard = new Clipboard(list.getDisplay());
				clipboard.setContents(new String[] { data.toString() }, new TextTransfer[] { TextTransfer.getInstance() });
			}
			finally {
				if (clipboard != null) {
					clipboard.dispose();
				}
			}
		}
	}

	protected boolean canCopy(final Table table) {
		return !table.isDisposed() && table.getColumnCount() > 0 && table.getSelectionCount() > 0;
	}

	protected boolean canCopy(final List list) {
		return !list.isDisposed() && list.getSelectionCount() > 0;
	}

	/**
	 * Export system information to file.
	 * 
	 * @param shell the parent shell, needed to open the save file dialog
	 */
	protected void export(final Shell shell) {
		final String fileName = openSaveDialog(shell);
		if (fileName != null && !fileName.trim().isEmpty()) {
			try {
				SwtUtils.blockShell(shell);
				final Map<String, Map<String, String>> maps = MapUtils.newLinkedHashMapWithExpectedSize(2);
				maps.put(JFaceMessages.get(LBL_SYSTEM_INFO_EXPORT_PROPERTIES), properties);
				maps.put(JFaceMessages.get(LBL_SYSTEM_INFO_EXPORT_ENV), env);
				final SystemInformationExporter exporter = new SystemInformationExporter(fileName, maps, Collections.singletonMap(JFaceMessages.get(LBL_SYSTEM_INFO_EXPORT_JVMARGS), jvmArgs));
				ModalContext.run(exporter, true, new NullProgressMonitor(), shell.getDisplay());
			}
			catch (final InvocationTargetException e) {
				final String message = JFaceMessages.get(ERR_SYSTEM_INFO_EXPORT);
				log.log(Level.WARNING, message, e);
				SwtUtils.unblockShell(shell);
				EnhancedErrorDialog.openError(shell, JFaceMessages.get(LBL_SYSTEM_INFO_DIALOG_TITLE), message, IStatus.WARNING, e.getCause() != null ? e.getCause() : e, shell.getDisplay().getSystemImage(SWT.ICON_WARNING));
			}
			catch (final Exception e) { // NOSONAR Either re-interrupt this method or rethrow the "InterruptedException" that can be caught here. "InterruptedException" should not be ignored (java:S2142)
				final String message = JFaceMessages.get(ERR_SYSTEM_INFO_EXPORT);
				log.log(Level.SEVERE, message, e);
				SwtUtils.unblockShell(shell);
				EnhancedErrorDialog.openError(shell, JFaceMessages.get(LBL_SYSTEM_INFO_DIALOG_TITLE), message, IStatus.ERROR, e, shell.getDisplay().getSystemImage(SWT.ICON_ERROR));
			}
			finally {
				SwtUtils.unblockShell(shell);
			}
		}
	}

	protected String openSaveDialog(final Shell parent) {
		final FileDialog saveDialog = new FileDialog(parent, SWT.SAVE);
		saveDialog.setFilterExtensions(new String[] { "*.TXT;*.txt" });
		saveDialog.setFileName("sysinfo.txt");
		saveDialog.setOverwrite(true);
		return saveDialog.open();
	}

}
