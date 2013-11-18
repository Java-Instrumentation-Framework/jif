/*
 * ImageInspector.java
 *
 * Created on March 25, 2007, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.imaging;

import com.sun.media.imageio.plugins.tiff.BaselineTIFFTagSet;
import edu.mbl.jif.imaging.meta.TiffMetadata;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author GBH
 */
public class ImageInspector {
    static ArrayList imgs = new ArrayList();
    /** Creates a new instance of ImageInspector */
    public ImageInspector() {
    }
    
    public void inspect(String filename) {
        
    }
    
    static String testGetImageInfo(String file) {
        StringBuffer output = new StringBuffer();
        MultipageTiffFile tif = new MultipageTiffFile(file);
        output.append("File: " + file +"\n");
        int n = tif.getNumImages();
        output.append("Pages: " + n + "\n");
        BufferedImage img = (BufferedImage) tif.getImage(0);
        output.append(edu.mbl.jif.imaging.util.ImgInfoDumper.dump(img)+"/n/n");
        
        TiffMetadata ts = new TiffMetadata(tif.getImageMetadata(0));
        int bps = ts.getNumericTag(BaselineTIFFTagSet.TAG_BITS_PER_SAMPLE);
        System.out.println("bps: " + bps);
        System.out.println("=== Image Metadata ====================================================");
        edu.mbl.jif.imaging.meta.IIOMetadataDisplay.displayIIOMetadataNative(tif.getImageMetadata(0));
        System.out.println("=== Stream Metadata ===================================================");
        edu.mbl.jif.imaging.meta.IIOMetadataDisplay.displayIIOMetadataNative(tif.getStreamMetadata());
        return output.toString();
    }
    
  public static void main(String[] args) {
    testGetImageInfo("C:\\MicroManagerData\\TestDatasetGen\\magort_26\\magort_MMImages.ome.tif");
  }
   
}
