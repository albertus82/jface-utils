package it.albertus.jface.preference;

import java.util.Arrays;
import java.util.Date;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.graphics.Image;

import it.albertus.util.Localized;

public class FieldEditorDetails {

	// Mandatory
	private Class<? extends FieldEditor> fieldEditorClass;

	// Generic
	private Boolean emptyStringAllowed;
	private Integer horizontalSpan;
	private Image[] icons;
	private Boolean defaultToolTip;
	private Boolean boldCustomValues;
	private Integer style;

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

	// Numeric FieldEditors
	private Number numberMinimum;
	private Number numberMaximum;

	// DateFieldEditor
	private String datePattern;
	private Date dateFrom;
	private Date dateTo;

	// DirectoryFieldEditor & PathEditor
	private Localized directoryDialogMessage;

	// FileFieldEditor
	private String[] fileExtensions;
	private Boolean fileEnforceAbsolute;

	// FontFieldEditor
	private Localized fontPreviewAreaText;
	private Localized fontChangeButtonText;

	// Allow extension
	protected FieldEditorDetails() {}

	private FieldEditorDetails(final FieldEditorDetailsBuilder builder) {
		this.fieldEditorClass = builder.fieldEditorClass;
		this.emptyStringAllowed = builder.emptyStringAllowed;
		this.horizontalSpan = builder.horizontalSpan;
		this.icons = builder.icons;
		this.style = builder.style;
		this.labelsAndValues = builder.labelsAndValues;
		this.scaleMinimum = builder.scaleMinimum;
		this.scaleMaximum = builder.scaleMaximum;
		this.scaleIncrement = builder.scaleIncrement;
		this.scalePageIncrement = builder.scalePageIncrement;
		this.textLimit = builder.textLimit;
		this.textWidth = builder.textWidth;
		this.textHeight = builder.textHeight;
		this.textValidateStrategy = builder.textValidateStrategy;
		this.numberMinimum = builder.numberMinimum;
		this.numberMaximum = builder.numberMaximum;
		this.datePattern = builder.datePattern;
		this.dateFrom = builder.dateFrom;
		this.dateTo = builder.dateTo;
		this.directoryDialogMessage = builder.directoryDialogMessage;
		this.fileExtensions = builder.fileExtensions;
		this.fileEnforceAbsolute = builder.fileEnforceAbsolute;
		this.radioNumColumns = builder.radioNumColumns;
		this.radioUseGroup = builder.radioUseGroup;
		this.fontPreviewAreaText = builder.fontPreviewAreaText;
		this.fontChangeButtonText = builder.fontChangeButtonText;
		this.boldCustomValues = builder.boldCustomValues;
		this.defaultToolTip = builder.defaultToolTip;
	}

	public Class<? extends FieldEditor> getFieldEditorClass() {
		return fieldEditorClass;
	}

	public void setFieldEditorClass(final Class<? extends FieldEditor> fieldEditorClass) {
		this.fieldEditorClass = fieldEditorClass;
	}

	public Boolean getEmptyStringAllowed() {
		return emptyStringAllowed;
	}

	public void setEmptyStringAllowed(final Boolean emptyStringAllowed) {
		this.emptyStringAllowed = emptyStringAllowed;
	}

	public Integer getHorizontalSpan() {
		return horizontalSpan;
	}

	public void setHorizontalSpan(final Integer horizontalSpan) {
		this.horizontalSpan = horizontalSpan;
	}

	public Image[] getIcons() {
		return icons;
	}

	public void setIcons(final Image[] icons) {
		this.icons = icons;
	}

	public LabelsAndValues getLabelsAndValues() {
		return labelsAndValues;
	}

	public void setLabelsAndValues(final LabelsAndValues labelsAndValues) {
		this.labelsAndValues = labelsAndValues;
	}

	public Boolean getRadioUseGroup() {
		return radioUseGroup;
	}

	public void setRadioUseGroup(final Boolean radioUseGroup) {
		this.radioUseGroup = radioUseGroup;
	}

	public Integer getRadioNumColumns() {
		return radioNumColumns;
	}

	public void setRadioNumColumns(final Integer radioNumColumns) {
		this.radioNumColumns = radioNumColumns;
	}

	public Integer getScaleMinimum() {
		return scaleMinimum;
	}

	public void setScaleMinimum(final Integer scaleMinimum) {
		this.scaleMinimum = scaleMinimum;
	}

	public Integer getScaleMaximum() {
		return scaleMaximum;
	}

