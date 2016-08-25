package it.albertus.jface.preference;

import it.albertus.util.Localized;

import java.util.Arrays;

import org.eclipse.swt.graphics.Image;

public class FieldEditorData {

	// Generic
	private final Boolean emptyStringAllowed;
	private final Integer horizontalSpan;
	private final Image[] icons;

	// ComboFieldEditor & RadioGroupFieldEditor
	private final NamesAndValues namesAndValues;

	// RadioGroupFieldEditor
	private final Boolean radioUseGroup;
	private final Integer radioNumColumns;

	// ScaleFieldEditor
	private final Integer scaleMinimum;
	private final Integer scaleMaximum;
	private final Integer scaleIncrement;
	private final Integer scalePageIncrement;

	// StringFieldEditor
	private final Integer textLimit;
	private final Integer textWidth;
	private final Integer textHeight;
	private final Integer textValidateStrategy;

	// IntegerFieldEditor
	private final Integer integerMinValidValue;
	private final Integer integerMaxValidValue;

	// DirectoryFieldEditor
	private final Localized directoryDialogMessage;

	// FileFieldEditor
	private final String[] fileExtensions;
	private final Boolean fileEnforceAbsolute;

	public Boolean getEmptyStringAllowed() {
		return emptyStringAllowed;
	}

	public Integer getHorizontalSpan() {
		return horizontalSpan;
	}

	public NamesAndValues getNamesAndValues() {
		return namesAndValues;
	}

	public Integer getScaleMinimum() {
		return scaleMinimum;
	}

	public Integer getScaleMaximum() {
		return scaleMaximum;
	}

	public Integer getScaleIncrement() {
		return scaleIncrement;
	}

	public Integer getScalePageIncrement() {
		return scalePageIncrement;
	}

	public Integer getTextLimit() {
		return textLimit;
	}

	public Integer getTextWidth() {
		return textWidth;
	}

	public Integer getTextHeight() {
		return textHeight;
	}

	public Integer getTextValidateStrategy() {
		return textValidateStrategy;
	}

	public Integer getIntegerMinValidValue() {
		return integerMinValidValue;
	}

	public Integer getIntegerMaxValidValue() {
		return integerMaxValidValue;
	}

	public Localized getDirectoryDialogMessage() {
		return directoryDialogMessage;
	}

	public String[] getFileExtensions() {
		return fileExtensions;
	}

	public Boolean getFileEnforceAbsolute() {
		return fileEnforceAbsolute;
	}

	public Boolean getRadioUseGroup() {
		return radioUseGroup;
	}

	public Integer getRadioNumColumns() {
		return radioNumColumns;
	}

	public Image[] getIcons() {
		return icons;
	}

	@Override
	public String toString() {
		return "FieldEditorData [emptyStringAllowed=" + emptyStringAllowed + ", horizontalSpan=" + horizontalSpan + ", icons=" + Arrays.toString(icons) + ", namesAndValues=" + namesAndValues + ", radioUseGroup=" + radioUseGroup + ", radioNumColumns=" + radioNumColumns + ", scaleMinimum=" + scaleMinimum + ", scaleMaximum=" + scaleMaximum + ", scaleIncrement=" + scaleIncrement + ", scalePageIncrement=" + scalePageIncrement + ", textLimit=" + textLimit + ", textWidth=" + textWidth + ", textHeight="
				+ textHeight + ", textValidateStrategy=" + textValidateStrategy + ", integerMinValidValue=" + integerMinValidValue + ", integerMaxValidValue=" + integerMaxValidValue + ", directoryDialogMessage=" + directoryDialogMessage + ", fileExtensions=" + Arrays.toString(fileExtensions) + ", fileEnforceAbsolute=" + fileEnforceAbsolute + "]";
	}

	public static class FieldEditorDataBuilder {
		private Boolean emptyStringAllowed;
		private Integer horizontalSpan;
		private Image[] icons;
		private NamesAndValues namesAndValues;
		private Boolean radioUseGroup;
		private Integer radioNumColumns;
		private Integer scaleMinimum;
		private Integer scaleMaximum;
		private Integer scaleIncrement;
		private Integer scalePageIncrement;
		private Integer textLimit;
		private Integer textWidth;
		private Integer textHeight;
		private Integer textValidateStrategy;
		private Integer integerMinValidValue;
		private Integer integerMaxValidValue;
		private Localized directoryDialogMessage;
		private String[] fileExtensions;
		private Boolean fileEnforceAbsolute;

