package it.albertus.jface.maps.google;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.maps.MapBounds;
import it.albertus.jface.maps.MapBoundsDialog;
import it.albertus.jface.maps.MapBoundsDialogCreationHelper;

public class GoogleMapBoundsDialog extends GoogleMapDialog implements MapBoundsDialog {

	private final MapBounds bounds = new MapBounds();

	private final MapBoundsDialogCreationHelper helper = new MapBoundsDialogCreationHelper(this);

	public GoogleMapBoundsDialog(final Shell parent) {
		super(parent);
	}

	public GoogleMapBoundsDialog(final Shell parent, final int style) {
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
		getOptions().setCenterLat((Double) browser.evaluate("return map.getCenter().lat();"));
		getOptions().setCenterLng((Double) browser.evaluate("return map.getCenter().lng();"));
	}

	@Override
	public void setBoundValues(final Browser browser) {
		getBounds().setNorthEastLat((Double) browser.evaluate("return map.getBounds().getNorthEast().lat();"));
		getBounds().setSouthWestLat((Double) browser.evaluate("return map.getBounds().getSouthWest().lat();"));
		getBounds().setNorthEastLng((Double) browser.evaluate("return map.getBounds().getNorthEast().lng();"));
		getBounds().setSouthWestLng((Double) browser.evaluate("return map.getBounds().getSouthWest().lng();"));
	}

}
