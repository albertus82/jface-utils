package it.albertus.jface.maps.leaflet;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.maps.MapBounds;
import it.albertus.jface.maps.MapBoundsDialog;
import it.albertus.jface.maps.MapBoundsDialogCreationHelper;

public class LeafletMapBoundsDialog extends LeafletMapDialog implements MapBoundsDialog {

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
	public void setOptionValues(final Browser browser) {
		getOptions().setZoom(((Number) browser.evaluate("return map.getZoom();")).intValue());
		getOptions().setCenterLat((Double) browser.evaluate("return map.getCenter().lat;"));
		getOptions().setCenterLng((Double) browser.evaluate("return map.getCenter().lng;"));
	}

	@Override
	public void setBoundValues(final Browser browser) {
		final Double northEastLat = (Double) browser.evaluate("return map.getBounds().getNorthEast().lat;");
		final Double southWestLat = (Double) browser.evaluate("return map.getBounds().getSouthWest().lat;");
		final Double northEastLng = (Double) browser.evaluate("return map.getBounds().getNorthEast().lng;");
		final Double southWestLng = (Double) browser.evaluate("return map.getBounds().getSouthWest().lng;");
		bounds = new MapBounds(northEastLat, southWestLat, northEastLng, southWestLng);
	}

}
