package it.albertus.jface.maps.google;

import java.util.EnumMap;
import java.util.Map;

import it.albertus.jface.maps.MapOptions;

public class GoogleMapOptions extends MapOptions {

	private static final long serialVersionUID = 8324663629284543572L;

	public static final GoogleMapType DEFAULT_TYPE = GoogleMapType.ROADMAP;

	private GoogleMapType type = DEFAULT_TYPE;

	private final Map<GoogleMapControl, Boolean> controls = new EnumMap<GoogleMapControl, Boolean>(GoogleMapControl.class);

	public GoogleMapOptions() {/* Default constructor */}

	public GoogleMapOptions(final double centerLat, final double centerLng, final int zoom, final GoogleMapType type) {
		super(centerLat, centerLng, zoom);
		this.type = type;
	}

	public GoogleMapType getType() {
		return type;
	}

	public void setType(final GoogleMapType type) {
		if (type == null) {
			throw new IllegalArgumentException(String.valueOf(type));
		}
		this.type = type;
	}

	public Map<GoogleMapControl, Boolean> getControls() {
		return controls;
	}

	@Override
	public String toString() {
		return "GoogleMapOptions [centerLat=" + getCenterLat() + ", centerLng=" + getCenterLng() + ", zoom=" + getZoom() + ", type=" + type + ", controls=" + getControls() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + controls.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (!(obj instanceof GoogleMapOptions)) {
			return false;
		}
		GoogleMapOptions other = (GoogleMapOptions) obj;
		if (!controls.equals(other.controls)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

}
