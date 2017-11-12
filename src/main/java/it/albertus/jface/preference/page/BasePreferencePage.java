package it.albertus.jface.preference.page;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import it.albertus.jface.EnhancedErrorDialog;
import it.albertus.jface.JFaceMessages;
import it.albertus.jface.preference.IPreference;
import it.albertus.jface.preference.IPreferencesCallback;
import it.albertus.jface.preference.StaticLabelsAndValues;
import it.albertus.util.NewLine;
import it.albertus.util.logging.LoggerFactory;

public class BasePreferencePage extends FieldEditorPreferencePage {

	private static final Logger logger = LoggerFactory.getLogger(BasePreferencePage.class);

	protected static final Map<IPreference, FieldEditorWrapper> universe = new HashMap<IPreference, FieldEditorWrapper>();

	protected final Map<IPreference, FieldEditorWrapper> fieldEditorMap = new HashMap<IPreference, FieldEditorWrapper>();
	protected Control header;

	private IPreferencesCallback preferencesCallback;
	private IPreference[] preferences;
	private IPageDefinition pageDefinition;

	public BasePreferencePage() {
		this(GRID);
	}

	protected BasePreferencePage(final int style) {
		super(style);
	}

	public IPreferencesCallback getPreferencesCallback() {
		return preferencesCallback;
	}

	public void setPreferencesCallback(final IPreferencesCallback preferencesCallback) {
		this.preferencesCallback = preferencesCallback;
	}

	public IPreference[] getPreferences() {
		return preferences;
	}

	public void setPreferences(final IPreference[] preferences) {
		this.preferences = preferences;
	}

	public IPageDefinition getPageDefinition() {
		return pageDefinition;
	}

	public void setPageDefinition(final IPageDefinition pageDefinition) {
		this.pageDefinition = pageDefinition;
	}

	@Override
	public void createControl(final Composite parent) {
		super.createControl(parent);

		final Button defaultsButton = getDefaultsButton();
		defaultsButton.setText(JFaceMessages.get("lbl.preferences.button.defaults"));
		defaultsButton.setToolTipText(JFaceMessages.get("lbl.preferences.button.defaults.tooltip"));

		final Button applyButton = getApplyButton();
		applyButton.setText(JFaceMessages.get("lbl.button.apply"));
	}

	@Override
	protected void performApply() {
		super.performApply();

		// Save configuration file...
		try {
			((IPersistentPreferenceStore) getPreferenceStore()).save();
		}
		catch (final IOException ioe) {
			final String message = JFaceMessages.get("err.preferences.save");
			logger.log(Level.SEVERE, message, ioe);
			EnhancedErrorDialog.openError(getShell(), JFaceMessages.get("lbl.preferences.title"), message, IStatus.ERROR, ioe, Display.getCurrent().getSystemImage(SWT.ICON_ERROR));
		}

		// Reload RouterLogger configuration...
		try {
			preferencesCallback.reload(); // Callback
		}
		catch (final IOException ioe) {
			final String message = JFaceMessages.get("err.preferences.reload");
			logger.log(Level.WARNING, message, ioe);
			EnhancedErrorDialog.openError(getShell(), JFaceMessages.get("lbl.preferences.title"), message, IStatus.WARNING, ioe, Display.getCurrent().getSystemImage(SWT.ICON_WARNING));
		}
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
		for (final IPreference preference : preferences) {
			if (pageDefinition.equals(preference.getPageDefinition())) {
				if (preference.isSeparate()) {
					addSeparator();
				}
				final Composite fieldEditorParent = getFieldEditorParent();
				final FieldEditor fieldEditor = createFieldEditor(preference, fieldEditorParent);
				addField(fieldEditor);
				fieldEditorMap.put(preference, new FieldEditorWrapper(fieldEditor, fieldEditorParent));
			}
		}
		universe.putAll(fieldEditorMap);
	}

	protected FieldEditor createFieldEditor(final IPreference preference, final Composite parent) {
		return preference.createFieldEditor(parent);
	}

