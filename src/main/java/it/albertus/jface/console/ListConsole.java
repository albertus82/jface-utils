package it.albertus.jface.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.SwtUtils;

public class ListConsole extends ScrollableConsole<List> {

	public static final int DEFAULT_LIMIT = 10000;

	private final StringBuilder charBuffer = new StringBuilder();

	private Menu contextMenu;
	private MenuItem copyMenuItem;
	private MenuItem selectAllMenuItem;
	private MenuItem clearMenuItem;

	protected ListConsole(final Composite parent, final Object layoutData, final boolean redirectSystemStream) {
		super(parent, layoutData, redirectSystemStream);
	}

	@Override
	protected List createScrollable(final Composite parent) {
		final List list = new List(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		configureStyledText(list);
		return list;
	}

	protected void configureStyledText(final List list) {
		createContextMenu(list);
	}

	protected void createContextMenu(final List list) {
		contextMenu = new Menu(list);

		copyMenuItem = createCopyMenuItem(list);

		new MenuItem(contextMenu, SWT.SEPARATOR);

		selectAllMenuItem = createSelectAllMenuItem(list);

		new MenuItem(contextMenu, SWT.SEPARATOR);

		clearMenuItem = createClearMenuItem();

		list.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(final MenuDetectEvent mde) {
				final boolean notEmpty = !isEmpty();
				selectAllMenuItem.setEnabled(notEmpty);
				clearMenuItem.setEnabled(notEmpty);
				copyMenuItem.setEnabled(list.getSelectionCount() > 0);
				contextMenu.setVisible(true);
			}
		});
		list.setMenu(contextMenu);
	}

	protected MenuItem createClearMenuItem() {
		final MenuItem clear = new MenuItem(contextMenu, SWT.PUSH);
		clear.setText(JFaceMessages.get("lbl.menu.item.clear"));
		clear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				clear();
			}
		});
		return clear;
	}

	protected MenuItem createSelectAllMenuItem(final List list) {
		final MenuItem selectAll = new MenuItem(contextMenu, SWT.PUSH);
		selectAll.setText(JFaceMessages.get("lbl.menu.item.select.all") + SwtUtils.getMod1ShortcutLabel(SwtUtils.KEY_SELECT_ALL));
		selectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				list.selectAll();
			}
		});
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent ke) {
				if (ke.stateMask == SWT.MOD1 && ke.keyCode == SwtUtils.KEY_SELECT_ALL) {
					ke.doit = false;
					list.selectAll();
				}
			}
		});
		return selectAll;
	}

	protected MenuItem createCopyMenuItem(final List list) {
		final MenuItem copy = new MenuItem(contextMenu, SWT.PUSH);
		copy.setText(JFaceMessages.get("lbl.menu.item.copy") + SwtUtils.getMod1ShortcutLabel(SwtUtils.KEY_COPY));
		copy.setAccelerator(SWT.MOD1 | SwtUtils.KEY_COPY);
		copy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				copy(list);
			}
		});
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.stateMask == SWT.MOD1 && e.keyCode == SwtUtils.KEY_COPY) {
					e.doit = false; // avoids unwanted scrolling
					copy(list);
				}
			}
		});
		return copy;
	}

	@Override
	public void clear() {
		scrollable.removeAll();
	}

	@Override
	public boolean isEmpty() {
		return scrollable.getItemCount() == 0;
	}

	@Override
	public boolean hasSelection() {
		return scrollable.getSelectionCount() > 0;
	}

	public int getLastItemIndex() {
		return scrollable.getItemCount() - 1;
	}

	@Override
	protected int getDefaultLimit() {
		return DEFAULT_LIMIT;
	}

	@Override
	protected void doPrint(final String value, final int maxItems) {
		scrollable.setRedraw(false);

		final int overflow = getLastItemIndex() - maxItems;
		if (overflow > 0) {
			final int[] indexes = new int[overflow];
			for (int i = 0; i < overflow; i++) {
				indexes[i] = i;
			}
			scrollable.remove(indexes);
		}

		charBuffer.setLength(0);
		if (isEmpty()) {
			scrollable.add("");
		}
		else {
			charBuffer.append(scrollable.getItem(getLastItemIndex()));
		}
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '\r' && value.length() > i + 1 && value.charAt(i + 1) == '\n') {
				newLine();
				i++;
			}
			else if (value.charAt(i) == '\r' || value.charAt(i) == '\n') {
				newLine();
			}
			else {
				charBuffer.append(value.charAt(i));
			}
		}
		scrollable.setItem(getLastItemIndex(), charBuffer.toString());
		scrollable.setTopIndex(getLastItemIndex());

		scrollable.setRedraw(true);
	}

	private void newLine() {
		scrollable.setItem(Math.max(getLastItemIndex(), 0), charBuffer.toString());
		charBuffer.setLength(0);
		scrollable.add("");
	}

	private void copy(final List list) {
		if (list.getSelectionIndex() != -1) {
			final StringBuilder text = new StringBuilder();
			for (int i = 0; i < list.getSelectionCount(); i++) {
				text.append(list.getSelection()[i]);
				if (i != list.getSelectionCount() - 1) {
					text.append(newLine);
				}
			}
			final Clipboard clipboard = new Clipboard(list.getDisplay());
			clipboard.setContents(new String[] { text.toString() }, new TextTransfer[] { TextTransfer.getInstance() });
			clipboard.dispose();
		}
	}

}
