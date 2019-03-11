package it.albertus.jface.maps.leaflet;

import java.text.DecimalFormat;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.maps.MapBounds;
import it.albertus.jface.maps.MapBoundsDialog;
import it.albertus.jface.maps.MapBoundsDialogCreationHelper;
import it.albertus.jface.maps.MapUtils;

public class LeafletMapBoundsDialog extends LeafletMapDialog implements MapBoundsDialog {

	private static final String JS_FUNCTION_NAME = "onMapEvent";

	private MapBounds bounds = new MapBounds();

	private final MapBoundsDialogCreationHelper helper = new MapBoundsDialogCreationHelper(this);

	public LeafletMapBoundsDialog(final Shell parent) {
		super(parent);
	}

	public LeafletMapBoundsDialog(final Shell parent, final int style) {
		super(parent, style);
	}

	@Override
	protected Layout getLayout() {
		return helper.getLayout();
	}

	@Override
	protected void createContents(final Shell shell) {
		shell.setLayout(getLayout());

		final Composite c = new Composite(shell, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(c);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(c);

		final Group latitudeGroup = new Group(c, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(latitudeGroup);
		GridLayoutFactory.swtDefaults().numColumns(4).applyTo(latitudeGroup);
		latitudeGroup.setText(JFaceMessages.get("lbl.map.bounds.range.latitude"));

		final Group longitudeGroup = new Group(c, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(longitudeGroup);
		GridLayoutFactory.swtDefaults().numColumns(4).applyTo(longitudeGroup);
		longitudeGroup.setText(JFaceMessages.get("lbl.map.bounds.range.longitude"));

		final Label southWestLatLabel = new Label(latitudeGroup, SWT.NONE);
		GridDataFactory.swtDefaults().applyTo(southWestLatLabel);
		southWestLatLabel.setText(JFaceMessages.get("lbl.map.bounds.range.from"));
		final Text southWestLatText = new Text(latitudeGroup, SWT.BORDER | SWT.READ_ONLY);
		southWestLatText.setEditable(false);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(southWestLatText);

		final Label northEastLatLabel = new Label(latitudeGroup, SWT.NONE);
		GridDataFactory.swtDefaults().applyTo(northEastLatLabel);
		northEastLatLabel.setText(JFaceMessages.get("lbl.map.bounds.range.to"));
		final Text northEastLatText = new Text(latitudeGroup, SWT.BORDER | SWT.READ_ONLY);
		northEastLatText.setEditable(false);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(northEastLatText);

		final Label southWestLngLabel = new Label(longitudeGroup, SWT.NONE);
		GridDataFactory.swtDefaults().applyTo(southWestLngLabel);
		southWestLngLabel.setText(JFaceMessages.get("lbl.map.bounds.range.from"));
		final Text southWestLngText = new Text(longitudeGroup, SWT.BORDER | SWT.READ_ONLY);
		southWestLngText.setEditable(false);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(southWestLngText);

		final Label northEastLngLabel = new Label(longitudeGroup, SWT.NONE);
		GridDataFactory.swtDefaults().applyTo(northEastLngLabel);
		northEastLngLabel.setText(JFaceMessages.get("lbl.map.bounds.range.to"));
		final Text northEastLngText = new Text(longitudeGroup, SWT.BORDER | SWT.READ_ONLY);
		northEastLngText.setEditable(false);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(northEastLngText);

		final Browser browser = createBrowser(shell);

		final BrowserFunction function = new BrowserFunction(browser, JS_FUNCTION_NAME) {
			@Override
			public Object function(final Object[] arguments) {
				final MapBounds mb = adjustBoundValues(getRawBoundValues(browser));
				final DecimalFormat coordinateFormat = MapUtils.getCoordinateFormat();
				if (mb.getSouthWestLat() != null && mb.getNorthEastLat() != null) {
					southWestLatText.setText(coordinateFormat.format(mb.getSouthWestLat()));
					northEastLatText.setText(coordinateFormat.format(mb.getNorthEastLat()));
				}
				if (mb.getSouthWestLng() != null && mb.getNorthEastLng() != null) {
					southWestLngText.setText(coordinateFormat.format(mb.getSouthWestLng()));
					northEastLngText.setText(coordinateFormat.format(mb.getNorthEastLng()));
				}
				return null;
			}
		};
		browser.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				function.dispose();
			}
		});

		createButtonBox(browser);

		browser.setFocus();
	}

