package edu.mbl.jif.imaging.series;

import edu.mbl.jif.imaging.api.SeriesFileListener;
import edu.mbl.jif.imaging.api.SeriesOfImages;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import java.awt.*;
import java.awt.image.*;



/**
 * <p>SeriesOfImagesMultipageTiff: </p> <p>Description: Contains a series of images </p> <p>Copyright: Copyright (c)
 * 2004</p> <p>Company: </p>
 *
 * @author not attributable Represents a series of images Using MultipageTiffFile for viewing
 *
 * @version 1.0
 */
// multiple Positions mean multiple SeriesOfImages
// SeriesOfImages[]
// 
public class SeriesOfImagesMultipageTiff implements SeriesOfImages, SeriesFileListener  {

	private String seriesFile;
	private MultipageTiffFile tif;
	private int numImages; // total, should = nC * nZ * nT;
	private Dimension imgDim;
	// Sequence, time-series
	private int nT; // number of time intervals
	private int nC=1; // number of Channels
	private int nZ; // number of Z sections


	/* TODO
	 * Make the image storage general...
	 * MultipageTiffFile
	 * Set of files in a directory or set of directories
	 * VirtualStack... Hyperstack
	 * 
	 * TODO - Add Channels !!!
	 */
	public SeriesOfImagesMultipageTiff(String seriesFile) {
		// a time series, default interval one 'unit'
		this(seriesFile, 1);
	}

	// Time - Z-section Series
	public SeriesOfImagesMultipageTiff(String seriesFile, int zSections) {
		// a ZT series
		this(seriesFile, 1, zSections);
	}

	public SeriesOfImagesMultipageTiff(String seriesFile, int channels, int zSections) {
		this.seriesFile = seriesFile;
		this.nZ = zSections;
		this.nC = channels;
		try {
			tif = new MultipageTiffFile(this.seriesFile);
			if (tif != null) {
				imgDim = new Dimension(tif.getWidth(0), tif.getHeight(0));
				numImages = tif.getNumImages();
			}
			if (zSections > 1) {
				if (numImages % zSections != 0) {
					// Warning: 
				}
				nT = numImages / zSections / channels;
			} else {
				nT = numImages;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
	}

	@Override
	public int getNumImages() {
		return numImages;
	}

	@Override
	public int getZSections() {
		return nZ;
	}

	@Override
	public int getTimeIntervals() {
		return nT;
	}
	
	@Override
	public int getChannels() {
		return nC;
	}
	
//	@Override
//	public double getInterval() {
//		return interval;
//	}

	@Override
	public String getFilename() {
		return seriesFile;
	}

	@Override
	public Dimension getImageDimensions() {
		return imgDim;
	}

	@Override
	public BufferedImage getImage(int n) {
		if (n < numImages) {
			return tif.getImage(n);
		} else {
			return null;
		}
	}

	@Override
	public BufferedImage getImage(int c, int z, int t) {
		int n = c + (z * nC) + (t * nZ * nC);
		return getImage(n);
	}

	public BufferedImage getAsThumbnail(int n, int sample) {
		if (n < numImages) {
			return tif.getAsThumbnail(n, sample);
		} else {
			return null;
		}
	}

	public void close() {
		try {
			if (tif != null) {
				tif.close();
			}
		} catch (Exception e) {
		}
		tif = null;
	}

	/**
	 * @todo add functions
	 */
	@Override
	public int imageAdded() {
		boolean onLastSlice = true;
		tif.closeRead();
		tif.openRead(getFilename());
		numImages = tif.getNumImages();
		if (onLastSlice) {
			//playCtrl.gotoSlice(numImages - 1);
		}
		// tif.getImage(i);
		return numImages;
	}

}
