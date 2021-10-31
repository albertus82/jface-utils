package io.github.albertus82.jface;

import java.awt.image.BufferedImage;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/** @deprecated Use {@link ImageUtils} class. */
@Deprecated
public class HqImageResizer {

	private HqImageResizer() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * @param image source image
	 * @param scale scale factor (&lt;1 = downscaling, &gt;1 = upscaling)
	 * @return scaled image
	 * 
	 * @deprecated Use {@link ImageUtils#resize(Image, float)} class.
	 */
	@Deprecated
	public static Image resize(final Image image, final float scale) {
		return ImageUtils.resize(image, scale);
	}

	/**
	 * @param data source SWT image data
	 * @return AWT image
	 * 
	 * @deprecated Use {@link ImageUtils#convertToAWT(ImageData)} class.
	 */
	@Deprecated
	public static BufferedImage convertToAWT(final ImageData data) {
		return ImageUtils.convertToAWT(data);
	}

	/**
	 * @param image source AWT image
	 * @return SWT image data
	 * 
	 * @deprecated Use {@link ImageUtils#convertToSWT(BufferedImage)} class.
	 */
	@Deprecated
	public static ImageData convertToSWT(final BufferedImage image) {
		return ImageUtils.convertToSWT(image);
	}

}
