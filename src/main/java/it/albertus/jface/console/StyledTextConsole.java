package it.albertus.jface.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.SwtUtils;

public class StyledTextConsole extends AbstractTextConsole<StyledText> {

	protected static final int MARGIN = 4;

	private Menu contextMenu;
	private MenuItem copyMenuItem;
	private MenuItem selectAllMenuItem;
	private MenuItem clearMenuItem;

	public StyledTextConsole(final Composite parent, final Object layoutData, final boolean redirectSystemStreams) {
		super(parent, layoutData, redirectSystemStreams);
	}

	@Override
	protected StyledText createScrollable(final Composite parent) {
		final StyledText styledText = new StyledText(parent, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
		configureStyledText(styledText);
		return styledText;
	}

	protected void configureStyledText(final StyledText styledText) {
		styledText.setMargins(MARGIN, MARGIN, MARGIN, MARGIN); // like the Text control
		createContextMenu(styledText);
	}

	protected void createContextMenu(final StyledText styledText) {
		contextMenu = new Menu(styledText);

		copyMenuItem = createCopyMenuItem(styledText);

		new MenuItem(contextMenu, SWT.SEPARATOR);

		selectAllMenuItem = createSelectAllMenuItem(styledText);

		new MenuItem(contextMenu, SWT.SEPARATOR);

		clearMenuItem = createClearMenuItem();

		styledText.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(final MenuDetectEvent mde) {
				final boolean charCount = styledText.getCharCount() > 0;
				selectAllMenuItem.setEnabled(charCount);
				clearMenuItem.setEnabled(charCount);
				copyMenuItem.setEnabled(styledText.getSelectionCount() > 0);
			}
		});
		styledText.setMenu(contextMenu);
	}

	protected MenuItem createClearMenuItem() {
		final MenuItem clearMenuItem = new MenuItem(contextMenu, SWT.PUSH);
		clearMenuItem.setText(JFaceMessages.get("lbl.menu.item.clear"));
		clearMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				clear();
			}
		});
		return clearMenuItem;
	}

	protected MenuItem createSelectAllMenuItem(final StyledText styledText) {
		final MenuItem selectAllMenuItem = new MenuItem(contextMenu, SWT.PUSH);
		selectAllMenuItem.setText(JFaceMessages.get("lbl.menu.item.select.all") + SwtUtils.getMod1ShortcutLabel(SwtUtils.KEY_SELECT_ALL));
		selectAllMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				styledText.invokeAction(ST.SELECT_ALL);
			}
		});
		styledText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent ke) {
				if (ke.stateMask == SWT.MOD1 && ke.keyCode == SwtUtils.KEY_SELECT_ALL) {
					styledText.invokeAction(ST.SELECT_ALL);
				}
			}
		});
		return selectAllMenuItem;
	}

	protected MenuItem createCopyMenuItem(final StyledText styledText) {
		final MenuItem copyMenuItem = new MenuItem(contextMenu, SWT.PUSH);
		copyMenuItem.setText(JFaceMessages.get("lbl.menu.item.copy") + SwtUtils.getMod1ShortcutLabel(SwtUtils.KEY_COPY));
		copyMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent se) {
				styledText.invokeAction(ST.COPY);
			}
		});
		return copyMenuItem;
	}

	@Override
	public void clear() {
		scrollable.setText("");
	}

	@Override
	public boolean isEmpty() {
		return scrollable == null || scrollable.isDisposed() || scrollable.getCharCount() == 0;
	}

	@Override
	public boolean hasSelection() {
		return scrollable != null && !scrollable.isDisposed() && scrollable.getSelectionCount() > 0;
	}

	@Override
	protected void doPrint(final String value, final int maxChars) {
		if (scrollable.getCharCount() < maxChars) {
			scrollable.append(value);
		}
		else {
			scrollable.setText(value.startsWith(newLine) ? value.substring(newLine.length()) : value);
		}
		scrollable.setTopPixel((scrollable.getLineCount() - 1) * scrollable.getLineHeight());
	}

	protected Menu getContextMenu() {
		return contextMenu;
	}

	protected void setContextMenu(final Menu contextMenu) {
		this.contextMenu = contextMenu;
	}

	protected MenuItem getCopyMenuItem() {
		return copyMenuItem;
	}

	protected void setCopyMenuItem(final MenuItem copyMenuItem) {
		this.copyMenuItem = copyMenuItem;
	}

	protected MenuItem getSelectAllMenuItem() {
		return selectAllMenuItem;
	}

	protected void setSelectAllMenuItem(final MenuItem selectAllMenuItem) {
		this.selectAllMenuItem = selectAllMenuItem;
	}

	protected MenuItem getClearMenuItem() {
		return clearMenuItem;
	}

	protected void setClearMenuItem(final MenuItem clearMenuItem) {
		this.clearMenuItem = clearMenuItem;
	}

}
