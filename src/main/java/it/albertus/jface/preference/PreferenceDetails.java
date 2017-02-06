package it.albertus.jface.preference;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.FontData;

import it.albertus.jface.preference.page.IPageDefinition;
import it.albertus.util.Localized;

public class PreferenceDetails {

	// Mandatory
	private IPageDefinition pageDefinition;

	private String name;
	private Localized label;
	private String defaultValue;
	private IPreference parent;
	private boolean restartRequired;
	private boolean separate;

	// Allow extension
	protected PreferenceDetails() {}

	private PreferenceDetails(final PreferenceDetailsBuilder builder) {
		this.pageDefinition = builder.pageDefinition;
		this.name = builder.name;
		this.label = builder.label;
		this.defaultValue = builder.defaultValue;
		this.parent = builder.parent;
		this.restartRequired = builder.restartRequired;
		this.separate = builder.separate;
	}

	public IPageDefinition getPageDefinition() {
		return pageDefinition;
	}

	public void setPageDefinition(final IPageDefinition pageDefinition) {
		this.pageDefinition = pageDefinition;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Localized getLabel() {
		return label;
	}

	public void setLabel(final Localized label) {
		this.label = label;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public IPreference getParent() {
		return parent;
	}

	public void setParent(final IPreference parent) {
		this.parent = parent;
	}

	public boolean isRestartRequired() {
		return restartRequired;
	}

	public void setRestartRequired(final boolean restartRequired) {
		this.restartRequired = restartRequired;
	}

	public boolean isSeparate() {
		return separate;
	}

	public void setSeparate(final boolean separate) {
		this.separate = separate;
	}

	@Override
	public String toString() {
		return "PreferenceDetails [" + (pageDefinition != null ? "pageDefinition=" + pageDefinition + ", " : "") + (name != null ? "name=" + name + ", " : "") + (label != null ? "label=" + label + ", " : "") + (defaultValue != null ? "defaultValue=" + defaultValue + ", " : "") + (parent != null ? "parent=" + parent + ", " : "") + "restartRequired=" + restartRequired + ", separate=" + separate + "]";
	}

	public static class PreferenceDetailsBuilder {
		private final IPageDefinition pageDefinition;
		private String name;
		private Localized label;
		private String defaultValue;
		private IPreference parent;
		private boolean restartRequired;
		private boolean separate;

		public PreferenceDetailsBuilder(final IPageDefinition pageDefinition) {
			this.pageDefinition = pageDefinition;
		}

		public PreferenceDetailsBuilder name(final String name) {
			this.name = name;
			return this;
		}

		public PreferenceDetailsBuilder label(final Localized label) {
			this.label = label;
			return this;
		}

		public PreferenceDetailsBuilder label(final String label) {
			this.label = new Localized() {
				@Override
				public String getString() {
					return label;
				}
			};
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final String defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final boolean defaultValue) {
			this.defaultValue = Boolean.toString(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final byte defaultValue) {
			this.defaultValue = Byte.toString(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final short defaultValue) {
			this.defaultValue = Short.toString(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final char defaultValue) {
			this.defaultValue = Character.toString(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final int defaultValue) {
			this.defaultValue = Integer.toString(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final long defaultValue) {
			this.defaultValue = Long.toString(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final float defaultValue) {
			this.defaultValue = Float.toString(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final double defaultValue) {
			this.defaultValue = Double.toString(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final FontData[] defaultValue) {
			this.defaultValue = PreferenceConverter.getStoredRepresentation(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder defaultValue(final Object defaultValue) {
			this.defaultValue = String.valueOf(defaultValue);
			return this;
		}

		public PreferenceDetailsBuilder parent(final IPreference parent) {
			this.parent = parent;
			return this;
		}

		private PreferenceDetailsBuilder restartRequired(final boolean restartRequired) {
			this.restartRequired = restartRequired;
			return this;
		}

		public PreferenceDetailsBuilder restartRequired() {
			return restartRequired(true);
		}

		private PreferenceDetailsBuilder separate(final boolean separate) {
			this.separate = separate;
			return this;
		}

		public PreferenceDetailsBuilder separate() {
			return separate(true);
		}

		public PreferenceDetails build() {
			return new PreferenceDetails(this);
		}
	}

}
