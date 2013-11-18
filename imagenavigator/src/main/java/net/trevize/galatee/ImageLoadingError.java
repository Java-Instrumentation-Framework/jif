package net.trevize.galatee;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * ImageLoadingError.java - Feb 2, 2010
 */

public class ImageLoadingError {

	public static BufferedImage getImageLoadingError(Dimension imgDim) {
		BufferedImage bi;

		bi = new BufferedImage(imgDim.width, imgDim.height,
				BufferedImage.TYPE_INT_RGB);

		bi.getGraphics().setColor(Color.WHITE);
		bi.getGraphics().drawString("IMAGE", 20, 20);
		bi.getGraphics().drawString("LOADING", 20, 40);
		bi.getGraphics().drawString("ERROR", 20, 60);

		return bi;
	}

}
