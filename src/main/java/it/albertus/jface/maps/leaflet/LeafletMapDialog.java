package it.albertus.jface.maps.leaflet;

import java.util.Map.Entry;

import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.maps.MapDialog;
import it.albertus.jface.maps.MapMarker;
import it.albertus.net.httpserver.html.HtmlUtils;
import it.albertus.util.NewLine;

public class LeafletMapDialog extends MapDialog {

	private final LeafletMapOptions options = new LeafletMapOptions();

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
			final StringBuilder optionsBlock = new StringBuilder();
			optionsBlock.append(String.format("map.setView([%s, %s], %d);", getOptions().getCenterLat(), getOptions().getCenterLng(), getOptions().getZoom()));
			for (final Entry<LeafletMapControl, Boolean> control : options.getControls().entrySet()) {
				if (control.getValue()) {
					optionsBlock.append(NewLine.SYSTEM_LINE_SEPARATOR);
					optionsBlock.append(String.format("map.addControl(L.control.%s());", control.getKey().getName()));
				}
			}
			return optionsBlock.toString().trim();
		}
		// Markers
		else if (line.contains(MARKERS_PLACEHOLDER)) {
			if (getMarkers().isEmpty()) {
				return null;
			}
			else {
				final StringBuilder markersBlock = new StringBuilder();
				for (final MapMarker marker : getMarkers()) {
					markersBlock.append(String.format("L.marker([%s, %s]).addTo(map).bindPopup('%s');", marker.getLatitude(), marker.getLongitude(), HtmlUtils.escapeEcmaScript(marker.getTitle().replace(NewLine.SYSTEM_LINE_SEPARATOR, "<br />"))));
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
	public LeafletMapOptions getOptions() {
		return options;
	}

}
