package it.albertus.jface.preference;

import java.util.Arrays;
import java.util.Date;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.graphics.Image;

import it.albertus.util.ISupplier;
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
	private Integer height;

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
	private Integer textValidateStrategy;

	// PasswordFieldEditor
	private String hashAlgorithm;

	// Numeric FieldEditors
	private Number numberMinimum;
	private Number numberMaximum;

	// DateFieldEditor
	private String datePattern;
	private Date dateFrom;
	private Date dateTo;

	// DirectoryFieldEditor & PathEditor
	private ISupplier<String> directoryDialogMessage;
	private Boolean directoryMustExist;

	// FileFieldEditor
	private String[] fileExtensions;
	private Boolean fileEnforceAbsolute;

	// FontFieldEditor
	private ISupplier<String> fontPreviewAreaText;
	private ISupplier<String> fontChangeButtonText;

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
		this.textValidateStrategy = builder.textValidateStrategy;
		this.hashAlgorithm = builder.hashAlgorithm;
		this.height = builder.height;
		this.numberMinimum = builder.numberMinimum;
		this.numberMaximum = builder.numberMaximum;
		this.datePattern = builder.datePattern;
		this.dateFrom = builder.dateFrom;
		this.dateTo = builder.dateTo;
		this.directoryDialogMessage = builder.directoryDialogMessage;
		this.directoryMustExist = builder.directoryMustExist;
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

	public Integer getHeight() {
		return height;
	}

	public void setHeight(final Integer height) {
		this.height = height;
	}

	public Integer getTextValidateStrategy() {
		return textValidateStrategy;
	}

	public void setTextValidateStrategy(final Integer textValidateStrategy) {
		this.textValidateStrategy = textValidateStrategy;
	}

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(final String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
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

	public ISupplier<String> getDirectoryDialogMessage() {
		return directoryDialogMessage;
	}

	public void setDirectoryDialogMessage(final ISupplier<String> directoryDialogMessage) {
		this.directoryDialogMessage = directoryDialogMessage;
	}

	public Boolean getDirectoryMustExist() {
		return directoryMustExist;
	}

	public void setDirectoryMustExist(final Boolean directoryMustExist) {
		this.directoryMustExist = directoryMustExist;
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

	public ISupplier<String> getFontPreviewAreaText() {
		return fontPreviewAreaText;
	}

	public void setFontPreviewAreaText(final ISupplier<String> fontPreviewAreaText) {
		this.fontPreviewAreaText = fontPreviewAreaText;
	}

	public ISupplier<String> getFontChangeButtonText() {
		return fontChangeButtonText;
	}

	public void setFontChangeButtonText(final ISupplier<String> fontChangeButtonText) {
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
		final StringBuilder builder = new StringBuilder();
		builder.append("FieldEditorDetails [fieldEditorClass=");
		builder.append(fieldEditorClass);
		builder.append(", emptyStringAllowed=");
		builder.append(emptyStringAllowed);
		builder.append(", horizontalSpan=");
		builder.append(horizontalSpan);
		builder.append(", icons=");
		builder.append(Arrays.toString(icons));
		builder.append(", defaultToolTip=");
		builder.append(defaultToolTip);
		builder.append(", boldCustomValues=");
		builder.append(boldCustomValues);
		builder.append(", style=");
		builder.append(style);
		builder.append(", height=");
		builder.append(height);
		builder.append(", labelsAndValues=");
		builder.append(labelsAndValues);
		builder.append(", radioUseGroup=");
		builder.append(radioUseGroup);
		builder.append(", radioNumColumns=");
		builder.append(radioNumColumns);
		builder.append(", scaleMinimum=");
		builder.append(scaleMinimum);
		builder.append(", scaleMaximum=");
		builder.append(scaleMaximum);
		builder.append(", scaleIncrement=");
		builder.append(scaleIncrement);
		builder.append(", scalePageIncrement=");
		builder.append(scalePageIncrement);
		builder.append(", textLimit=");
		builder.append(textLimit);
		builder.append(", textWidth=");
		builder.append(textWidth);
		builder.append(", textValidateStrategy=");
		builder.append(textValidateStrategy);
		builder.append(", hashAlgorithm=");
		builder.append(hashAlgorithm);
		builder.append(", numberMinimum=");
		builder.append(numberMinimum);
		builder.append(", numberMaximum=");
		builder.append(numberMaximum);
		builder.append(", datePattern=");
		builder.append(datePattern);
		builder.append(", dateFrom=");
		builder.append(dateFrom);
		builder.append(", dateTo=");
		builder.append(dateTo);
		builder.append(", directoryDialogMessage=");
		builder.append(directoryDialogMessage);
		builder.append(", directoryMustExist=");
		builder.append(directoryMustExist);
		builder.append(", fileExtensions=");
		builder.append(Arrays.toString(fileExtensions));
		builder.append(", fileEnforceAbsolute=");
		builder.append(fileEnforceAbsolute);
		builder.append(", fontPreviewAreaText=");
		builder.append(fontPreviewAreaText);
		builder.append(", fontChangeButtonText=");
		builder.append(fontChangeButtonText);
		builder.append("]");
		return builder.toString();
	}

	public static class FieldEditorDetailsBuilder {
		private final Class<? extends FieldEditor> fieldEditorClass;
		private Boolean emptyStringAllowed;
		private Integer horizontalSpan;
		private Image[] icons;
		private Integer style;
		private Integer height;
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
		private Integer textValidateStrategy;
		private String hashAlgorithm;
		private Number numberMinimum;
		private Number numberMaximum;
		private String datePattern;
		private Date dateFrom;
		private Date dateTo;
		private ISupplier<String> directoryDialogMessage;
		private Boolean directoryMustExist;
		private String[] fileExtensions;
		private Boolean fileEnforceAbsolute;
		private ISupplier<String> fontPreviewAreaText;
		private ISupplier<String> fontChangeButtonText;

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

		public FieldEditorDetailsBuilder height(final int height) {
			this.height = height;
			return this;
		}

		public FieldEditorDetailsBuilder textValidateStrategy(final int validateStrategy) {
			this.textValidateStrategy = validateStrategy;
			return this;
		}

		public FieldEditorDetailsBuilder hashAlgorithm(final String hashAlgorithm) {
			this.hashAlgorithm = hashAlgorithm;
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

		public FieldEditorDetailsBuilder directoryDialogMessage(final ISupplier<String> dialogMessage) {
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

		public FieldEditorDetailsBuilder directoryMustExist(final boolean directoryMustExist) {
			this.directoryMustExist = directoryMustExist;
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

		public FieldEditorDetailsBuilder fontPreviewAreaText(final ISupplier<String> fontPreviewAreaText) {
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

		public FieldEditorDetailsBuilder fontChangeButtonText(final ISupplier<String> fontChangeButtonText) {
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
