package it.albertus.jface.preference;

import it.albertus.util.Localized;

import java.util.Arrays;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.graphics.Image;

public class FieldEditorDetails {

	// Mandatory
	private Class<? extends FieldEditor> fieldEditorClass;

	// Generic
	private Boolean emptyStringAllowed;
	private Integer horizontalSpan;
	private Image[] icons;

	// ComboFieldEditor & RadioGroupFieldEditor
	private LabelsAndValues labelsAndValues;

	// RadioGroupFieldEditor
	private Boolean radioUseGroup;
	private Integer radioNumColumns;

	// ScaleFieldEditor
	private Integer scaleMinimum;
	private Integer scaleMaximum;
	private Integer scaleIncrement;
	private Integer scalePageIncrement;

	// StringFieldEditor
	private Integer textLimit;
	private Integer textWidth;
	private Integer textHeight;
	private Integer textValidateStrategy;

	// IntegerFieldEditor
	private Integer integerMinValidValue;
	private Integer integerMaxValidValue;

	// DirectoryFieldEditor & PathEditor
	private Localized directoryDialogMessage;

	// FileFieldEditor
	private String[] fileExtensions;
	private Boolean fileEnforceAbsolute;

	// FontFieldEditor
	private Localized fontPreviewAreaText;
	private Localized fontChangeButtonText;

	public Class<? extends FieldEditor> getFieldEditorClass() {
		return fieldEditorClass;
	}

	public void setFieldEditorClass(Class<? extends FieldEditor> fieldEditorClass) {
		this.fieldEditorClass = fieldEditorClass;
	}

	public Boolean getEmptyStringAllowed() {
		return emptyStringAllowed;
	}

	public void setEmptyStringAllowed(Boolean emptyStringAllowed) {
		this.emptyStringAllowed = emptyStringAllowed;
	}

	public Integer getHorizontalSpan() {
		return horizontalSpan;
	}

	public void setHorizontalSpan(Integer horizontalSpan) {
		this.horizontalSpan = horizontalSpan;
	}

	public Image[] getIcons() {
		return icons;
	}

	public void setIcons(Image[] icons) {
		this.icons = icons;
	}

	public LabelsAndValues getLabelsAndValues() {
		return labelsAndValues;
	}

	public void setLabelsAndValues(LabelsAndValues labelsAndValues) {
		this.labelsAndValues = labelsAndValues;
	}

	public Boolean getRadioUseGroup() {
		return radioUseGroup;
	}

	public void setRadioUseGroup(Boolean radioUseGroup) {
		this.radioUseGroup = radioUseGroup;
	}

	public Integer getRadioNumColumns() {
		return radioNumColumns;
	}

	public void setRadioNumColumns(Integer radioNumColumns) {
		this.radioNumColumns = radioNumColumns;
	}

	public Integer getScaleMinimum() {
		return scaleMinimum;
	}

	public void setScaleMinimum(Integer scaleMinimum) {
		this.scaleMinimum = scaleMinimum;
	}

	public Integer getScaleMaximum() {
		return scaleMaximum;
	}

	public void setScaleMaximum(Integer scaleMaximum) {
		this.scaleMaximum = scaleMaximum;
	}

	public Integer getScaleIncrement() {
		return scaleIncrement;
	}

	public void setScaleIncrement(Integer scaleIncrement) {
		this.scaleIncrement = scaleIncrement;
	}

	public Integer getScalePageIncrement() {
		return scalePageIncrement;
	}

	public void setScalePageIncrement(Integer scalePageIncrement) {
		this.scalePageIncrement = scalePageIncrement;
	}

	public Integer getTextLimit() {
		return textLimit;
	}

	public void setTextLimit(Integer textLimit) {
		this.textLimit = textLimit;
	}

	public Integer getTextWidth() {
		return textWidth;
	}

	public void setTextWidth(Integer textWidth) {
		this.textWidth = textWidth;
	}

	public Integer getTextHeight() {
		return textHeight;
	}

	public void setTextHeight(Integer textHeight) {
		this.textHeight = textHeight;
	}

	public Integer getTextValidateStrategy() {
		return textValidateStrategy;
	}