	public void setScaleMaximum(final Integer scaleMaximum) {
		this.scaleMaximum = scaleMaximum;
	}

	public Integer getScaleIncrement() {
		return scaleIncrement;
	}

	public void setScaleIncrement(final Integer scaleIncrement) {
		this.scaleIncrement = scaleIncrement;
	}

	public Integer getScalePageIncrement() {
		return scalePageIncrement;
	}

	public void setScalePageIncrement(final Integer scalePageIncrement) {
		this.scalePageIncrement = scalePageIncrement;
	}

	public Integer getTextLimit() {
		return textLimit;
	}

	public void setTextLimit(final Integer textLimit) {
		this.textLimit = textLimit;
	}

	public Integer getTextWidth() {
		return textWidth;
	}

	public void setTextWidth(final Integer textWidth) {
		this.textWidth = textWidth;
	}

	public Integer getTextHeight() {
		return textHeight;
	}

	public void setTextHeight(final Integer textHeight) {
		this.textHeight = textHeight;
	}

	public Integer getTextValidateStrategy() {
		return textValidateStrategy;
	}

	public void setTextValidateStrategy(final Integer textValidateStrategy) {
		this.textValidateStrategy = textValidateStrategy;
	}

	public Number getNumberMinimum() {
		return numberMinimum;
	}

	public void setNumberMinimum(final Number numberMinimum) {
		this.numberMinimum = numberMinimum;
	}

	public Number getNumberMaximum() {
		return numberMaximum;
	}

	public void setNumberMaximum(final Number numberMaximum) {
		this.numberMaximum = numberMaximum;
	}

	public Localized getDirectoryDialogMessage() {
		return directoryDialogMessage;
	}

	public void setDirectoryDialogMessage(final Localized directoryDialogMessage) {
		this.directoryDialogMessage = directoryDialogMessage;
	}

	public String[] getFileExtensions() {
		return fileExtensions;
	}

