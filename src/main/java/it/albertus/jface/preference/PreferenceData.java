package it.albertus.jface.preference;

import it.albertus.util.Localized;

public class PreferenceData {

	private final String configurationKey;
	private final Localized label;
	private final String defaultValue;
	private final Preference parent;
	private final boolean restartRequired;
	private final boolean separator;

	public String getConfigurationKey() {
		return configurationKey;
	}

	public Localized getLabel() {
		return label;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public Preference getParent() {
		return parent;
	}

	public boolean isRestartRequired() {
		return restartRequired;
	}

	public boolean hasSeparator() {
		return separator;
	}

	@Override
	public String toString() {
		return "PreferenceData [configurationKey=" + configurationKey + ", label=" + label + ", defaultValue=" + defaultValue + ", parent=" + parent + ", restartRequired=" + restartRequired + ", separator=" + separator + "]";
	}

	public static class PreferenceDataBuilder {
		private String configurationKey;
		private Localized label;
		private String defaultValue;
		private Preference parent;
		private boolean restartRequired;
		private boolean separator;

		public PreferenceDataBuilder configurationKey(final String configurationKey) {
			this.configurationKey = configurationKey;
			return this;
		}

		public PreferenceDataBuilder label(final Localized label) {
			this.label = label;
			return this;
		}

		public PreferenceDataBuilder label(final String label) {
			this.label = new Localized() {
				@Override
				public String getString() {
					return label;
				}
			};
			return this;
		}

		public PreferenceDataBuilder defaultValue(final String defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}

		public PreferenceDataBuilder defaultValue(final boolean defaultValue) {
			this.defaultValue = Boolean.toString(defaultValue);
			return this;
		}

		public PreferenceDataBuilder defaultValue(final byte defaultValue) {
			this.defaultValue = Byte.toString(defaultValue);
			return this;
		}

		public PreferenceDataBuilder defaultValue(final short defaultValue) {
			this.defaultValue = Short.toString(defaultValue);
			return this;
		}

		public PreferenceDataBuilder defaultValue(final char defaultValue) {
			this.defaultValue = Character.toString(defaultValue);
			return this;
		}

		public PreferenceDataBuilder defaultValue(final int defaultValue) {
			this.defaultValue = Integer.toString(defaultValue);
			return this;
		}

		public PreferenceDataBuilder defaultValue(final long defaultValue) {
			this.defaultValue = Long.toString(defaultValue);
			return this;
		}

		public PreferenceDataBuilder defaultValue(final float defaultValue) {
			this.defaultValue = Float.toString(defaultValue);
			return this;
		}

		public PreferenceDataBuilder defaultValue(final double defaultValue) {
			this.defaultValue = Double.toString(defaultValue);
			return this;
		}

		public PreferenceDataBuilder parent(final Preference parent) {
			this.parent = parent;
			return this;
		}

		private PreferenceDataBuilder restartRequired(final boolean restartRequired) {
			this.restartRequired = restartRequired;
			return this;
		}

		public PreferenceDataBuilder restartRequired() {
			return restartRequired(true);
		}

		private PreferenceDataBuilder separator(final boolean separator) {
			this.separator = separator;
			return this;
		}

		public PreferenceDataBuilder separator() {
			return separator(true);
		}

		public PreferenceData build() {
			return new PreferenceData(this);
		}
	}

	private PreferenceData(final PreferenceDataBuilder builder) {
		this.configurationKey = builder.configurationKey;
		this.label = builder.label;
		this.defaultValue = builder.defaultValue;
		this.parent = builder.parent;
		this.restartRequired = builder.restartRequired;
		this.separator = builder.separator;
	}

}
