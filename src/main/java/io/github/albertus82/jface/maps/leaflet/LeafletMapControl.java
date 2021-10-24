package io.github.albertus82.jface.maps.leaflet;

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
	 * layers and switch overlays on/off. This control requires a value, e.g.:
	 * 
	 * <pre>
	 * {
	 *   &quot;OpenStreetMap&quot;: L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	 *     maxZoom: 19,
	 *     attribution: '&amp;copy; &lt;a href=&quot;http://www.openstreetmap.org/copyright&quot;&gt;OpenStreetMap&lt;/a&gt;'
	 *   }).addTo(map),
	 *   &quot;OpenTopoMap&quot;: L.tileLayer('https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png', {
	 *     maxZoom: 17,
	 *     attribution: 'Map data: &amp;copy; &lt;a href=&quot;http://www.openstreetmap.org/copyright&quot;&gt;OpenStreetMap&lt;/a&gt;, &lt;a href=&quot;http://viewfinderpanoramas.org&quot;&gt;SRTM&lt;/a&gt; | Map style: &amp;copy; &lt;a href=&quot;https://opentopomap.org&quot;&gt;OpenTopoMap&lt;/a&gt; (&lt;a href=&quot;https://creativecommons.org/licenses/by-sa/3.0/&quot;&gt;CC-BY-SA&lt;/a&gt;)'
	 *   })
	 * }
	 * </pre>
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
