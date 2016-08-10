package it.albertus.jface.preference.page;

import it.albertus.jface.JFaceResources;
import it.albertus.jface.preference.LocalizedComboEntryNamesAndValues;
import it.albertus.jface.preference.Preference;
import it.albertus.util.Configuration;
import it.albertus.util.Localized;
import it.albertus.util.NewLine;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public abstract class AbstractPreferencePage extends FieldEditorPreferencePage {

	protected static final Map<Preference, FieldEditor> universe = new HashMap<Preference, FieldEditor>();

	protected final Configuration configuration;
	protected final Preference[] preferences;
	protected final Map<Preference, FieldEditor> fieldEditorMap = new HashMap<Preference, FieldEditor>();
	protected Control header;

	public AbstractPreferencePage(final Configuration configuration, final Preference[] preferences) {
		this(configuration, preferences, GRID);
	}

	protected AbstractPreferencePage(final Configuration configuration, final Preference[] preferences, final int style) {
		super(style);
		this.configuration = configuration;
		this.preferences = preferences;
	}

	protected abstract Page getPage();

	@Override
	public void createControl(final Composite parent) {
		super.createControl(parent);

		final Button defaultsButton = getDefaultsButton();
		defaultsButton.setText(JFaceResources.get("lbl.preferences.button.defaults"));
		defaultsButton.setToolTipText(JFaceResources.get("lbl.preferences.button.defaults.tooltip"));

		final Button applyButton = getApplyButton();
		applyButton.setText(JFaceResources.get("lbl.button.apply"));
	}

	@Override
	protected void performApply() {
		super.performApply();

		// Save configuration file...
		OutputStream configurationOutputStream = null;
		try {
			configurationOutputStream = configuration.openConfigurationOutputStream();
			((PreferenceStore) getPreferenceStore()).save(configurationOutputStream, null);
		}
		catch (final IOException ioe) {
			throw new RuntimeException(ioe);
		}
		finally {
			try {
				configurationOutputStream.close();
			}
			catch (final Exception exception) {}
		}

		// Reload RouterLogger configuration...
		configuration.reload();
	}

	@Override
	protected void createFieldEditors() {
		// Header
		header = createHeader();
		if (header != null) {
			GridDataFactory.fillDefaults().span(Short.MAX_VALUE, 1).applyTo(header);
			addSeparator();
		}

		// Fields
		final Page page = getPage();
		for (final Preference preference : preferences) {
			if (page.equals(preference.getPage())) {
				final FieldEditor fieldEditor = preference.createFieldEditor(getFieldEditorParent());
				addField(fieldEditor);
				fieldEditorMap.put(preference, fieldEditor);
			}
		}
		universe.putAll(fieldEditorMap);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		super.propertyChange(event);
		if (event.getSource() instanceof BooleanFieldEditor) {
			final BooleanFieldEditor changedBooleanFieldEditor = (BooleanFieldEditor) event.getSource();
			final Boolean parentEnabled = (Boolean) event.getNewValue();
			for (final Entry<Preference, FieldEditor> entry : fieldEditorMap.entrySet()) {
				if (entry.getValue().equals(changedBooleanFieldEditor)) {
					// Found!
					for (final Preference childPreference : entry.getKey().getChildren()) {
						updateChildrenStatus(childPreference, parentEnabled);
					}
					break; // Done!
				}
			}
		}
	}

	@Override
	protected void initialize() {
		super.initialize();
		updateFieldsStatus();
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		updateFieldsStatus();
	}

	protected void updateChildrenStatus(final Preference childPreference, final Boolean parentEnabled) {
		final FieldEditor childFieldEditor = fieldEditorMap.get(childPreference);
		updateChildStatus(childFieldEditor, parentEnabled);
		// Recurse descendants...
		if (childFieldEditor instanceof BooleanFieldEditor) {
			final BooleanFieldEditor childBooleanFieldEditor = (BooleanFieldEditor) childFieldEditor;
			for (final Preference descendantPreference : childPreference.getChildren()) { // Exit condition
				final boolean childEnabled = childBooleanFieldEditor.getBooleanValue();
				updateChildrenStatus(descendantPreference, childEnabled && parentEnabled);
			}
		}
	}

	protected void updateFieldsStatus() {
		for (final Entry<Preference, FieldEditor> entry : fieldEditorMap.entrySet()) {
			if (entry.getValue() instanceof BooleanFieldEditor) {
				final boolean parentsEnabled = getParentsEnabled(entry.getKey());
				for (final Preference childPreference : entry.getKey().getChildren()) {
					updateChildStatus(fieldEditorMap.get(childPreference), parentsEnabled);
				}
			}
		}
	}

	protected boolean getParentsEnabled(final Preference preference) {
		final FieldEditor fieldEditor = fieldEditorMap.get(preference);
		if (fieldEditor == null) {
			return true; // The parent is on another page, checked by updateCrossChildrenStatus().
		}
		boolean parentEnabled = ((BooleanFieldEditor) fieldEditor).getBooleanValue();
		if (preference.getParent() != null) {
			return parentEnabled && getParentsEnabled(preference.getParent());
		}
		else {
			return parentEnabled;
		}
	}

	protected void updateChildStatus(final FieldEditor childFieldEditor, final boolean parentEnabled) {
		if (childFieldEditor != null) { // null if on another page!
			childFieldEditor.setEnabled(parentEnabled, getFieldEditorParent());
			if (!parentEnabled && !childFieldEditor.isValid()) {
				childFieldEditor.loadDefault(); // Fix invalid value
				checkState(); // Enable OK & Apply buttons
			}
		}
	}

	public void updateCrossChildrenStatus() {
		for (final Entry<Preference, FieldEditor> entry : fieldEditorMap.entrySet()) {
			if (entry.getKey().getParent() != null && !fieldEditorMap.containsKey(entry.getKey().getParent())) {
				final FieldEditor fieldEditor = universe.get(entry.getKey().getParent());
				if (fieldEditor == null) {
					updateChildrenStatus(entry.getKey(), configuration.getBoolean(entry.getKey().getParent().getConfigurationKey()));
				}
				else if (fieldEditor instanceof BooleanFieldEditor) {
					boolean parentEnabled;
					try {
						parentEnabled = ((BooleanFieldEditor) fieldEditor).getBooleanValue();
					}
					catch (final NullPointerException npe) {
						parentEnabled = getPreferenceStore().getBoolean(entry.getKey().getParent().getConfigurationKey());
					}
					updateChildrenStatus(entry.getKey(), parentEnabled);
				}
			}
		}
	}

	public Control getHeader() {
		return header;
	}

	/** Viene aggiunto automaticamente un separatore tra il testo e i campi. */
	protected Control createHeader() {
		return null;
	}

	protected void addSeparator() {
		final Label separator = new Label(getFieldEditorParent(), SWT.HORIZONTAL | SWT.SEPARATOR);
		GridDataFactory.fillDefaults().span(Short.MAX_VALUE, 1).grab(true, false).applyTo(separator);
	}

	public static LocalizedComboEntryNamesAndValues getNewLineComboOptions() {
		final NewLine[] values = NewLine.values();
		final LocalizedComboEntryNamesAndValues options = new LocalizedComboEntryNamesAndValues(values.length);
		for (final NewLine newLine : values) {
			final String value = newLine.name();
			final Localized name = new Localized() {
				@Override
				public String getString() {
					return value;
				}
			};
			options.put(name, value);
		}
		return options;
	}

}
