package it.albertus.jface.preference.page;

import java.util.Map;
import java.util.logging.Level;

import org.eclipse.swt.widgets.Control;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.preference.StaticLabelsAndValues;
import it.albertus.util.logging.LoggingSupport;

public class LoggingPreferencePage extends BasePreferencePage {

	private String overriddenMessage = JFaceMessages.get("lbl.preferences.logging.overridden");

	@Override
	protected Control createHeader() {
		if (LoggingSupport.getInitialConfigurationProperty() != null) {
			return createInfoComposite(getFieldEditorParent(), overriddenMessage);
		}
		else {
			return null;
		}
	}

	public final String getOverriddenMessage() {
		return overriddenMessage;
	}

	public final void setOverriddenMessage(final String overriddenMessage) {
		this.overriddenMessage = overriddenMessage;
	}

	public static StaticLabelsAndValues getLoggingComboOptions() {
		final Map<Integer, Level> levels = LoggingSupport.getLevels();
		final StaticLabelsAndValues options = new StaticLabelsAndValues(levels.size());
		for (final Level level : levels.values()) {
			options.put(level.getName(), level.getName());
		}
		return options;
	}

}
