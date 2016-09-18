package it.albertus.jface.google.maps;

import java.io.Serializable;

public class MapMarker implements Serializable {

	private static final long serialVersionUID = -8178591515901703859L;

	private final float latitude;
	private final float longitude;
	private final String title;

	public MapMarker(final float latitude, final float longitude, final String title) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title != null && !title.trim().isEmpty() ? title.trim() : "";
	}

	public MapMarker(final float latitude, final float longitude) {
		this(latitude, longitude, null);
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(latitude);
		result = prime * result + Float.floatToIntBits(longitude);
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
		if (!(obj instanceof MapMarker)) {
			return false;
		}
		MapMarker other = (MapMarker) obj;
		if (Float.floatToIntBits(latitude) != Float.floatToIntBits(other.latitude)) {
			return false;
		}
		if (Float.floatToIntBits(longitude) != Float.floatToIntBits(other.longitude)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Marker [latitude=" + latitude + ", longitude=" + longitude + ", " + (title != null && !title.trim().isEmpty() ? "title=" + title : "") + "]";
	}

}