	public void setTextValidateStrategy(Integer textValidateStrategy) {
		this.textValidateStrategy = textValidateStrategy;
	}

	public Integer getIntegerMinValidValue() {
		return integerMinValidValue;
	}

	public void setIntegerMinValidValue(Integer integerMinValidValue) {
		this.integerMinValidValue = integerMinValidValue;
	}

	public Integer getIntegerMaxValidValue() {
		return integerMaxValidValue;
	}

	public void setIntegerMaxValidValue(Integer integerMaxValidValue) {
		this.integerMaxValidValue = integerMaxValidValue;
	}

	public Localized getDirectoryDialogMessage() {
		return directoryDialogMessage;
	}

	public void setDirectoryDialogMessage(Localized directoryDialogMessage) {
		this.directoryDialogMessage = directoryDialogMessage;
	}

	public String[] getFileExtensions() {
		return fileExtensions;
	}

	public void setFileExtensions(String[] fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public Boolean getFileEnforceAbsolute() {
		return fileEnforceAbsolute;
	}

	public void setFileEnforceAbsolute(Boolean fileEnforceAbsolute) {
		this.fileEnforceAbsolute = fileEnforceAbsolute;
	}

	public Localized getFontPreviewAreaText() {
		return fontPreviewAreaText;
	}

	public void setFontPreviewAreaText(Localized fontPreviewAreaText) {
		this.fontPreviewAreaText = fontPreviewAreaText;
	}

	public Localized getFontChangeButtonText() {
		return fontChangeButtonText;
	}

	public void setFontChangeButtonText(Localized fontChangeButtonText) {
		this.fontChangeButtonText = fontChangeButtonText;
	}

	@Override
	public String toString() {
		return "FieldEditorDetails [" + (fieldEditorClass != null ? "fieldEditorClass=" + fieldEditorClass + ", " : "") + (emptyStringAllowed != null ? "emptyStringAllowed=" + emptyStringAllowed + ", " : "") + (horizontalSpan != null ? "horizontalSpan=" + horizontalSpan + ", " : "") + (icons != null ? "icons=" + Arrays.toString(icons) + ", " : "") + (labelsAndValues != null ? "labelsAndValues=" + labelsAndValues + ", " : "") + (radioUseGroup != null ? "radioUseGroup=" + radioUseGroup + ", " : "")
				+ (radioNumColumns != null ? "radioNumColumns=" + radioNumColumns + ", " : "") + (scaleMinimum != null ? "scaleMinimum=" + scaleMinimum + ", " : "") + (scaleMaximum != null ? "scaleMaximum=" + scaleMaximum + ", " : "") + (scaleIncrement != null ? "scaleIncrement=" + scaleIncrement + ", " : "") + (scalePageIncrement != null ? "scalePageIncrement=" + scalePageIncrement + ", " : "") + (textLimit != null ? "textLimit=" + textLimit + ", " : "")
				+ (textWidth != null ? "textWidth=" + textWidth + ", " : "") + (textHeight != null ? "textHeight=" + textHeight + ", " : "") + (textValidateStrategy != null ? "textValidateStrategy=" + textValidateStrategy + ", " : "") + (integerMinValidValue != null ? "integerMinValidValue=" + integerMinValidValue + ", " : "") + (integerMaxValidValue != null ? "integerMaxValidValue=" + integerMaxValidValue + ", " : "")
				+ (directoryDialogMessage != null ? "directoryDialogMessage=" + directoryDialogMessage + ", " : "") + (fileExtensions != null ? "fileExtensions=" + Arrays.toString(fileExtensions) + ", " : "") + (fileEnforceAbsolute != null ? "fileEnforceAbsolute=" + fileEnforceAbsolute + ", " : "") + (fontPreviewAreaText != null ? "fontPreviewAreaText=" + fontPreviewAreaText + ", " : "") + (fontChangeButtonText != null ? "fontChangeButtonText=" + fontChangeButtonText : "") + "]";
	}

	public static class FieldEditorDetailsBuilder {
		private final Class<? extends FieldEditor> fieldEditorClass;
		private Boolean emptyStringAllowed;
		private Integer horizontalSpan;
		private Image[] icons;
		private LabelsAndValues labelsAndValues;
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
		private Localized fontPreviewAreaText;
		private Localized fontChangeButtonText;

		public FieldEditorDetailsBuilder(final Class<? extends FieldEditor> fieldEditorClass) {
			this.fieldEditorClass = fieldEditorClass;
		}

		public FieldEditorDetailsBuilder emptyStringAllowed(final boolean emptyStringAllowed) {
			this.emptyStringAllowed = emptyStringAllowed;
			return this;
		}

		public FieldEditorDetailsBuilder horizontalSpan(final int horizontalSpan) {
			this.horizontalSpan = horizontalSpan;
			return this;
		}

		public FieldEditorDetailsBuilder icons(final Image[] icons) {
			this.icons = icons;
			return this;
		}

		public FieldEditorDetailsBuilder labelsAndValues(final LabelsAndValues labelsAndValues) {
			this.labelsAndValues = labelsAndValues;
			return this;
		}

		public FieldEditorDetailsBuilder scaleMinimum(final int min) {
			this.scaleMinimum = min;
			return this;
		}

		public FieldEditorDetailsBuilder scaleMaximum(final int max) {
			this.scaleMaximum = max;
			return this;
		}

		public FieldEditorDetailsBuilder scaleIncrement(final int increment) {
			this.scaleIncrement = increment;
			return this;
		}

		public FieldEditorDetailsBuilder scalePageIncrement(final int pageIncrement) {
			this.scalePageIncrement = pageIncrement;
			return this;
		}

		public FieldEditorDetailsBuilder textLimit(final int limit) {
			this.textLimit = limit;
			return this;
		}

		public FieldEditorDetailsBuilder textWidth(final int width) {
			this.textWidth = width;
			return this;
		}

		public FieldEditorDetailsBuilder textHeight(final int height) {
			this.textHeight = height;
			return this;
		}

		public FieldEditorDetailsBuilder textValidateStrategy(final int validateStrategy) {
			this.textValidateStrategy = validateStrategy;
			return this;
		}

		public FieldEditorDetailsBuilder integerValidRange(final int min, final int max) {
			this.integerMinValidValue = min;
			this.integerMaxValidValue = max;
			return this;
		}

		public FieldEditorDetailsBuilder directoryDialogMessage(final Localized dialogMessage) {
			this.directoryDialogMessage = dialogMessage;
			return this;
		}

		public FieldEditorDetailsBuilder fileExtensions(final String[] fileExtensions) {
			this.fileExtensions = fileExtensions;
			return this;
		}

		public FieldEditorDetailsBuilder fileEnforceAbsolute(final boolean fileEnforceAbsolute) {
			this.fileEnforceAbsolute = fileEnforceAbsolute;
			return this;
		}

		public FieldEditorDetailsBuilder radioUseGroup(final boolean radioUseGroup) {
			this.radioUseGroup = radioUseGroup;
			return this;
		}

		public FieldEditorDetailsBuilder radioNumColumns(final int radioNumColumns) {
			this.radioNumColumns = radioNumColumns;
			return this;
		}

		public FieldEditorDetailsBuilder fontPreviewAreaText(final Localized fontPreviewAreaText) {
			this.fontPreviewAreaText = fontPreviewAreaText;
			return this;
		}

		public FieldEditorDetailsBuilder fontChangeButtonText(final Localized fontChangeButtonText) {
			this.fontChangeButtonText = fontChangeButtonText;
			return this;
		}

		public FieldEditorDetails build() {
			return new FieldEditorDetails(this);
		}
	}

	private FieldEditorDetails(final FieldEditorDetailsBuilder builder) {
		this.fieldEditorClass = builder.fieldEditorClass;
		this.emptyStringAllowed = builder.emptyStringAllowed;
		this.horizontalSpan = builder.horizontalSpan;
		this.icons = builder.icons;
		this.labelsAndValues = builder.labelsAndValues;
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
		this.fontPreviewAreaText = builder.fontPreviewAreaText;
		this.fontChangeButtonText = builder.fontChangeButtonText;
	}

}
