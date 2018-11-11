package it.albertus.jface.maps.leaflet;

import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.maps.MapDialog;
import it.albertus.jface.maps.MapMarker;
import it.albertus.jface.maps.MapOptions;
import it.albertus.net.httpserver.html.HtmlUtils;
import it.albertus.util.NewLine;

public class LeafletMapDialog extends MapDialog {

	private final MapOptions options = new MapOptions();

	public LeafletMapDialog(final Shell parent) {
		super(parent);
	}

	public LeafletMapDialog(final Shell parent, final int style) {
		super(parent, style);
	}

	@Override
	protected String parseLine(final String line) {
		// Options
		if (line.contains(OPTIONS_PLACEHOLDER)) {
			return String.format("map.setView([%s, %s], %d);", getOptions().getCenterLat(), getOptions().getCenterLng(), getOptions().getZoom());
		}
		// Markers
		else if (line.contains(MARKERS_PLACEHOLDER)) {
			if (getMarkers().isEmpty()) {
				return null;
			}
			else {
				final String mask = "L.marker([%s, %s]).addTo(map).bindPopup('%s');";
				final StringBuilder markersBlock = new StringBuilder();
				for (final MapMarker marker : getMarkers()) {
					markersBlock.append(String.format(mask, marker.getLatitude(), marker.getLongitude(), HtmlUtils.escapeEcmaScript(marker.getTitle().replace(NewLine.SYSTEM_LINE_SEPARATOR, "<br />"))));
					markersBlock.append(NewLine.SYSTEM_LINE_SEPARATOR);
				}
				return markersBlock.toString().trim();
			}
		}
		else {
			return line;
		}
	}

	@Override
	public MapOptions getOptions() {
		return options;
	}

}
