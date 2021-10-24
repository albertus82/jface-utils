package it.albertus.jface.maps.google;

// @formatter:off
/**
 * <p>
 * The maps displayed through the Google Maps JavaScript API contain UI elements
 * to allow user interaction with the map. These elements are known as
 * <em>controls</em> and you can include variations of these controls in your
 * application. Alternatively, you can do nothing and let the Google Maps
 * JavaScript API handle all control behavior.
 * </p>
 * 
 * <p>
 * Below is a list of the full set of controls you can use in your maps:
 * </p>
 * <ul>
 * <li>The <em><strong>Zoom control</strong></em> displays "+" and "-" buttons
 * for changing the zoom level of the map. This control appears by default in
 * the bottom right corner of the map.</li>
 * <li>The <em><strong>Map Type control</strong></em> is available in a dropdown
 * or horizontal button bar style, allowing the user to choose a map type (
 * {@code ROADMAP}, {@code SATELLITE}, {@code HYBRID}, or {@code TERRAIN}). This
 * control appears by default in the top left corner of the map.</li>
 * <li>The <em><strong>Street View</strong></em> control contains a Pegman icon
 * which can be dragged onto the map to enable Street View. This control appears
 * by default near the bottom right of the map.</li>
 * <li>The <em><strong>Rotate control</strong></em> provides a combination of
 * tilt and rotate options for maps containing oblique imagery. This control
 * appears by default near the bottom right of the map. See 45&deg; imagery for
 * more information.</li>
 * <li>The <em><strong>Scale control</strong></em> displays a map scale element.
 * This control is disabled by default.</li>
 * <li>The <em><strong>Fullscreen control</strong></em> offers the option to
 * open the map in fullscreen mode. This control is disabled by default.</li>
 * </ul>
 * 
 * <p>
 * You may wish to tailor your interface by removing, adding, or modifying UI
 * behavior or controls and ensure that future updates don't alter this
 * behavior. If you wish to only add or modify existing behavior, you need to
 * ensure that the control is explicitly added to your application.
 * </p>
 * 
 * <p>
 * Some controls appear on the map by default while others will not appear
 * unless you specifically request them. Adding or removing controls from the
 * map is specified in the following {@code MapOptions} object's fields, which
 * you set to true to make them visible or set to false to hide them:
 * </p>
 * 
 * <pre>
 * {
 *   zoomControl: boolean,
 *   mapTypeControl: boolean,
 *   scaleControl: boolean,
 *   streetViewControl: boolean,
 *   rotateControl: boolean,
 *   fullscreenControl: boolean
 * }</pre>
 * 
 * <p>
 * By default, all the controls disappear if the map is smaller than 200x200px.
 * You can override this behavior by explicitly setting the control to be
 * visible.
 * </p>
 * 
 * <p>
 * <em> Portions of this page are reproduced from work created and shared by
 * Google and used according to terms described in the Creative Commons 3.0
 * Attribution License.</em> <a href=
 * "https://developers.google.com/maps/documentation/javascript/controls" >See
 * documentation online</a>.
 * </p>
 */
// @formatter:on
public enum GoogleMapControl {

	/**
	 * Enables/disables the Zoom control. By default, this control is visible and
	 * appears near the bottom right of the map.
	 */
	ZOOM("zoomControl"),

	/**
	 * Enables/disables the Map Type control that lets the user toggle between map
	 * types (such as Map and Satellite). By default, this control is visible and
	 * appears in the top left corner of the map.
	 */
	MAPTYPE("mapTypeControl"),

	/**
	 * Enables/disables the Scale control that provides a simple map scale. By
	 * default, this control is not visible. When enabled, it will always appear in
	 * the bottom right corner of the map.
	 */
	SCALE("scaleControl"),

	/**
	 * Enables/disables the Pegman control that lets the user activate a Street View
	 * panorama. By default, this control is visible and appears near the bottom
	 * right of the map.
	 */
	STREETVIEW("streetViewControl"),

	/**
	 * Enables/disables the appearance of a Rotate control for controlling the
	 * orientation of 45&deg; imagery. By default, the control's presence is
	 * determined by the presence or absence of 45&deg; imagery for the given map
	 * type at the current zoom and location.
	 */
	ROTATE("rotateControl"),

	/**
	 * Enables/disables the control that opens the map in fullscreen mode. By
	 * default, this control is not visible. When enabled, it appears near the top
	 * right of the map.
	 */
	FULLSCREEN("fullscreenControl");

	private final String fieldName;

	GoogleMapControl(final String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

}
