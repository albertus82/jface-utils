package it.albertus.jface.preference.field;

import javax.annotation.Nullable;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import it.albertus.jface.JFaceMessages;

public abstract class EnhancedListEditor extends FieldEditor {

	private final boolean container;

	private @Nullable List list;
	private @Nullable Composite buttonBox;
	private @Nullable Button addButton;
	private @Nullable Button editButton;
	private @Nullable Button removeButton;
	private @Nullable Button upButton;
	private @Nullable Button downButton;
	private @Nullable SelectionListener selectionListener;

	protected class ListEditorSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(@Nullable final SelectionEvent se) {
			if (se != null && se.widget != null) {
				final Widget widget = se.widget;
				if (widget == addButton) {
					addPressed();
				}
				else if (widget == editButton) {
					editPressed();
				}
				else if (widget == removeButton) {
					removePressed();
				}
				else if (widget == upButton) {
					upPressed();
				}
				else if (widget == downButton) {
					downPressed();
				}
				else if (widget == list) {
					selectionChanged();
				}
			}
		}
	}

	protected class ButtonBoxDisposeListener implements DisposeListener {
		@Override
		public void widgetDisposed(final DisposeEvent event) {
			addButton = null;
			editButton = null;
			removeButton = null;
			upButton = null;
			downButton = null;
			buttonBox = null;
		}
	}

	protected EnhancedListEditor() {
		container = false;
	}

	public EnhancedListEditor(final String name, final String labelText, final Composite parent) {
		this(name, labelText, parent, null);
	}

	public EnhancedListEditor(final String name, final String labelText, final Composite parent, final Integer horizontalSpan) {
		super(name, labelText, (horizontalSpan != null && horizontalSpan > 0) ? createContainer(parent, horizontalSpan) : parent);
		if (horizontalSpan != null && horizontalSpan > 0) {
			container = true;
		}
		else {
			container = false;
		}
	}

	protected static Composite createContainer(final Composite fieldEditorParent, final int horizontalSpan) {
		final Composite parent = new Composite(fieldEditorParent, SWT.NULL);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).span(horizontalSpan, 1).applyTo(parent);
		return parent;
	}

	@Override
	protected void checkParent(final Control control, final Composite parent) {
		super.checkParent(container ? control.getParent() : control, parent);
	}

	protected void addPressed() {
		setPresentsDefaultValue(false);
		final String input = getNewInputObject();
		if (input != null) {
			final int index = list.getSelectionIndex();
			if (index >= 0) {
				list.add(input, index + 1);
			}
			else {
				list.add(input, 0);
			}
			selectionChanged();
		}
	}

	protected void editPressed() {
		setPresentsDefaultValue(false);
		final int index = list.getSelectionIndex();
		final String input = getModifiedInputObject(list.getItem(index));
		if (input != null && index >= 0) {
			list.setItem(index, input);
			selectionChanged();
		}
	}

	@Override
	protected void adjustForNumColumns(final int numColumns) {
		final Control control = getLabelControl();
		((GridData) control.getLayoutData()).horizontalSpan = numColumns;
		((GridData) list.getLayoutData()).horizontalSpan = numColumns - 1;
	}

	protected abstract String createList(String[] items);

	protected Button createPushButton(final Composite parent, final String text) {
		final Button button = new Button(parent, SWT.PUSH);
		button.setText(text);
		button.setFont(parent.getFont());
		final GridData data = new GridData(GridData.FILL_HORIZONTAL);
		final int widthHint = convertHorizontalDLUsToPixels(button, IDialogConstants.BUTTON_WIDTH);
		data.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		button.setLayoutData(data);
		button.addSelectionListener(getSelectionListener());
		return button;
	}

	protected ListEditorSelectionListener createSelectionListener() {
		return new ListEditorSelectionListener();
	}

	@Override
	protected void doFillIntoGrid(final Composite parent, final int numColumns) {
		final Control control = getLabelControl(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = numColumns;
		control.setLayoutData(gd);

		list = getListControl(parent);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.FILL;
		gd.horizontalSpan = numColumns - 1;
		gd.widthHint = list.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		gd.grabExcessHorizontalSpace = false;
		list.setLayoutData(gd);

		buttonBox = getButtonBoxControl(parent);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		buttonBox.setLayoutData(gd);
	}

	@Override
	protected void doLoad() {
		if (list != null) {
			final String s = getPreferenceStore().getString(getPreferenceName());
			final String[] array = parseString(s);
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
		}
	}

	@Override
	protected void doLoadDefault() {
		if (list != null) {
			list.removeAll();
			final String s = getPreferenceStore().getDefaultString(getPreferenceName());
			final String[] array = parseString(s);
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
		}
	}

	@Override
	protected void doStore() {
		final String s = createList(list.getItems());
		if (s != null) {
			getPreferenceStore().setValue(getPreferenceName(), s);
		}
	}

	protected void createButtons(final Composite box) {
		createAddButton(box);
		createRemoveButton(box);
		createUpButton(box);
		createDownButton(box);
	}

	protected void createAddButton(final Composite box) {
		addButton = createPushButton(box, JFaceMessages.get("lbl.preferences.list.button.add"));
	}

	protected void createEditButton(final Composite box) {
		editButton = createPushButton(box, JFaceMessages.get("lbl.preferences.list.button.edit"));
	}

	protected void createRemoveButton(final Composite box) {
		removeButton = createPushButton(box, JFaceMessages.get("lbl.preferences.list.button.remove"));
	}

	protected void createUpButton(final Composite box) {
		upButton = createPushButton(box, JFaceMessages.get("lbl.preferences.list.button.up"));
	}

	protected void createDownButton(final Composite box) {
		downButton = createPushButton(box, JFaceMessages.get("lbl.preferences.list.button.down"));
	}

	public Composite getButtonBoxControl(final Composite parent) {
		if (buttonBox == null) {
			buttonBox = new Composite(parent, SWT.NULL);
			final GridLayout layout = new GridLayout();
			layout.marginWidth = 0;
			buttonBox.setLayout(layout);
			createButtons(buttonBox);
			buttonBox.addDisposeListener(createButtonBoxDisposeListener());
		}
		else {
			checkParent(buttonBox, parent);
		}

		selectionChanged();

		return buttonBox;
	}

	protected ButtonBoxDisposeListener createButtonBoxDisposeListener() {
		return new ButtonBoxDisposeListener();
	}

	public List getListControl(final Composite parent) {
		if (list == null) {
			list = new List(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
			list.setFont(parent.getFont());
			list.addSelectionListener(getSelectionListener());
			list.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent event) {
					list = null;
				}
			});
		}
		else {
			checkParent(list, parent);
		}
		return list;
	}

	protected abstract String getNewInputObject();

	protected String getModifiedInputObject(final String value) { // NOSONAR Hook
		return null;
	}

	@Override
	public int getNumberOfControls() {
		return 2;
	}

	protected SelectionListener getSelectionListener() {
		if (selectionListener == null) {
			selectionListener = createSelectionListener();
		}
		return selectionListener;
	}

	protected Shell getShell() {
		if (addButton == null) {
			return null;
		}
		return addButton.getShell();
	}

	protected abstract String[] parseString(String stringList);

	protected void removePressed() {
		setPresentsDefaultValue(false);
		final int index = list.getSelectionIndex();
		if (index >= 0) {
			list.remove(index);

			// Set new selection after remove
			final int size = list.getItemCount();
			if (size > 0) {
				if (index < size) {
					list.setSelection(index);
				}
				else {
					list.setSelection(size - 1);
				}
			}
			selectionChanged();
		}
	}

	protected void selectionChanged() {
		final int index = list.getSelectionIndex();
		final int size = list.getItemCount();
		if (editButton != null && !editButton.isDisposed()) {
			editButton.setEnabled(index >= 0);
		}
		if (removeButton != null && !removeButton.isDisposed()) {
			removeButton.setEnabled(index >= 0);
		}
		if (upButton != null && !upButton.isDisposed()) {
			upButton.setEnabled(size > 1 && index > 0);
		}
		if (downButton != null && !downButton.isDisposed()) {
			downButton.setEnabled(size > 1 && index >= 0 && index < size - 1);
		}
	}

	@Override
	public void setFocus() {
		if (list != null) {
			list.setFocus();
		}
	}

	@Override
	public void setEnabled(final boolean enabled, final Composite parent) {
		super.setEnabled(enabled, parent);
		final boolean hasSelection = list.getSelectionIndex() >= 0;
		getListControl(parent).setEnabled(enabled);
		if (addButton != null && !addButton.isDisposed()) {
			addButton.setEnabled(enabled);
		}
		if (editButton != null && !editButton.isDisposed()) {
			editButton.setEnabled(enabled && hasSelection);
		}
		if (removeButton != null && !removeButton.isDisposed()) {
			removeButton.setEnabled(enabled && hasSelection);
		}
		if (upButton != null && !upButton.isDisposed()) {
			upButton.setEnabled(enabled && hasSelection);
		}
		if (downButton != null && !downButton.isDisposed()) {
			downButton.setEnabled(enabled && hasSelection);
		}
	}

	protected void upPressed() {
		swap(true);
	}

	protected void downPressed() {
		swap(false);
	}

	protected void swap(final boolean up) {
		setPresentsDefaultValue(false);
		final int index = list.getSelectionIndex();
		final int target = up ? index - 1 : index + 1;

		if (index >= 0) {
			final String[] selection = list.getSelection();
			Assert.isTrue(selection.length == 1);
			list.remove(index);
			list.add(selection[0], target);
			list.setSelection(target);
		}
		selectionChanged();
	}

	protected List getList() {
		return list;
	}

	protected Composite getButtonBox() {
		return buttonBox;
	}

	protected Button getAddButton() {
		return addButton;
	}

	protected Button getEditButton() {
		return editButton;
	}

	protected Button getRemoveButton() {
		return removeButton;
	}

	protected Button getUpButton() {
		return upButton;
	}

	protected Button getDownButton() {
		return downButton;
	}

}
