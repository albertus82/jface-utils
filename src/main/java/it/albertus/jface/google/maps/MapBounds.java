package it.albertus.jface.google.maps;

import java.io.Serializable;

public class MapBounds implements Serializable {

	private static final long serialVersionUID = 8379255527593727314L;

	private Double northEastLat;
	private Double southWestLat;
	private Double northEastLng;
	private Double southWestLng;

	public MapBounds() {}

	public MapBounds(final Double northEastLat, final Double southWestLat, final Double northEastLng, final Double southWestLng) {
		this.northEastLat = northEastLat;
		this.southWestLat = southWestLat;
		this.northEastLng = northEastLng;
		this.southWestLng = southWestLng;
	}

	public Double getNorthEastLat() {
		return northEastLat;
	}

	public void setNorthEastLat(final Double northEastLat) {
		this.northEastLat = northEastLat;
	}

	public Double getSouthWestLat() {
		return southWestLat;
	}

	public void setSouthWestLat(final Double southWestLat) {
		this.southWestLat = southWestLat;
	}

	public Double getNorthEastLng() {
		return northEastLng;
	}

	public void setNorthEastLng(final Double northEastLng) {
		this.northEastLng = northEastLng;
	}

	public Double getSouthWestLng() {
		return southWestLng;
	}

	public void setSouthWestLng(final Double southWestLng) {
		this.southWestLng = southWestLng;
	}

	@Override
	public String toString() {
		return "MapBounds [northEastLat=" + northEastLat + ", southWestLat=" + southWestLat + ", northEastLng=" + northEastLng + ", southWestLng=" + southWestLng + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((northEastLat == null) ? 0 : northEastLat.hashCode());
		result = prime * result + ((northEastLng == null) ? 0 : northEastLng.hashCode());
		result = prime * result + ((southWestLat == null) ? 0 : southWestLat.hashCode());
		result = prime * result + ((southWestLng == null) ? 0 : southWestLng.hashCode());
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
		if (!(obj instanceof MapBounds)) {
			return false;
		}
		MapBounds other = (MapBounds) obj;
		if (northEastLat == null) {
			if (other.northEastLat != null) {
				return false;
			}
		}
		else if (!northEastLat.equals(other.northEastLat)) {
			return false;
		}
		if (northEastLng == null) {
			if (other.northEastLng != null) {
				return false;
			}
		}
		else if (!northEastLng.equals(other.northEastLng)) {
			return false;
		}
		if (southWestLat == null) {
			if (other.southWestLat != null) {
				return false;
			}
		}
		else if (!southWestLat.equals(other.southWestLat)) {
			return false;
		}
		if (southWestLng == null) {
			if (other.southWestLng != null) {
				return false;
			}
		}
		else if (!southWestLng.equals(other.southWestLng)) {
			return false;
		}
		return true;
	}

}
