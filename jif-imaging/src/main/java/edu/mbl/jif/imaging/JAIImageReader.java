package edu.mbl.jif.imaging;

/*
 * @(#)JAIImageReader.java	13.2 02/05/08
 * Copyright (c) 2002 Sun Microsystems, Inc. All Rights Reserved.
 */
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;

import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

public class JAIImageReader {

  public static PlanarImage readImage(String filename) {
    PlanarImage image = null;
    // Use the JAI API unless JAI_IMAGE_READER_USE_CODECS is set
    if (System.getProperty("JAI_IMAGE_READER_USE_CODECS") == null) {
      image = JAI.create("fileload", filename);
    } else {
      try {
        // Use the ImageCodec APIs
        SeekableStream stream = new FileSeekableStream(filename);
        String[] names = ImageCodec.getDecoderNames(stream);
        ImageDecoder dec =
            ImageCodec.createImageDecoder(names[0], stream, null);
        RenderedImage im = dec.decodeAsRenderedImage();
        image = PlanarImage.wrapRenderedImage(im);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
    // If the source image is colormapped, convert it to 3-band RGB.
    if (image.getColorModel()instanceof IndexColorModel) {
      // Retrieve the IndexColorModel
      IndexColorModel icm = (IndexColorModel) image.getColorModel();
      // Cache the number of elements in each band of the colormap.
      int mapSize = icm.getMapSize();
      // Allocate an array for the lookup table data.
      byte[][] lutData = new byte[3][mapSize];
      // Load the lookup table data from the IndexColorModel.
      icm.getReds(lutData[0]);
      icm.getGreens(lutData[1]);
      icm.getBlues(lutData[2]);
      // Create the lookup table object.
      LookupTableJAI lut = new LookupTableJAI(lutData);
      // Replace the original image with the 3-band RGB image.
      image = JAI.create("lookup", image, lut);
    }
    return image;
  }
  
    public static void main(String[] args){
        String filename = "";
        JAIImageReader.readImage(filename);
    }
}
