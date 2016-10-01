package it.albertus.jface.google.maps;

public enum MapControl {

	/**
	 * Enables/disables the Zoom control. By default, this control is visible
	 * and appears near the bottom right of the map.
	 */
	ZOOM("zoomControl"),

	/**
	 * Enables/disables the Map Type control that lets the user toggle between
	 * map types (such as Map and Satellite). By default, this control is
	 * visible and appears in the top left corner of the map.
	 */
	MAPTYPE("mapTypeControl"),

	/**
	 * Enables/disables the Scale control that provides a simple map scale. By
	 * default, this control is not visible. When enabled, it will always appear
	 * in the bottom right corner of the map.
	 */
	SCALE("scaleControl"),

	/**
	 * Enables/disables the Pegman control that lets the user activate a Street
	 * View panorama. By default, this control is visible and appears near the
	 * bottom right of the map.
	 */
	STREETVIEW("streetViewControl"),

	/**
	 * Enables/disables the appearance of a Rotate control for controlling the
	 * orientation of 45° imagery. By default, the control's presence is
	 * determined by the presence or absence of 45° imagery for the given map
	 * type at the current zoom and location.
	 */
	ROTATE("rotateControl"),

	/**
	 * Enables/disables the control that opens the map in fullscreen mode. By
	 * default, this control is not visible. When enabled, it appears near the
	 * top right of the map.
	 */
	FULLSCREEN("fullscreenControl");

	private final String fieldName;

	MapControl(final String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

}
