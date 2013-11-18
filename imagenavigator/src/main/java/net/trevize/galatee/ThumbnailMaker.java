/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.trevize.galatee;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author GBH
 */
public class ThumbnailMaker {
   
	/* (non-Javadoc)
	 * @see fr.cnrs.mri.thumbnail.IThumbnailCreator#createThumbnail(java.lang.String, java.lang.String, int, int)
	 */

	public void createThumbnail(String imagePath, String thumbnailPath,
			int width, int height) {
		try {
 			BufferedImage image = ImageIO.read(new File(imagePath));
			BufferedImage thumb = createThumbnail(image, width);
			ImageIO.write(thumb, "jpg", new File(thumbnailPath));
		} catch (IOException e) {
			//logger.warning(LoggingUtil.getMessageAndStackTrace(e));
		}

	}
	
	public static BufferedImage createThumbnail(BufferedImage image, int requestedThumbSize) {
        float ratio = (float) image.getWidth() / (float) image.getHeight();
        int width = image.getWidth();
        BufferedImage thumb = image;
        
        do {
            width /= 2;
            if (width < requestedThumbSize) {
                width = requestedThumbSize;
            }
            
            BufferedImage temp = new BufferedImage(width, (int) (width / ratio), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = temp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), null);
            g2.dispose();
 
            thumb = temp;
        } while (width != requestedThumbSize);
        
        return thumb;
    }

   
}
