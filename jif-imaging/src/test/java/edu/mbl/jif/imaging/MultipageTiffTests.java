package edu.mbl.jif.imaging;

import com.sun.media.imageio.plugins.tiff.BaselineTIFFTagSet;
import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import edu.mbl.jif.imaging.meta.TiffMetadata;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * 
 * @author GBH
 */
public class MultipageTiffTests {
	
	
    // >>>> Testing
    public static void main(String[] args)
      {
        //ImagingUtils.listReadersWriters();

        // OME test Image:
        //"D:\\_TestImages\\OME-TIFF Test Images\\tubhiswt_C1.tiff""
        //"D:\\_TestImages\\meta\\xyzt-200x200x10x15.tif"
//        String imageFile = //edu.mbl.jif.Constants.testDataPath +
//            "meta/ome.tif";

        //testGetImageInfo(imageFile);

        testFastWrite();

        if (false) {
//            testThumbSample(3);
//            testThumbSample(4);
//            testThumbSample(5);
        }

        // testWriteMultiple();


        if (false) {
            BufferedImage img = ImageFactoryGrayScale.testImageByte();
            MultipageTiffFile.appendImageToTiffFile(img, "byterooski");
        //appendImageToTiffFile(ImageFactoryGrayScale.testImageByte(), "byteType");
        //appendImageToTiffFile(ImageFactoryGrayScale.testImageFloat(), "floatType");
        }
      }
		
		// Test SequenceReadIncremental -------------------------
//    static void testSequenceReadIncremental() {
//        // Test: read sequence incrementally
//        JFrame f = new JFrame();
//        psj.Image.ImagePanel ip = new psj.Image.ImagePanel();
//        f.getContentPane().add(ip, BorderLayout.CENTER);
//        f.setSize(new Dimension(649, 540));
//        f.setVisible(true);
//        ArrayList img2 = new ArrayList();
//        MultipageTiffFile tif2 = new MultipageTiffFile( //"out.tif");
//                edu.mbl.jif.Constants.testDataPath + "ps/STMPS_04_0621_1451_54_Copy.tif");
//        int n = tif2.getNumImages();
//        ip.setPreferredSize(new Dimension(tif2.getWidth(0), tif2.getHeight(0)));
//        for (int k = 0; k < 10; k++) {
//            for (int i = 0; i < n; i++) {
//                //img2.add(tif2.getImage(i));
//                ip.setImage(tif2.getImage(i));
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException ex) {
//                }
//            }
//            for (int i = n - 1; i > 0; i--) {
//                //img2.add(tif2.getImage(i));
//                ip.setImage(tif2.getImage(i));
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException ex) {
//                }
//            }
//        }
//        tif2.close();
//    }
    static void testWriteMultiple()
      {
        //testWriteSeries() {
        ArrayList imgs = new ArrayList();
        imgs = MultipageTiffFile.loadImageArrayList("PolStackTest.tif");
        // new FrameImageDisplayTabbed(imgs);
        MultipageTiffFile.saveImageArrayList(imgs, "out.tif", false);
        // Open, append images to a sequence, then close.

        for (Iterator iter = imgs.iterator(); iter.hasNext();) {
            MultipageTiffFile tif = new MultipageTiffFile("outSeq.tif", false);
            Object item = (Object) iter.next();
            tif.appendImage((BufferedImage) item);
            System.out.println("ImgOut");
            tif.close();
        }
//        MultipageTiffFile tif = new MultipageTiffFile("outSeq.tif", false);
//        for (Iterator iter = imgs.iterator(); iter.hasNext();) {
//            Object item = (Object) iter.next();
//            tif.appendImage((BufferedImage) item);
//            System.out.println("ImgOut");
//        }
//        tif.close();
      }
		
		//
    // Test METADATA ------------------------------------------------------
    static void testWriteMeta()
      {
        MultipageTiffFile tif = new MultipageTiffFile("PolStackTest.tif");
        int n = tif.getNumImages();
        System.out.println("n: " + n);
        TiffMetadata ts = new TiffMetadata(tif.getImageMetadata(0));
        int bps = ts.getNumericTag(BaselineTIFFTagSet.TAG_BITS_PER_SAMPLE);
        System.out.println("bps: " + bps);
        edu.mbl.jif.imaging.meta.IIOMetadataDisplay.displayIIOMetadataNative(tif.getImageMetadata(0));
        edu.mbl.jif.imaging.meta.IIOMetadataDisplay.displayIIOMetadataNative(tif.getStreamMetadata());

        //testWriteSeries() {
        //      ArrayList imgs = new ArrayList();
        //      imgs = MultipageTiffFile.loadImageArrayList("PS_JulyTest.tif");
        //      new FrameImageDisplayTabbed(imgs);
        //      MultipageTiffFile.saveImageArrayList(imgs, "out.tif");
        //      //Open, append images to a sequence, then close.
        //      MultipageTiffFile tif = new MultipageTiffFile("outSeq.tif");
        //      for (Iterator iter = imgs.iterator(); iter.hasNext(); ) {
        //         Object item = (Object) iter.next();
        //         IIOMetadata meta = tif.modifyMetadata((BufferedImage) item);
        //
        //         //IIOMetadata meta = tif.createMetadata((BufferedImage) item);
        ////         tif.appendImage((BufferedImage) item, null, meta);
        //         System.out.println("ImgOut");
        //      }
        tif.close();
      }

		//
		// Test Thumbnailing------------------------------------
//    public static void testThumbSample(int sample) {
//        MultipageTiffFile mf = new MultipageTiffFile( //"out.tif");
//                edu.mbl.jif.Constants.testDataPath + "ps/_PS_03_0825_1751_01.tiff");
//        int numPages = mf.getNumImages();
//        
//        //BufferedImage bImage = mf.getImage(0);
//        BufferedImage thumb = mf.getAsThumbnail(0, sample);
//        JFrame f = new JFrame();
//        psj.Image.ImagePanel ip = new psj.Image.ImagePanel();
//        f.getContentPane().add(ip, BorderLayout.CENTER);
//        ip.setImage(thumb);
//        f.setSize(new Dimension(649, 540));
//        f.setVisible(true);
//        mf.close();
//    }
		
		public static void testFastWrite() {
			String filename = "out.tif";
			int width = 2048;
			int height = 2048;
			int n = 450;
			BufferedImage image = 
				image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			MultipageTiffFile mf = new MultipageTiffFile(filename);
			long start = System.currentTimeMillis();	
			for (int i = 0; i < n; i++) {
				mf.appendImage(image);
			}
			long end = System.currentTimeMillis();
			long dur = end - start;
			System.out.println(n + " images in " + dur + " msec.");
			mf.close();
			File f = new File(filename);
			long fs = f.length();
			System.out.println("filesize = " + fs);
			System.out.println(fs/dur + " K/s");
	}
	
	
}
