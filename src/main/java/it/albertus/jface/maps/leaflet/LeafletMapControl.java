package it.albertus.jface.maps.leaflet;

public enum LeafletMapControl {

	/** A basic zoom control with two buttons (zoom in and zoom out). */
	ZOOM("zoom"),

	/**
	 * A simple scale control that shows the scale of the current center of screen
	 * in metric (m/km) and imperial (mi/ft) systems.
	 */
	SCALE("scale"),

	/**
	 * The attribution control allows you to display attribution data in a small
	 * text box on a map.
	 */
	ATTRIBUTION("attribution"),

	/**
	 * The layers control gives users the ability to switch between different base
	 * layers and switch overlays on/off.
	 */
	LAYERS("layers");

	private final String constructor;

	private LeafletMapControl(final String constructor) {
		this.constructor = constructor;
	}

	public String getConstructor() {
		return constructor;
	}

}
