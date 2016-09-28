package it.albertus.jface.preference.field;

import it.albertus.jface.JFaceMessages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

abstract class AbstractDateFieldEditor extends StringFieldEditor {

	/**
	 * Use {@link #parseDate} and {@link #formatDate} synchronized methods
	 * instead.
	 */
	@Deprecated
	protected final DateFormat dateFormat;

	private final Composite parent;

	private final String pattern;

	private final int style;

	private DateTime dateTime;

	private Date minValidValue;
	private Date maxValidValue;

	private int validateStrategy;

	protected synchronized Date parseDate(final String source) throws ParseException {
		return dateFormat.parse(source);
	}

	protected synchronized String formatDate(final Date date) {
		return dateFormat.format(date);
	}

	public AbstractDateFieldEditor(final String name, final String labelText, final String pattern, final int style, final Composite parent) {
		super(name, labelText, parent);
		checkPattern(pattern);
		this.pattern = pattern;
		this.parent = parent;
		this.style = style;
		this.validateStrategy = VALIDATE_ON_KEY_STROKE;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	public AbstractDateFieldEditor(final String name, final String labelText, final String pattern, final int style, final int width, final Composite parent) {
		super(name, labelText, width, parent);
		checkPattern(pattern);
		this.pattern = pattern;
		this.parent = parent;
		this.style = style;
		this.validateStrategy = VALIDATE_ON_KEY_STROKE;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	public AbstractDateFieldEditor(final String name, final String labelText, final String pattern, final int style, final int width, final int strategy, final Composite parent) {
		super(name, labelText, width, strategy, parent);
		checkPattern(pattern);
		this.pattern = pattern;
		this.parent = parent;
		this.style = style;
		this.validateStrategy = strategy;
		this.dateFormat = new SimpleDateFormat(pattern);
		init();
	}

	/** See {@link #doCreateControl}. */
	@Override
	protected void createControl(final Composite parent) {}

	@Override
	protected void valueChanged() {
		if (dateTime == null) {
			super.valueChanged();
		}
		else {
			setPresentsDefaultValue(false);
			final boolean oldState = isValid();
			refreshValidState();
			if (isValid() != oldState) {
				fireStateChanged(IS_VALID, oldState, isValid());
			}
			try {
				final String newValue = formatDate(getDateValue());
				if (!newValue.equals(oldValue)) {
					fireValueChanged(VALUE, oldValue, newValue);
					oldValue = newValue;
				}
			}
			catch (final ParseException pe) {
				pe.printStackTrace();
			}
		}
	}

	@Override
	protected void doFillIntoGrid(final Composite parent, final int numColumns) {
		if (style == SWT.NONE) {
			super.doFillIntoGrid(parent, numColumns);
		}
		else {
			getLabelControl(parent);
			dateTime = getDateTimeControl(parent);
			final GridData gd = new GridData();
			gd.horizontalSpan = numColumns - 1;
			gd.horizontalAlignment = GridData.FILL;
			gd.grabExcessHorizontalSpace = true;
			dateTime.setLayoutData(gd);
		}
	}

	protected DateTime getDateTimeControl(final Composite parent) {
		final DateTime dateTime = new DateTime(parent, style);
		switch (validateStrategy) {
		case VALIDATE_ON_KEY_STROKE:
			dateTime.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					valueChanged();
				}
			});
			dateTime.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					valueChanged();
				}
			});
			break;
		case VALIDATE_ON_FOCUS_LOST:
			dateTime.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					clearErrorMessage();
				}
			});
			dateTime.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					refreshValidState();
				}

				@Override
				public void focusLost(FocusEvent e) {
					valueChanged();
					clearErrorMessage();
				}
			});
			break;
		default:
			Assert.isTrue(false, "Unknown validate strategy");
		}
		return dateTime;
	}

	@Override
	protected void adjustForNumColumns(final int numColumns) {
		final GridData gd;
		if (getTextControl() != null) {
			gd = (GridData) getTextControl().getLayoutData();
		}
		else {
			gd = (GridData) dateTime.getLayoutData();
		}
		gd.horizontalSpan = numColumns - 1;
		gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
	}

	@Override
	protected boolean checkState() {
		if (dateTime == null) {
			return super.checkState();
		}
		else {
			boolean result = doCheckState();
			if (result) {
				clearErrorMessage();
			}
			else {
				showErrorMessage(getErrorMessage());
			}
			return result;
		}
	}

	@Override
	protected boolean doCheckState() {
		boolean result;
		Date date = null;
		if (isEmptyStringAllowed() && getTextControl() != null && getTextControl().getText().isEmpty()) {
			result = true;
		}
		else {
			try {
				date = getDateValue();
				result = true;
			}
			catch (final ParseException pe) {
				result = false;
			}
		}
		if (date != null && ((getMinValidValue() != null && date.before(getMinValidValue())) || (getMaxValidValue() != null && date.after(getMaxValidValue())))) {
			result = false;
		}
		return result;
	}

	@Override
	protected void doLoad() {
		if (dateTime == null) {
			super.doLoad();
		}
		else {
			final String value = getPreferenceStore().getString(getPreferenceName());
			final Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(parseDate(value));
			}
			catch (final ParseException pe) {/* Ignore */}
			setDateTimeValue(calendar);
			oldValue = value;
		}
	}

	@Override
	protected void doLoadDefault() {
		if (dateTime == null) {
			super.doLoadDefault();
		}
		else {
			final String value = getPreferenceStore().getDefaultString(getPreferenceName());
			final Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(parseDate(value));
			}
			catch (final ParseException pe) {/* Ignore */}
			setDateTimeValue(calendar);
			valueChanged();
		}
	}

	@Override
	protected void doStore() {
		if (dateTime == null) {
			super.doStore();
		}
		else {
			try {
				final Date date = getDateValue();
				final String dateString = formatDate(date);
				getPreferenceStore().setValue(getPreferenceName(), dateString);
			}
			catch (final ParseException pe) {
				pe.printStackTrace();
			}
		}
	}

	protected void setDateTimeValue(final Calendar calendar) {
		dateTime.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dateTime.setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
	}

	protected void setDateTimeValue(final Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setLenient(false);
		calendar.setTime(date);
		setDateTimeValue(calendar);
	}

	public void setValidRange(final Date from, final Date to) {
		setMinValidValue(from);
		setMaxValidValue(to);
	}

	protected void checkPattern(final String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("Missing date pattern");
		}
	}

	protected void init() {
		dateFormat.setLenient(false);
		updateErrorMessage();
		setTextLimit(Byte.MAX_VALUE);
		doCreateControl();
	}

	protected void doCreateControl() {
		final GridLayout layout = new GridLayout();
		layout.numColumns = getNumberOfControls();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = HORIZONTAL_GAP;
		parent.setLayout(layout);
		doFillIntoGrid(parent, layout.numColumns);
	}

	public DateTime getDateTimeControl() {
		return dateTime;
	}

	public String getPattern() {
		return pattern;
	}

	public int getStyle() {
		return style;
	}

	public Date getDateValue() throws ParseException {
		if (dateTime == null) {
			return parseDate(getTextControl().getText());
		}
		else {
			final Calendar calendar = Calendar.getInstance();
			calendar.setLenient(false);
			calendar.set(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(), dateTime.getHours(), dateTime.getMinutes(), dateTime.getSeconds());
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();
		}
	}

	public Date getMinValidValue() {
		return minValidValue;
	}

	public void setMinValidValue(final Date minValidValue) {
		this.minValidValue = minValidValue;
		updateErrorMessage();
	}

	public Date getMaxValidValue() {
		return maxValidValue;
	}

	public void setMaxValidValue(final Date maxValidValue) {
		this.maxValidValue = maxValidValue;
		updateErrorMessage();
	}

	public int getValidateStrategy() {
		return validateStrategy;
	}

	@Override
	public void setValidateStrategy(final int value) {
		super.setValidateStrategy(value);
		this.validateStrategy = value;
	}

	protected void updateErrorMessage() {
		if (getMinValidValue() == null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences.date", pattern));
		}
		else if (getMinValidValue() != null && getMaxValidValue() == null) {
			setErrorMessage(JFaceMessages.get("err.preferences.date.from", formatDate(getMinValidValue())));
		}
		else if (getMinValidValue() == null && getMaxValidValue() != null) {
			setErrorMessage(JFaceMessages.get("err.preferences.date.to", formatDate(getMaxValidValue())));
		}
		else {
			setErrorMessage(JFaceMessages.get("err.preferences.date.range", formatDate(getMinValidValue()), formatDate(getMaxValidValue())));
		}
	}

}