	public void setFileExtensions(final String[] fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public Boolean getFileEnforceAbsolute() {
		return fileEnforceAbsolute;
	}

	public void setFileEnforceAbsolute(final Boolean fileEnforceAbsolute) {
		this.fileEnforceAbsolute = fileEnforceAbsolute;
	}

	public Localized getFontPreviewAreaText() {
		return fontPreviewAreaText;
	}

	public void setFontPreviewAreaText(final Localized fontPreviewAreaText) {
		this.fontPreviewAreaText = fontPreviewAreaText;
	}

	public Localized getFontChangeButtonText() {
		return fontChangeButtonText;
	}

	public void setFontChangeButtonText(final Localized fontChangeButtonText) {
		this.fontChangeButtonText = fontChangeButtonText;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(final String datePattern) {
		this.datePattern = datePattern;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(final Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(final Date dateTo) {
		this.dateTo = dateTo;
	}

	public Boolean getDefaultToolTip() {
		return defaultToolTip;
	}

	public void setDefaultToolTip(final Boolean defaultToolTip) {
		this.defaultToolTip = defaultToolTip;
	}

	public Boolean getBoldCustomValues() {
		return boldCustomValues;
	}

	public void setBoldCustomValues(final Boolean boldCustomValues) {
		this.boldCustomValues = boldCustomValues;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(final Integer style) {
		this.style = style;
	}

	@Override
	public String toString() {
		return "FieldEditorDetails [" + (fieldEditorClass != null ? "fieldEditorClass=" + fieldEditorClass + ", " : "") + (emptyStringAllowed != null ? "emptyStringAllowed=" + emptyStringAllowed + ", " : "") + (horizontalSpan != null ? "horizontalSpan=" + horizontalSpan + ", " : "") + (icons != null ? "icons=" + Arrays.toString(icons) + ", " : "") + (defaultToolTip != null ? "defaultToolTip=" + defaultToolTip + ", " : "") + (boldCustomValues != null ? "boldCustomValues=" + boldCustomValues + ", " : "")
				+ (style != null ? "style=" + style + ", " : "") + (labelsAndValues != null ? "labelsAndValues=" + labelsAndValues + ", " : "") + (radioUseGroup != null ? "radioUseGroup=" + radioUseGroup + ", " : "") + (radioNumColumns != null ? "radioNumColumns=" + radioNumColumns + ", " : "") + (scaleMinimum != null ? "scaleMinimum=" + scaleMinimum + ", " : "") + (scaleMaximum != null ? "scaleMaximum=" + scaleMaximum + ", " : "")
				+ (scaleIncrement != null ? "scaleIncrement=" + scaleIncrement + ", " : "") + (scalePageIncrement != null ? "scalePageIncrement=" + scalePageIncrement + ", " : "") + (textLimit != null ? "textLimit=" + textLimit + ", " : "") + (textWidth != null ? "textWidth=" + textWidth + ", " : "") + (textHeight != null ? "textHeight=" + textHeight + ", " : "") + (textValidateStrategy != null ? "textValidateStrategy=" + textValidateStrategy + ", " : "")
				+ (numberMinimum != null ? "numberMinimum=" + numberMinimum + ", " : "") + (numberMaximum != null ? "numberMaximum=" + numberMaximum + ", " : "") + (datePattern != null ? "datePattern=" + datePattern + ", " : "") + (dateFrom != null ? "dateFrom=" + dateFrom + ", " : "") + (dateTo != null ? "dateTo=" + dateTo + ", " : "") + (directoryDialogMessage != null ? "directoryDialogMessage=" + directoryDialogMessage + ", " : "")
				+ (fileExtensions != null ? "fileExtensions=" + Arrays.toString(fileExtensions) + ", " : "") + (fileEnforceAbsolute != null ? "fileEnforceAbsolute=" + fileEnforceAbsolute + ", " : "") + (fontPreviewAreaText != null ? "fontPreviewAreaText=" + fontPreviewAreaText + ", " : "") + (fontChangeButtonText != null ? "fontChangeButtonText=" + fontChangeButtonText : "") + "]";
	}

	public static class FieldEditorDetailsBuilder {
		private final Class<? extends FieldEditor> fieldEditorClass;
		private Boolean emptyStringAllowed;
		private Integer horizontalSpan;
		private Image[] icons;
		private Integer style;
		private Boolean defaultToolTip;
		private Boolean boldCustomValues;
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
		private Number numberMinimum;
		private Number numberMaximum;
		private String datePattern;
		private Date dateFrom;
		private Date dateTo;
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

		public FieldEditorDetailsBuilder numberValidRange(final Number min, final Number max) {
			this.numberMinimum = min;
			this.numberMaximum = max;
			return this;
		}

		public FieldEditorDetailsBuilder numberMinimum(final Number min) {
			this.numberMinimum = min;
			return this;
		}

		public FieldEditorDetailsBuilder numberMaximum(final Number max) {
			this.numberMaximum = max;
			return this;
		}

		public FieldEditorDetailsBuilder datePattern(final String datePattern) {
			this.datePattern = datePattern;
			return this;
		}

		public FieldEditorDetailsBuilder dateValidRange(final Date from, final Date to) {
			this.dateFrom = from;
			this.dateTo = to;
			return this;
		}

		public FieldEditorDetailsBuilder dateFrom(final Date from) {
			this.dateFrom = from;
			return this;
		}

		public FieldEditorDetailsBuilder dateTo(final Date to) {
			this.dateTo = to;
			return this;
		}

		public FieldEditorDetailsBuilder directoryDialogMessage(final Localized dialogMessage) {
			this.directoryDialogMessage = dialogMessage;
			return this;
		}

		public FieldEditorDetailsBuilder directoryDialogMessage(final String dialogMessage) {
			this.directoryDialogMessage = new Localized() {
				@Override
				public String getString() {
					return dialogMessage;
				}
			};
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

		public FieldEditorDetailsBuilder fontPreviewAreaText(final String fontPreviewAreaText) {
			this.fontPreviewAreaText = new Localized() {
				@Override
				public String getString() {
					return fontPreviewAreaText;
				}
			};
			return this;
		}

		public FieldEditorDetailsBuilder fontChangeButtonText(final Localized fontChangeButtonText) {
			this.fontChangeButtonText = fontChangeButtonText;
			return this;
		}

		public FieldEditorDetailsBuilder fontChangeButtonText(final String fontChangeButtonText) {
			this.fontChangeButtonText = new Localized() {
				@Override
				public String getString() {
					return fontChangeButtonText;
				}
			};
			return this;
		}

		public FieldEditorDetailsBuilder defaultToolTip(final boolean defaultToolTip) {
			this.defaultToolTip = defaultToolTip;
			return this;
		}

		public FieldEditorDetailsBuilder boldCustomValues(final boolean boldCustomValues) {
			this.boldCustomValues = boldCustomValues;
			return this;
		}

		public FieldEditorDetailsBuilder style(final int style) {
			this.style = style;
			return this;
		}

		public FieldEditorDetails build() {
			return new FieldEditorDetails(this);
		}
	}

}
