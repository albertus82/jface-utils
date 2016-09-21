package it.albertus.jface.google.maps;

import java.io.Serializable;

public class MapMarker implements Serializable {

	private static final long serialVersionUID = -8178591515901703859L;

	private final double latitude;
	private final double longitude;
	private final String title;

	public MapMarker(final double latitude, final double longitude, final String title) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title != null && !title.trim().isEmpty() ? title.trim() : "";
	}

	public MapMarker(final double latitude, final double longitude) {
		this(latitude, longitude, null);
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "MapMarker [latitude=" + latitude + ", longitude=" + longitude + ", " + (title != null && !title.trim().isEmpty() ? "title=" + title : "") + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude)) {
			return false;
		}
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude)) {
			return false;
		}
		return true;
	}

}
