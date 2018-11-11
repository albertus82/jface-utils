package it.albertus.jface.maps.leaflet;

public enum LeafletMapControl {

	ZOOM("zoom"),
	SCALE("scale"),
	ATTRIBUTION("attribution");

	private final String name;

	private LeafletMapControl(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