	@Override
	protected Browser createBrowser(final Composite parent) {
		return helper.createBrowser(parent, getMapPage(parent));
	}

	@Override
	public Composite createButtonBox(final Browser browser) {
		return helper.createButtonBox(browser);
	}

	@Override
	public MapBounds getBounds() {
		return bounds;
	}

	@Override
	public String parseLine(final String line) {
		return parseLine(line, getOptions(), getMarkers(), String.format("map.on('resize', %s); map.on('move', %s); map.on('zoomend', %s); document.onload(%s());", JS_FUNCTION_NAME, JS_FUNCTION_NAME, JS_FUNCTION_NAME, JS_FUNCTION_NAME));
	}

	@Override
	public void setOptionValues(final Browser browser) {
		getOptions().setZoom(((Number) browser.evaluate("return map.getZoom();")).intValue());
		getOptions().setCenterLat((Double) browser.evaluate("return map.getCenter().lat;"));
		getOptions().setCenterLng((Double) browser.evaluate("return map.getCenter().lng;"));
	}

	@Override
	public void setBoundValues(final Browser browser) {
		bounds = adjustBoundValues(getRawBoundValues(browser));
	}

	private static MapBounds getRawBoundValues(final Browser browser) {
		final Double northEastLat = (Double) browser.evaluate("return map.getBounds().getNorthEast().lat;");
		final Double southWestLat = (Double) browser.evaluate("return map.getBounds().getSouthWest().lat;");
		final Double northEastLng = (Double) browser.evaluate("return map.getBounds().getNorthEast().lng;");
		final Double southWestLng = (Double) browser.evaluate("return map.getBounds().getSouthWest().lng;");
		return new MapBounds(northEastLat, southWestLat, northEastLng, southWestLng);
	}

	private static MapBounds adjustBoundValues(final MapBounds raw) {
		Double southWestLat = null;
		Double northEastLat = null;
		Double southWestLng = null;
		Double northEastLng = null;

		if (raw.getSouthWestLat() != null && raw.getNorthEastLat() != null) {
			southWestLat = Math.max(MapBounds.LATITUDE_MIN_VALUE, raw.getSouthWestLat());
			northEastLat = Math.min(MapBounds.LATITUDE_MAX_VALUE, raw.getNorthEastLat());
		}
		if (raw.getSouthWestLng() != null && raw.getNorthEastLng() != null) {
			if (Math.abs(raw.getSouthWestLng() - raw.getNorthEastLng()) >= MapBounds.LONGITUDE_MAX_VALUE * 2) {
				southWestLng = Double.valueOf(MapBounds.LONGITUDE_MIN_VALUE);
				northEastLng = Double.valueOf(MapBounds.LONGITUDE_MAX_VALUE);
			}
			else {
				double swl = raw.getSouthWestLng();
				while (Math.abs(swl) > MapBounds.LONGITUDE_MAX_VALUE) {
					swl -= Math.signum(swl) * MapBounds.LONGITUDE_MAX_VALUE * 2;
				}
				double nel = raw.getNorthEastLng();
				while (Math.abs(nel) > MapBounds.LONGITUDE_MAX_VALUE) {
					nel -= Math.signum(nel) * MapBounds.LONGITUDE_MAX_VALUE * 2;
				}
				southWestLng = Math.max(MapBounds.LONGITUDE_MIN_VALUE, swl);
				northEastLng = Math.min(MapBounds.LONGITUDE_MAX_VALUE, nel);
			}
		}
		return new MapBounds(northEastLat, southWestLat, northEastLng, southWestLng);
	}

}
