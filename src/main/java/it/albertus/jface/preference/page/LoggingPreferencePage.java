package it.albertus.jface.preference.page;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

	public static StaticLabelsAndValues getLoggingLevelComboOptions(final Level min, final Level max) {
		final Map<Integer, Level> levels = LoggingSupport.getLevels();
		final StaticLabelsAndValues options = new StaticLabelsAndValues(levels.size());
		for (final Entry<Integer, Level> entry : levels.entrySet()) {
			if ((min == null || entry.getKey().intValue() >= min.intValue()) && (max == null || entry.getKey().intValue() <= max.intValue())) {
				options.put(entry.getValue().getName(), entry.getValue().getName());
			}
		}
		return options;
	}

	public static StaticLabelsAndValues getLoggingLevelComboOptions(final Level... levels) {
		final StaticLabelsAndValues options = new StaticLabelsAndValues(levels.length);
		final List<Level> levelsList = Arrays.asList(levels);
		for (final Level level : LoggingSupport.getLevels().values()) {
			if (levelsList.contains(level)) {
				options.put(level.getName(), level.getName());
			}
		}
		return options;
	}

	public static StaticLabelsAndValues getLoggingLevelComboOptions() {
		return getLoggingLevelComboOptions(null, null);
	}

}
