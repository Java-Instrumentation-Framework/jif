package edu.mbl.jif.imaging.api;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 *
 * @author GBH
 */
public interface SeriesOfImages {
	
	String getFilename();

	BufferedImage getImage(int n);

	BufferedImage getImage(int channel, int zSections, int timePoints);
	
	BufferedImage getAsThumbnail(int n, int sampling);

	Dimension getImageDimensions();

	int getNumImages();

	int getChannels();
	
	int getTimeIntervals();

	int getZSections();
	
}
