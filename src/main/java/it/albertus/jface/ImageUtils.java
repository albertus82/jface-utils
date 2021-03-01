package it.albertus.jface;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.SortOrder;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import it.albertus.util.IOUtils;

/* https://stackoverflow.com/questions/4752748/swt-how-to-do-high-quality-image-resize */
public class ImageUtils {

	private ImageUtils() {
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Resizes an image, using the given scaling factor. Constructs a new image
	 * resource, please take care of resource disposal if you no longer need the
	 * original one. This method is optimized for quality, not for speed.
	 * 
	 * @param image source image
	 * @param scale scale factor (<1 = downscaling, >1 = upscaling)
	 * @return scaled image
	 */
	public static Image resize(final Image image, final float scale) {
		final int w = image.getBounds().width;
		final int h = image.getBounds().height;

		// Convert to buffered image
		BufferedImage img = convertToAWT(image.getImageData());

		// Resize buffered image
		final int newWidth = Math.round(scale * w);
		final int newHeight = Math.round(scale * h);

		// Determine scaling mode for best result: if downsizing, use area averaging, if upsizing, use smooth scaling (usually bilinear).
		final int mode = scale < 1 ? java.awt.Image.SCALE_AREA_AVERAGING : java.awt.Image.SCALE_SMOOTH;
		final java.awt.Image scaledImage = img.getScaledInstance(newWidth, newHeight, mode);

		// Convert the scaled image back to a buffered image
		img = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		img.getGraphics().drawImage(scaledImage, 0, 0, null);

		// Reconstruct SWT image
		final ImageData imageData = convertToSWT(img);
		return new org.eclipse.swt.graphics.Image(Display.getDefault(), imageData);
	}

	public static BufferedImage convertToAWT(final ImageData data) {
		final ColorModel colorModel;
		final PaletteData palette = data.palette;
		if (palette.isDirect) {
			colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask, palette.blueMask);
			final BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
			final WritableRaster raster = bufferedImage.getRaster();
			final int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					final int pixel = data.getPixel(x, y);
					final RGB rgb = palette.getRGB(pixel);
					pixelArray[0] = rgb.red;
					pixelArray[1] = rgb.green;
					pixelArray[2] = rgb.blue;
					raster.setPixels(x, y, 1, 1, pixelArray);
				}
			}
			return bufferedImage;
		}
		else {
			final RGB[] rgbs = palette.getRGBs();
			final byte[] red = new byte[rgbs.length];
			final byte[] green = new byte[rgbs.length];
			final byte[] blue = new byte[rgbs.length];
			for (int i = 0; i < rgbs.length; i++) {
				final RGB rgb = rgbs[i];
				red[i] = (byte) rgb.red;
				green[i] = (byte) rgb.green;
				blue[i] = (byte) rgb.blue;
			}
			if (data.transparentPixel != -1) {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue, data.transparentPixel);
			}
			else {
				colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
			}
			final BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
			final WritableRaster raster = bufferedImage.getRaster();
			final int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					final int pixel = data.getPixel(x, y);
					pixelArray[0] = pixel;
					raster.setPixel(x, y, pixelArray);
				}
			}
			return bufferedImage;
		}
	}

	public static ImageData convertToSWT(final BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			final DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
			final PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			final ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			final WritableRaster raster = bufferedImage.getRaster();
			final int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					final int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		}
		else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			final IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
			final int size = colorModel.getMapSize();
			final byte[] reds = new byte[size];
			final byte[] greens = new byte[size];
			final byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			final RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			final PaletteData palette = new PaletteData(rgbs);
			final ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			final WritableRaster raster = bufferedImage.getRaster();
			final int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}

	public static Map<Rectangle, Image> createImageMap(final Iterable<String> resourceNames, final SortOrder areaSortOrder) {
		final Map<Rectangle, Image> map = new TreeMap<Rectangle, Image>(getAreaComparator(areaSortOrder));
		for (final String resourceName : resourceNames) {
			InputStream stream = null;
			try {
				stream = ImageUtils.class.getResourceAsStream(resourceName);
				for (final ImageData data : new ImageLoader().load(stream)) {
					final Image image = new Image(Display.getCurrent(), data);
					map.put(image.getBounds(), image);
				}
			}
			finally {
				IOUtils.closeQuietly(stream);
			}
		}
		return map;
	}

	public static Comparator<Rectangle> getAreaComparator(final SortOrder sortOrder) {
		return new Comparator<Rectangle>() {
			@Override
			public int compare(final Rectangle r1, final Rectangle r2) {
				final int value = compareAscending(r1, r2);
				return SortOrder.DESCENDING.equals(sortOrder) ? -value : value;
			}

			private int compareAscending(final Rectangle r1, final Rectangle r2) {
				final int a1 = r1.width * r1.height;
				final int a2 = r2.width * r2.height;
				if (a1 > a2) {
					return 1;
				}
				if (a1 < a2) {
					return -1;
				}
				return 0;
			}
		};
	}

}
