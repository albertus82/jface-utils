package it.albertus.jface.google.maps;

import java.io.Serializable;

public class MapOptions implements Serializable {

	private static final long serialVersionUID = 8324663629284543572L;

	public static final MapType DEFAULT_TYPE = MapType.ROADMAP;
	public static final int DEFAULT_ZOOM = 1;

	private float centerLat;
	private float centerLng;
	private int zoom = DEFAULT_ZOOM;
	private MapType type = DEFAULT_TYPE;

	public MapOptions() {}

	public MapOptions(final float centerLat, final float centerLng, final int zoom, final MapType type) {
		this.centerLat = centerLat;
		this.centerLng = centerLng;
		this.zoom = zoom;
		this.type = type;
	}

	public float getCenterLat() {
		return centerLat;
	}

	public void setCenterLat(final float centerLat) {
		this.centerLat = centerLat;
	}

	public float getCenterLng() {
		return centerLng;
	}

	public void setCenterLng(final float centerLng) {
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

	@Override
	public String toString() {
		return "MapOptions [centerLat=" + centerLat + ", centerLng=" + centerLng + ", zoom=" + zoom + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(centerLat);
		result = prime * result + Float.floatToIntBits(centerLng);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + zoom;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
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
		if (Float.floatToIntBits(centerLat) != Float.floatToIntBits(other.centerLat)) {
			return false;
		}
		if (Float.floatToIntBits(centerLng) != Float.floatToIntBits(other.centerLng)) {
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
