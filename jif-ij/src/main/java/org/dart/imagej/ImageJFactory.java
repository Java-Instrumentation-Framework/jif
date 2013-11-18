/**
 *
 */
package org.dart.imagej;

import ij.ImageJ;

/**
 * @author Tony I'Hagan
 *
 */
public final class ImageJFactory {
	static ImageJ ij;

	private ImageJFactory() {
		super();
	}

	public static ImageJ create() {
		if (ImageJFactory.ij != null) {
			ImageJFactory.ij = new ImageJ();
		}

		return ImageJFactory.ij;
	}
}
