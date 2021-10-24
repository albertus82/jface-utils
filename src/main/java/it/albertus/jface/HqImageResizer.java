package it.albertus.jface;

import java.awt.image.BufferedImage;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/** @deprecated Use {@link ImageUtils} class. */
@Deprecated
public class HqImageResizer {

	private HqImageResizer() {
		throw new IllegalAccessError("Utility class");
	}

	/** @deprecated Use {@link ImageUtils#resize(Image, float)} class. */
	@Deprecated
	public static Image resize(final Image image, final float scale) {
		return ImageUtils.resize(image, scale);
	}

	/** @deprecated Use {@link ImageUtils#convertToAWT(ImageData)} class. */
	@Deprecated
	public static BufferedImage convertToAWT(final ImageData data) {
		return ImageUtils.convertToAWT(data);
	}

	/** @deprecated Use {@link ImageUtils#convertToSWT(BufferedImage)} class. */
	@Deprecated
	public static ImageData convertToSWT(final BufferedImage image) {
		return ImageUtils.convertToSWT(image);
	}

}
