
package edu.mbl.jif.imaging;

import edu.mbl.jif.imaging.api.SeriesOfImages;
import java.awt.image.BufferedImage;

/**
 *
 * @author GBH
 */
/*
 * Implement as Dataset5DTiff for MultipageTiff, and Dataset5DMmgr for MicroManager dataset
 * 
 */
public interface Dataset5D extends SeriesOfImages {
	
	BufferedImage getImage(int channel, int zSections, int timePoints, int position);
	  
}
