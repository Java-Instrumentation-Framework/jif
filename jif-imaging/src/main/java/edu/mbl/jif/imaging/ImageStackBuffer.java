package edu.mbl.jif.imaging;

import  edu.mbl.jif.utils.DialogBoxI;
import  java.io.IOException;
import  java.awt.image.BufferedImage;

// ImagePageBuffer holds an array of images (pages) loaded from a
// TIFF file for display in ImageMgr

public class ImageStackBuffer {

  BufferedImage[] bImage;

  public ImageStackBuffer(String filename) {
    try {
	  bImage = TiffMultipage.readBufferedImageArrayFromTiff(filename);
	} catch (IOException ex) {
	  DialogBoxI.boxError("Cannot open/read file: " + filename,
						"File error: " + ex );
	}
  }

  public BufferedImage getSlice(int _page) {
	return bImage[_page];
  }

  public int getNumSlices(int page) {
	return bImage.length;
  }

}
