package io.github.albertus82.jface.preference.field;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import io.github.albertus82.jface.JFaceMessages;

public class ListFieldEditor extends FieldEditor implements FieldEditorDefault {

	private static final int DEFAULT_HEIGHT = 4;

	private final String[][] entryNamesAndValues;
	private final int height;
	private List list;
	private String fValue;

	private boolean defaultToolTip = true;

	public ListFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final Composite parent) {
		this(name, labelText, entryNamesAndValues, DEFAULT_HEIGHT, parent);
	}

	public ListFieldEditor(final String name, final String labelText, final String[][] entryNamesAndValues, final int height, final Composite parent) {
		init(name, labelText);
		Assert.isTrue(checkArray(entryNamesAndValues));
		this.entryNamesAndValues = entryNamesAndValues;
		this.height = height;
		createControl(parent);
	}

	protected boolean checkArray(final String[][] table) {
		if (table == null) {
			return false;
		}
		for (int i = 0; i < table.length; i++) {
			final String[] array = table[i];
			if (array == null || array.length != 2) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void adjustForNumColumns(final int numColumns) {
		final GridData gd = (GridData) list.getLayoutData();
		gd.horizontalSpan = numColumns - 1;
		gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
	}

	@Override
	public int getNumberOfControls() {
		return 2;
	}

	@Override
	protected void doFillIntoGrid(final Composite parent, final int numColumns) {
		getLabelControl(parent);

		list = getListControl(parent);
		GridDataFactory.swtDefaults().span(numColumns - 1, 1).align(SWT.FILL, SWT.CENTER).grab(true, false).hint(SWT.DEFAULT, list.getItemHeight() * height).applyTo(list);

		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (list != null && list.getSelectionCount() == 1) {
					fValue = entryNamesAndValues[list.getSelectionIndex()][1];
				}
			}
		});
	}

	@Override
	protected void createControl(final Composite parent) {
		super.createControl(parent);
		for (final String[] pair : entryNamesAndValues) {
			list.add(pair[0]);
		}
	}

	protected List getListControl(final Composite parent) {
		if (list == null) {
			list = new List(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		}
		else {
			checkParent(list, parent);
		}
		return list;
	}

	@Override
	protected void doLoad() {
		updateComboForValue(getPreferenceStore().getString(getPreferenceName()));
		setToolTipText();
	}

	@Override
	protected void doLoadDefault() {
		updateComboForValue(getDefaultValue());
	}

	protected void updateComboForValue(final String value) {
		this.fValue = value;
		for (int i = 0; i < entryNamesAndValues.length; i++) {
			if (value.equals(entryNamesAndValues[i][1])) {
				list.setSelection(new String[] { entryNamesAndValues[i][0] });
				return;
			}
		}
		if (entryNamesAndValues.length > 0) {
			this.fValue = entryNamesAndValues[0][1];
			list.setSelection(new String[] { entryNamesAndValues[0][0] });
		}
	}

	@Override
	protected void doStore() {
		if (fValue == null) {
			getPreferenceStore().setToDefault(getPreferenceName());
			return;
		}
		getPreferenceStore().setValue(getPreferenceName(), fValue);
	}

	protected String getDefaultValue() {
		return getPreferenceStore().getDefaultString(getPreferenceName());
	}

	protected String getNameForValue(final String value) {
		final String[][] namesAndValues = getEntryNamesAndValues();
		for (int i = 0; i < namesAndValues.length; i++) {
			final String[] entry = namesAndValues[i];
			if (value.equals(entry[1])) {
				return entry[0];
			}
		}
		return namesAndValues[0][0];
	}

	protected void setToolTipText() {
		if (defaultToolTip && getDefaultValue() != null && !getDefaultValue().isEmpty()) {
			String defaultValue = getNameForValue(getDefaultValue());
			if (getListControl() != null && !getListControl().isDisposed() && defaultValue != null && !defaultValue.isEmpty()) {
				getListControl().setToolTipText(JFaceMessages.get("lbl.preferences.default.value", defaultValue));
			}
		}
	}

	protected List getListControl() {
		return list;
	}

	@Override
	public boolean isDefaultToolTip() {
		return defaultToolTip;
	}

	@Override
	public void setDefaultToolTip(final boolean defaultToolTip) {
		this.defaultToolTip = defaultToolTip;
	}

	@Override
	public boolean isBoldCustomValues() {
		return false;
	}

	@Override
	public void setBoldCustomValues(final boolean boldCustomValues) {
		// List items cannot be formatted.
	}

	public String getValue() {
		return fValue;
	}

	public void setValue(String value) {
		this.fValue = value;
	}

	public String[][] getEntryNamesAndValues() {
		return entryNamesAndValues;
	}

	public int getHeight() {
		return height;
	}

}
