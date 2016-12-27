package it.albertus.jface.google.maps;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class MapOptions implements Serializable {

	private static final long serialVersionUID = 8324663629284543572L;

	public static final MapType DEFAULT_TYPE = MapType.ROADMAP;
	public static final int DEFAULT_ZOOM = 1;

	private double centerLat;
	private double centerLng;
	private int zoom = DEFAULT_ZOOM;
	private MapType type = DEFAULT_TYPE;
	private final Map<MapControl, Boolean> controls = new EnumMap<MapControl, Boolean>(MapControl.class);

	public MapOptions() {/* Default constructor */}

	public MapOptions(final double centerLat, final double centerLng, final int zoom, final MapType type) {
		this.centerLat = centerLat;
		this.centerLng = centerLng;
		this.zoom = zoom;
		this.type = type;
	}

	public double getCenterLat() {
		return centerLat;
	}

	public void setCenterLat(final double centerLat) {
		this.centerLat = centerLat;
	}

	public double getCenterLng() {
		return centerLng;
	}

	public void setCenterLng(final double centerLng) {
		this.centerLng = centerLng;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(final int zoom) {
		this.zoom = zoom;
	}

	public MapType getType() {
		return type;
	}

	public void setType(final MapType type) {
		if (type == null) {
			throw new IllegalArgumentException(String.valueOf(type));
		}
		this.type = type;
	}

	public Map<MapControl, Boolean> getControls() {
		return controls;
	}

	@Override
	public String toString() {
		return "MapOptions [centerLat=" + centerLat + ", centerLng=" + centerLng + ", zoom=" + zoom + ", type=" + type + ", controls=" + controls + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(centerLat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(centerLng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((controls == null) ? 0 : controls.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + zoom;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MapOptions)) {
			return false;
		}
		MapOptions other = (MapOptions) obj;
		if (Double.doubleToLongBits(centerLat) != Double.doubleToLongBits(other.centerLat)) {
			return false;
		}
		if (Double.doubleToLongBits(centerLng) != Double.doubleToLongBits(other.centerLng)) {
			return false;
		}
		if (controls == null) {
			if (other.controls != null) {
				return false;
			}
		}
		else if (!controls.equals(other.controls)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (zoom != other.zoom) {
			return false;
		}
		return true;
	}

}
