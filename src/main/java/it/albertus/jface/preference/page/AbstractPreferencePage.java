package it.albertus.jface.preference.page;

import it.albertus.jface.JFaceResources;
import it.albertus.jface.preference.Preference;
import it.albertus.jface.preference.StaticLabelsAndValues;
import it.albertus.util.Configuration;
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

	protected static final Map<Preference, FieldEditorWrapper> universe = new HashMap<Preference, FieldEditorWrapper>();

	protected final Map<Preference, FieldEditorWrapper> fieldEditorMap = new HashMap<Preference, FieldEditorWrapper>();
	protected Control header;

	private Configuration configuration;
	private Preference[] preferences;
	private PageDefinition pageDefinition;

	public AbstractPreferencePage() {
		this(GRID);
	}

	protected AbstractPreferencePage(final int style) {
		super(style);
	}

	public final Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	public final Preference[] getPreferences() {
		return preferences;
	}

	public void setPreferences(final Preference[] preferences) {
		this.preferences = preferences;
	}

	public final PageDefinition getPageDefinition() {
		return pageDefinition;
	}

	public void setPageDefinition(final PageDefinition pageDefinition) {
		this.pageDefinition = pageDefinition;
	}

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
		for (final Preference preference : preferences) {
			if (pageDefinition.equals(preference.getPageDefinition())) {
				if (preference.hasSeparator()) {
					addSeparator();
				}
				final Composite fieldEditorParent = getFieldEditorParent();
				final FieldEditor fieldEditor = preference.createFieldEditor(fieldEditorParent);
				addField(fieldEditor);
				fieldEditorMap.put(preference, new FieldEditorWrapper(fieldEditor, fieldEditorParent));
			}
		}
		universe.putAll(fieldEditorMap);
	}

	/* propertyChange -> updateChildrenStatus -> updateChildStatus */
	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		super.propertyChange(event);
		if (event.getSource() instanceof BooleanFieldEditor) {
			final BooleanFieldEditor changedBooleanFieldEditor = (BooleanFieldEditor) event.getSource();
			final Boolean parentEnabled = (Boolean) event.getNewValue();
			for (final Entry<Preference, FieldEditorWrapper> entry : fieldEditorMap.entrySet()) {
				if (entry.getValue().getFieldEditor().equals(changedBooleanFieldEditor)) {
					// Found!
					for (final Preference childPreference : entry.getKey().getChildren()) {
						updateChildrenStatus(childPreference, parentEnabled);
					}
					break; // Done!
				}
			}
		}
	}

	// initialize -> updateFieldsStatus -> getParentsEnabled -> updateChildStatus
	@Override
	protected void initialize() {
		super.initialize();
		updateFieldsStatus();
	}

	// performDefaults -> updateFieldsStatus -> getParentsEnabled -> updateChildStatus
	@Override
	protected void performDefaults() {
		super.performDefaults();
		updateFieldsStatus();
	}

	protected void updateChildrenStatus(final Preference childPreference, final Boolean parentEnabled) {
		final FieldEditorWrapper childFieldEditor = fieldEditorMap.get(childPreference);
		updateChildStatus(childFieldEditor, parentEnabled);
		// Recurse descendants...
		if (childFieldEditor != null && childFieldEditor.getFieldEditor() instanceof BooleanFieldEditor) {
			final BooleanFieldEditor childBooleanFieldEditor = (BooleanFieldEditor) childFieldEditor.getFieldEditor();
			for (final Preference descendantPreference : childPreference.getChildren()) { // Exit condition
				final boolean childEnabled = childBooleanFieldEditor.getBooleanValue();
				updateChildrenStatus(descendantPreference, childEnabled && parentEnabled);
			}
		}
	}

	protected void updateFieldsStatus() {
		for (final Entry<Preference, FieldEditorWrapper> entry : fieldEditorMap.entrySet()) {
			if (entry.getValue().getFieldEditor() instanceof BooleanFieldEditor) {
				final boolean parentsEnabled = getParentsEnabled(entry.getKey());
				for (final Preference childPreference : entry.getKey().getChildren()) {
					updateChildStatus(fieldEditorMap.get(childPreference), parentsEnabled);
				}
			}
		}
	}

	protected boolean getParentsEnabled(final Preference preference) {
		final FieldEditorWrapper fieldEditorWrapper = universe.get(preference);
		boolean parentEnabled;
		if (fieldEditorWrapper == null) { // Field belongs to a not-yet-created page.
			parentEnabled = configuration.getBoolean(preference.getConfigurationKey(), Boolean.parseBoolean(preference.getDefaultValue()));
		}
		else {
			try {
				parentEnabled = ((BooleanFieldEditor) fieldEditorWrapper.getFieldEditor()).getBooleanValue();
			}
			catch (final NullPointerException npe) { // Uninitialized field.
				parentEnabled = getPreferenceStore().getBoolean(preference.getConfigurationKey());
			}
		}

		if (preference.getParent() != null) {
			return parentEnabled && getParentsEnabled(preference.getParent());
		}
		else {
			return parentEnabled;
		}
	}

	protected void updateChildStatus(final FieldEditorWrapper childFieldEditor, final boolean parentEnabled) {
		if (childFieldEditor != null) { // null if on another page!
			final FieldEditor fieldEditor = childFieldEditor.getFieldEditor();
			fieldEditor.setEnabled(parentEnabled, childFieldEditor.getParent());
			if (!parentEnabled && !fieldEditor.isValid()) {
				fieldEditor.loadDefault(); // Fix invalid value
				checkState(); // Enable OK & Apply buttons
			}
		}
	}

	public void updateCrossChildrenStatus() {
		for (final Preference preference : fieldEditorMap.keySet()) {
			if (preference.getParent() != null && !fieldEditorMap.containsKey(preference.getParent())) {
				final FieldEditorWrapper fieldEditorWrapper = universe.get(preference.getParent());
				if (fieldEditorWrapper == null) {
					updateChildrenStatus(preference, configuration.getBoolean(preference.getParent().getConfigurationKey(), Boolean.parseBoolean(preference.getParent().getDefaultValue())));
				}
				else {
					final FieldEditor fieldEditor = fieldEditorWrapper.getFieldEditor();
					if (fieldEditor instanceof BooleanFieldEditor) {
						boolean parentEnabled;
						try {
							parentEnabled = ((BooleanFieldEditor) fieldEditor).getBooleanValue();
						}
						catch (final NullPointerException npe) {
							parentEnabled = getPreferenceStore().getBoolean(preference.getParent().getConfigurationKey());
						}
						updateChildrenStatus(preference, parentEnabled);
					}
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

	public static StaticLabelsAndValues getNewLineComboOptions() {
		final NewLine[] values = NewLine.values();
		final StaticLabelsAndValues options = new StaticLabelsAndValues(values.length);
		for (final NewLine newLine : values) {
			final String value = newLine.name();
			options.put(value, value);
		}
		return options;
	}

}
