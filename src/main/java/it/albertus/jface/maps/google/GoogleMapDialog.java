package it.albertus.jface.maps.google;

import java.util.Map.Entry;

import org.eclipse.swt.widgets.Shell;

import it.albertus.jface.JFaceMessages;
import it.albertus.jface.maps.MapDialog;
import it.albertus.jface.maps.MapMarker;
import it.albertus.net.httpserver.html.HtmlUtils;
import it.albertus.util.NewLine;

public class GoogleMapDialog extends MapDialog {

	public static final String DEFAULT_URL = "http://maps.googleapis.com/maps/api/js";

	private final GoogleMapOptions options = new GoogleMapOptions();

	private String url = DEFAULT_URL;

	public GoogleMapDialog(final Shell parent) {
		super(parent);
	}

	public GoogleMapDialog(final Shell parent, final int style) {
		super(parent, style);
	}

	@Override
	protected String parseLine(final String line) {
		// Language
		if (line.contains(DEFAULT_URL) && !JFaceMessages.getLanguage().isEmpty()) {
			return line.replace(DEFAULT_URL, url + "?language=" + JFaceMessages.getLanguage());
		}
		// Options
		else if (line.contains(OPTIONS_PLACEHOLDER)) {
			final StringBuilder optionsBlock = new StringBuilder();
			optionsBlock.append('\t').append("center: new google.maps.LatLng(").append(options.getCenterLat()).append(", ").append(options.getCenterLng()).append("),").append(NewLine.SYSTEM_LINE_SEPARATOR);
			optionsBlock.append('\t').append("zoom: ").append(options.getZoom()).append(',').append(NewLine.SYSTEM_LINE_SEPARATOR);
			optionsBlock.append('\t').append("mapTypeId: google.maps.MapTypeId.").append(options.getType().name());
			for (final Entry<GoogleMapControl, Boolean> control : options.getControls().entrySet()) {
				if (control.getKey() != null && control.getValue() != null) {
					optionsBlock.append(',').append(NewLine.SYSTEM_LINE_SEPARATOR);
					optionsBlock.append('\t').append(control.getKey().getFieldName()).append(": ").append(control.getValue().toString());
				}
			}
			return optionsBlock.toString();
		}
		// Markers
		else if (line.contains(MARKERS_PLACEHOLDER)) {
			if (getMarkers().isEmpty()) {
				return null;
			}
			else {
				int index = 1;
				final StringBuilder markersBlock = new StringBuilder();
				for (final MapMarker marker : getMarkers()) {
					markersBlock.append("var marker").append(index).append(" = new google.maps.Marker({").append(NewLine.SYSTEM_LINE_SEPARATOR);
					markersBlock.append('\t').append("position: new google.maps.LatLng(").append(marker.getLatitude()).append(", ").append(marker.getLongitude()).append("),").append(NewLine.SYSTEM_LINE_SEPARATOR);
					markersBlock.append('\t').append("map: map,").append(NewLine.SYSTEM_LINE_SEPARATOR);
					markersBlock.append('\t').append("title: '").append(HtmlUtils.escapeEcmaScript(marker.getTitle())).append("'").append(NewLine.SYSTEM_LINE_SEPARATOR);
					markersBlock.append("});").append(NewLine.SYSTEM_LINE_SEPARATOR);
					index++;
				}
				return markersBlock.toString().trim();
			}
		}
		else {
			return line;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@Override
	public GoogleMapOptions getOptions() {
		return options;
	}

}
