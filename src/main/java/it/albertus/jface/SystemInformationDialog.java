package it.albertus.jface;

import java.lang.management.ManagementPermission;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import it.albertus.util.NewLine;
import it.albertus.util.logging.LoggerFactory;

public class SystemInformationDialog extends Dialog {

	private static final String LBL_MENU_ITEM_COPY = "lbl.menu.item.copy";
	private static final String LBL_MENU_ITEM_SELECT_ALL = "lbl.menu.item.select.all";

	private static final float MONITOR_SIZE_DIVISOR = 1.67f;

	private static final String TABLE_ITEM_FONT_SYMBOLIC_NAME = SystemInformationDialog.class.getName();

	private static final Logger logger = LoggerFactory.getLogger(SystemInformationDialog.class);

	/** The map contaning the system properties (can be null). */
	protected @Nullable final Map<String, String> properties;

	/** The map containing the environment variables (can be null). */
	protected @Nullable final Map<String, String> env;

	/** The collection containing the JVM arguments (can be null). */
	protected @Nullable final Collection<String> jvmArgs;

	/**
	 * Construct a new instance of the <em>System Information</em> dialog with
	 * one or more collections of data.
	 * 
	 * @param parent the parent control
	 * @param properties a map contaning the system properties (can be null)
	 * @param env a map containing the environment variables (can be null)
	 * @param jvmArgs a collection containing the JVM arguments (can be null)
	 * 
	 * @see #SystemInformationDialog(Shell, int, Map, Map, Collection)
	 * @see org.eclipse.swt.widgets.Dialog#Dialog(Shell, int)
	 */
	public SystemInformationDialog(final Shell parent, @Nullable final Map<String, String> properties, @Nullable final Map<String, String> env, @Nullable final Collection<String> jvmArgs) {
		this(parent, SWT.SHEET | SWT.RESIZE | SWT.MAX, properties, env, jvmArgs);
	}

	/**
	 * Construct a new instance of the <em>System Information</em> dialog with
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
	public SystemInformationDialog(final Shell parent, final int style, @Nullable final Map<String, String> properties, @Nullable final Map<String, String> env, @Nullable final Collection<String> jvmArgs) {
		super(parent, style);
		this.properties = properties;
		this.env = env;
		this.jvmArgs = jvmArgs;
		setText(JFaceMessages.get("lbl.system.info.dialog.title"));
	}

	/** Open this <em>System Information</em> dialog. */
	public void open() {
		final Shell shell = new Shell(getParent(), getStyle());
		shell.setText(getText());
		shell.setImage(shell.getDisplay().getSystemImage(SWT.ICON_INFORMATION));
		createContents(shell);
		constrainShellSize(shell);
		shell.open();
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
			final Table propertiesTable = createTable(folder, properties);
			final TabItem propertiesTab = new TabItem(folder, SWT.NONE);
			propertiesTab.setText(JFaceMessages.get("lbl.system.info.tab.properties"));
			propertiesTab.setControl(propertiesTable);
		}

		if (env != null) {
			final TabItem envTab = new TabItem(folder, SWT.NONE);
			envTab.setText(JFaceMessages.get("lbl.system.info.tab.env"));
			final Table envTable = createTable(folder, env);
			envTab.setControl(envTable);
		}

		if (jvmArgs != null) {
			final TabItem jvmArgsTab = new TabItem(folder, SWT.NONE);
			jvmArgsTab.setText(JFaceMessages.get("lbl.system.info.tab.jvmArgs"));
			final List jvmArgsList = createList(folder, jvmArgs);
			jvmArgsTab.setControl(jvmArgsList);
		}

		shell.setDefaultButton(createButton(shell));
	}

	protected Table createTable(final Composite parent, final Map<String, String> map) {
		final Table table = new Table(parent, SWT.FULL_SELECTION | SWT.MULTI);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);

		for (final String title : new String[] { JFaceMessages.get("lbl.system.info.table.key"), JFaceMessages.get("lbl.system.info.table.value") }) {
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

		final Menu contextMenu = new Menu(table);

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
		return table;
	}

	protected List createList(final Composite parent, final Collection<String> jvmArgs) {
		final List list = new List(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(list);

		for (final String arg : jvmArgs) {
			list.add(arg);
		}
		list.pack();

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

		final Menu contextMenu = new Menu(list);

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
		return list;
	}

	protected Button createButton(final Shell shell) {
		final Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText(JFaceMessages.get("lbl.button.close"));
		final int buttonWidth = SwtUtils.convertHorizontalDLUsToPixels(okButton, IDialogConstants.BUTTON_WIDTH);
		GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).minSize(buttonWidth, SWT.DEFAULT).applyTo(okButton);
		okButton.setFocus();
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(@Nullable final SelectionEvent se) {
				shell.close();
			}
		});
		return okButton;
	}

	/** Copies the current selection to the clipboard. */
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

			final Clipboard clipboard = new Clipboard(table.getDisplay());
			clipboard.setContents(new String[] { data.toString() }, new TextTransfer[] { TextTransfer.getInstance() });
			clipboard.dispose();
		}
	}

	protected void copy(final List list) {
		if (canCopy(list)) {
			final StringBuilder data = new StringBuilder();

			for (int row = 0; row < list.getSelectionCount(); row++) {
				data.append(list.getSelection()[row]);
				if (row != list.getSelectionCount() - 1) {
					data.append(NewLine.SYSTEM_LINE_SEPARATOR);
				}
			}

			final Clipboard clipboard = new Clipboard(list.getDisplay());
			clipboard.setContents(new String[] { data.toString() }, new TextTransfer[] { TextTransfer.getInstance() });
			clipboard.dispose();
		}
	}

	protected boolean canCopy(final Table table) {
		return !table.isDisposed() && table.getColumnCount() > 0 && table.getSelectionCount() > 0;
	}

	protected boolean canCopy(final List list) {
		return !list.isDisposed() && list.getSelectionCount() > 0;
	}

	/**
	 * Determine if the dialog can be populated with informations. The presence
	 * of a {@link SecurityManager} could limit the informations that can be
	 * retrieved.
	 * 
	 * @return {@code true} if there are informations to show, otherwise
	 *         {@code false}.
	 */
	public static boolean isAvailable() {
		final SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			int count = 3;
			try {
				sm.checkPropertiesAccess(); // system properties
			}
			catch (final SecurityException e) {
				logger.log(Level.FINE, e.toString(), e);
				count--;
			}
			try {
				sm.checkPermission(new RuntimePermission("getenv.*")); // environment variables 
			}
			catch (final SecurityException e) {
				logger.log(Level.FINE, e.toString(), e);
				count--;
			}
			try {
				sm.checkPermission(new ManagementPermission("monitor")); // jvm args 
			}
			catch (final SecurityException e) {
				logger.log(Level.FINE, e.toString(), e);
				count--;
			}
			if (count == 0) {
				return false;
			}
		}
		return true;
	}

}