		public FieldEditorDataBuilder emptyStringAllowed(final boolean emptyStringAllowed) {
			this.emptyStringAllowed = emptyStringAllowed;
			return this;
		}

		public FieldEditorDataBuilder horizontalSpan(final int horizontalSpan) {
			this.horizontalSpan = horizontalSpan;
			return this;
		}

		public FieldEditorDataBuilder icons(final Image[] icons) {
			this.icons = icons;
			return this;
		}

		public FieldEditorDataBuilder namesAndValues(final NamesAndValues namesAndValues) {
			this.namesAndValues = namesAndValues;
			return this;
		}

		public FieldEditorDataBuilder scaleMinimum(final int min) {
			this.scaleMinimum = min;
			return this;
		}

		public FieldEditorDataBuilder scaleMaximum(final int max) {
			this.scaleMaximum = max;
			return this;
		}

		public FieldEditorDataBuilder scaleIncrement(final int increment) {
			this.scaleIncrement = increment;
			return this;
		}

		public FieldEditorDataBuilder scalePageIncrement(final int pageIncrement) {
			this.scalePageIncrement = pageIncrement;
			return this;
		}

		public FieldEditorDataBuilder textLimit(final int limit) {
			this.textLimit = limit;
			return this;
		}

		public FieldEditorDataBuilder textWidth(final int width) {
			this.textWidth = width;
			return this;
		}

		public FieldEditorDataBuilder textHeight(final int height) {
			this.textHeight = height;
			return this;
		}

		public FieldEditorDataBuilder textValidateStrategy(final int validateStrategy) {
			this.textValidateStrategy = validateStrategy;
			return this;
		}

		public FieldEditorDataBuilder integerValidRange(final int min, final int max) {
			this.integerMinValidValue = min;
			this.integerMaxValidValue = max;
			return this;
		}

		public FieldEditorDataBuilder directoryDialogMessage(final Localized dialogMessage) {
			this.directoryDialogMessage = dialogMessage;
			return this;
		}

		public FieldEditorDataBuilder fileExtensions(final String[] fileExtensions) {
			this.fileExtensions = fileExtensions;
			return this;
		}

		public FieldEditorDataBuilder fileEnforceAbsolute(final boolean fileEnforceAbsolute) {
			this.fileEnforceAbsolute = fileEnforceAbsolute;
			return this;
		}

		public FieldEditorDataBuilder radioUseGroup(final boolean radioUseGroup) {
			this.radioUseGroup = radioUseGroup;
			return this;
		}

		public FieldEditorDataBuilder radioNumColumns(final int radioNumColumns) {
			this.radioNumColumns = radioNumColumns;
			return this;
		}

		public FieldEditorData build() {
			return new FieldEditorData(this);
		}
	}

	private FieldEditorData(final FieldEditorDataBuilder builder) {
		this.emptyStringAllowed = builder.emptyStringAllowed;
		this.horizontalSpan = builder.horizontalSpan;
		this.icons = builder.icons;
		this.namesAndValues = builder.namesAndValues;
		this.scaleMinimum = builder.scaleMinimum;
		this.scaleMaximum = builder.scaleMaximum;
		this.scaleIncrement = builder.scaleIncrement;
		this.scalePageIncrement = builder.scalePageIncrement;
		this.textLimit = builder.textLimit;
		this.textWidth = builder.textWidth;
		this.textHeight = builder.textHeight;
		this.textValidateStrategy = builder.textValidateStrategy;
		this.integerMinValidValue = builder.integerMinValidValue;
		this.integerMaxValidValue = builder.integerMaxValidValue;
		this.directoryDialogMessage = builder.directoryDialogMessage;
		this.fileExtensions = builder.fileExtensions;
		this.fileEnforceAbsolute = builder.fileEnforceAbsolute;
		this.radioNumColumns = builder.radioNumColumns;
		this.radioUseGroup = builder.radioUseGroup;
	}

}
