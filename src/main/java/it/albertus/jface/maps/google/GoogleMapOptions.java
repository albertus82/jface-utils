package it.albertus.jface.maps.google;

import it.albertus.jface.maps.MapOptions;

public class GoogleMapOptions extends MapOptions {

	private static final long serialVersionUID = 8324663629284543572L;

	public static final GoogleMapType DEFAULT_TYPE = GoogleMapType.ROADMAP;

	private GoogleMapType type = DEFAULT_TYPE;

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

	@Override
	public String toString() {
		return "GoogleMapOptions [centerLat=" + getCenterLat() + ", centerLng=" + getCenterLng() + ", zoom=" + getZoom() + ", type=" + type + ", controls=" + getControls() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(getCenterLat());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(getCenterLng());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((getControls() == null) ? 0 : getControls().hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + getZoom();
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
		if (!(obj instanceof GoogleMapOptions)) {
			return false;
		}
		GoogleMapOptions other = (GoogleMapOptions) obj;
		if (Double.doubleToLongBits(getCenterLat()) != Double.doubleToLongBits(other.getCenterLat())) {
			return false;
		}
		if (Double.doubleToLongBits(getCenterLng()) != Double.doubleToLongBits(other.getCenterLng())) {
			return false;
		}
		if (getControls() == null) {
			if (other.getControls() != null) {
				return false;
			}
		}
		else if (!getControls().equals(other.getControls())) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (getZoom() != other.getZoom()) {
			return false;
		}
		return true;
	}

}
