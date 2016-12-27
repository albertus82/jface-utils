package it.albertus.util;

/**
 * @deprecated use {@link it.albertus.util.ZipUtils ZipUtils} instead.
 */
@Deprecated
public class Zipper extends ZipUtils {

	/**
	 * @deprecated use static methods of {@link it.albertus.util.ZipUtils
	 *             ZipUtils} instead.
	 */
	@Deprecated
	public static Zipper getInstance() {
		return new Zipper();
	}

}