	// propertyChange -> updateChildrenStatus -> updateChildStatus
	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		super.propertyChange(event);
		if (event.getSource() instanceof BooleanFieldEditor) {
			final BooleanFieldEditor changedBooleanFieldEditor = (BooleanFieldEditor) event.getSource();
			final Boolean parentEnabled = (Boolean) event.getNewValue();
			for (final Entry<IPreference, FieldEditorWrapper> entry : fieldEditorMap.entrySet()) {
				if (entry.getValue().getFieldEditor().equals(changedBooleanFieldEditor)) {
					// Found!
					for (final IPreference childPreference : entry.getKey().getChildren()) {
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

	protected void updateChildrenStatus(final IPreference childPreference, final Boolean parentEnabled) {
		final FieldEditorWrapper childFieldEditor = fieldEditorMap.get(childPreference);
		updateChildStatus(childFieldEditor, parentEnabled);
		// Recurse descendants...
		if (childFieldEditor != null && childFieldEditor.getFieldEditor() instanceof BooleanFieldEditor) {
			final BooleanFieldEditor childBooleanFieldEditor = (BooleanFieldEditor) childFieldEditor.getFieldEditor();
			for (final IPreference descendantPreference : childPreference.getChildren()) { // Exit condition
				final boolean childEnabled = childBooleanFieldEditor.getBooleanValue();
				updateChildrenStatus(descendantPreference, childEnabled && parentEnabled);
			}
		}
	}

	protected void updateFieldsStatus() {
		for (final Entry<IPreference, FieldEditorWrapper> entry : fieldEditorMap.entrySet()) {
			if (entry.getValue().getFieldEditor() instanceof BooleanFieldEditor) {
				final boolean parentsEnabled = getParentsEnabled(entry.getKey());
				for (final IPreference childPreference : entry.getKey().getChildren()) {
					updateChildStatus(fieldEditorMap.get(childPreference), parentsEnabled);
				}
			}
		}
	}

	protected boolean getParentsEnabled(final IPreference preference) {
		final FieldEditorWrapper fieldEditorWrapper = universe.get(preference);
		boolean parentEnabled;
		if (fieldEditorWrapper == null) { // Field belongs to a not-yet-created page.
			parentEnabled = getBooleanFromStore(preference);
		}
		else {
			try {
				parentEnabled = ((BooleanFieldEditor) fieldEditorWrapper.getFieldEditor()).getBooleanValue();
			}
			catch (final NullPointerException e) { // Uninitialized field.
				logger.log(Level.FINE, e.toString(), e);
				parentEnabled = getPreferenceStore().getBoolean(preference.getName());
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
		for (final IPreference preference : fieldEditorMap.keySet()) { // iterate over all the preferences on the current page
			IPreference parentPreference = preference.getParent();
			if (parentPreference != null && !fieldEditorMap.containsKey(parentPreference)) { // if the parent is on another page
				final Set<IPreference> parents = new HashSet<IPreference>();

				while (parentPreference != null) {
					parents.add(parentPreference);
					parentPreference = parentPreference.getParent();
				}

				final boolean parentsEnabled = checkParentsEnabled(parents);
				updateChildrenStatus(preference, parentsEnabled);
			}
		}
	}

	protected boolean checkParentsEnabled(final Set<IPreference> parents) {
		boolean parentEnabled = true;
		for (final IPreference preference : parents) {
			final FieldEditorWrapper fieldEditorWrapper = universe.get(preference);
			if (fieldEditorWrapper == null) {
				parentEnabled = getBooleanFromStore(preference);
			}
			else {
				final FieldEditor fieldEditor = fieldEditorWrapper.getFieldEditor();
				if (fieldEditor instanceof BooleanFieldEditor) {
					try {
						parentEnabled = ((BooleanFieldEditor) fieldEditor).getBooleanValue();
					}
					catch (final NullPointerException e) {
						logger.log(Level.FINE, e.toString(), e);
						parentEnabled = getBooleanFromStore(preference);
					}
				}
			}
			if (!parentEnabled) {
				break; // don't waste time
			}
		}
		return parentEnabled;
	}

	protected boolean getBooleanFromStore(final IPreference preference) {
		return getPreferenceStore().getBoolean(preference.getName());
	}

	public Control getHeader() {
		return header;
	}

	/** Viene aggiunto automaticamente un separatore tra il testo e i campi. */
	protected Control createHeader() {
		return null;
	}

	protected Composite createInfoComposite(final Composite parent, final String message) {
		final Composite messageComposite = new Composite(parent, SWT.BORDER);
		messageComposite.setBackground(messageComposite.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		GridDataFactory.swtDefaults().applyTo(messageComposite);
		GridLayoutFactory.swtDefaults().margins(2, 2).applyTo(messageComposite);

		final Label messageLabel = new Label(messageComposite, SWT.BOLD | SWT.WRAP);
		messageLabel.setText(message);
		messageLabel.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		messageLabel.setBackground(messageLabel.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		GridDataFactory.swtDefaults().applyTo(messageLabel);
		return messageComposite;
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
