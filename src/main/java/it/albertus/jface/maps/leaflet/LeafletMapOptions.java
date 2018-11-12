package it.albertus.jface.maps.leaflet;

import java.util.EnumMap;
import java.util.Map;

import it.albertus.jface.maps.MapOptions;

public class LeafletMapOptions extends MapOptions {

	private static final long serialVersionUID = 6601920089135707963L;

	private final Map<LeafletMapControl, String> controls = new EnumMap<LeafletMapControl, String>(LeafletMapControl.class);

	public Map<LeafletMapControl, String> getControls() {
		return controls;
	}

	@Override
	public String toString() {
		return "LeafletMapOptions [centerLat=" + getCenterLat() + ", centerLng=" + getCenterLng() + ", zoom=" + getZoom() + ", controls=" + getControls() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + controls.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof LeafletMapOptions)) {
			return false;
		}
		LeafletMapOptions other = (LeafletMapOptions) obj;
		if (!controls.equals(other.controls)) {
			return false;
		}
		return true;
	}

}
