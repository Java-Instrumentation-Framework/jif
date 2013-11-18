package edu.mbl.jif.imaging.meta;

/*
 When working with single-band float data (i.e. data that does not
 represent pixels, but, say, precipitation), encoding an image and
 decoding it again does not work correctly. Looks to me like a byte
 order problem, at first glance. I'm using JAI ImageIO plugin for
 TIFF. Anybody who sees my mistake, or knows a workaround?
 ---------------------------------------------
 Andreas Jaeger
 http://afi.uni-muenster.de
 */


// FloatSampleTIFFTest.java

//import com.sun.media.imageio.plugins.tiff.*;

import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;
import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import javax.swing.*;


/**
 * Test illustrating problems when working with float samples and
 * the ImageIO Tools TIFFWriter.
 */
public class FloatSampleTIFFTest
{

   public static void main (String[] args) throws IOException {
      // Create the data simulation including noData values
      float[] data = new float[220 * 60];
      for (int i = 0; i < 220; i++) {
         for (int j = 0; j < 60; j++) {
            if (j + i > 250) {
               data[i + j * 220] = -9999.0f;
            } else {
               data[i + j * 220] = (i + j);
            }
         }
      }

      // Wrap the data as a displayable image
      SampleModel sm = new BandedSampleModel(DataBuffer.TYPE_FLOAT, 220, 60, 1);
      DataBuffer db = new DataBufferFloat(data, data.length);
      WritableRaster wr = Raster.createWritableRaster(sm, db, null);
      BufferedImage bi =
            new BufferedImage(new FloatCM( -9999.0f, 0.0f, 250.0f), wr, false, null);

      // Write out the image using ImageIO Tools TIFF encoder
      ImageWriter iw =
            (ImageWriter) ImageIO.getImageWritersByMIMEType("image/tiff").next();
      File tiffFile = new File(System.getProperty("java.io.tmpdir"), "floatSample.tif");
      System.out.println("Writing output to " + tiffFile.getAbsolutePath());
      ImageOutputStream ios = new MemoryCacheImageOutputStream(new FileOutputStream(
            tiffFile));
      iw.setOutput(ios);
      TIFFImageWriteParam iwp = (TIFFImageWriteParam) iw.getDefaultWriteParam();
//        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//        iwp.setCompressionType("Deflate");
      IIOMetadata imd = iw.getDefaultImageMetadata(new ImageTypeSpecifier(bi), iwp);
      iw.write(null, new IIOImage(bi, null, imd), iwp);
      ios.flush();
      ios.close();

      // Re-read image to check data integrity, using ImageIO Tools TIFF decoder
      ImageReader ir =
            (ImageReader) ImageIO.getImageReadersByMIMEType("image/tiff").next();
      ImageInputStream iis =
            new MemoryCacheImageInputStream(new FileInputStream(tiffFile));
      ir.setInput(iis);
      int w = ir.getWidth(0);
      int h = ir.getHeight(0);
      ImageReadParam irp = ir.getDefaultReadParam();
      SampleModel sm2 = new BandedSampleModel(DataBuffer.TYPE_FLOAT, w, h, 1);
      DataBuffer db2 = new DataBufferFloat(new float[w * h], w * h);
      WritableRaster wr2 = Raster.createWritableRaster(sm2, db2, null);
      BufferedImage bi2 =
            new BufferedImage(new FloatCM( -9999.0f, 0.0f, 250.0f), wr2, false, null);
      irp.setDestination(bi2);
      bi2 = ir.read(0, irp);
      iis.close();

      // Display both images
      JFrame jfr = new JFrame("Testing GeoTIFFs with Float Samples");
      jfr.getContentPane().setLayout(new GridLayout(1, 0));
      jfr.getContentPane().add(new JLabel(new ImageIcon(bi)));
      jfr.getContentPane().add(new JLabel(new ImageIcon(bi2)));
      jfr.pack();
      jfr.show();
      jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }


   /**
    * Dirty hack to support float sample data in images.
    */
   public static class FloatCM
         extends ColorModel
   {

      private float noData;
      private float min;
      private float max;
      private Color minColor;
      private Color maxColor;
      private Color noDataColor;

      public FloatCM (float noData, float min, float max) {
         this(noData, min, max, Color.YELLOW, Color.BLUE, Color.RED);
      }


      public FloatCM (float noData, float min, float max, Color minColor, Color maxColor,
            Color noDataColor) {
         super(32, new int[] {8, 8, 8, 8}, ColorSpace.getInstance(ColorSpace.CS_sRGB), true, false,
               Transparency.TRANSLUCENT, DataBuffer.TYPE_FLOAT);
         this.noData = noData;
         this.min = min;
         this.max = max;
         this.minColor = minColor;
         this.maxColor = maxColor;
         this.noDataColor = noDataColor;
      }


      public int getAlpha (int pixel) {
         return (pixel & 0xff000000) >> 24;
      }


      public int getRed (int pixel) {
         return (pixel & 0x00ff0000) >> 16;
      }


      public int getGreen (int pixel) {
         return (pixel & 0x0000ff00) >> 8;
      }


      public int getBlue (int pixel) {
         return (pixel & 0x000000ff);
      }


      public int getRGB (int pixel) {
         return pixel;
      }


      public int getAlpha (Object inData) {
         return getAlpha(getRGB(inData));
      }


      public int getRed (Object inData) {
         return getRed(getRGB(inData));
      }


      public int getGreen (Object inData) {
         return getGreen(getRGB(inData));
      }


      public int getBlue (Object inData) {
         return getBlue(getRGB(inData));
      }


      public int getRGB (Object inData) {
         float val = ((float[]) inData)[0];
         if (val == noData) {
            return noDataColor.getRGB();
         } else if (val <= min) {
            return minColor.getRGB();
         } else if (val >= max) {
            return maxColor.getRGB();
         } else {
            float fac = (val - min) / (max - min);
            int alpha = Math.round(minColor.getAlpha() + fac * (maxColor.getAlpha()
                  - minColor.getAlpha()));
            int red = Math.round(minColor.getRed() + fac * (maxColor.getRed()
                  - minColor.getRed()));
            int green = Math.round(minColor.getGreen() + fac * (maxColor.getGreen()
                  - minColor.getGreen()));
            int blue = Math.round(minColor.getBlue() + fac * (maxColor.getBlue()
                  - minColor.getBlue()));
            return ((alpha & 0xff) << 24) | ((red & 0xff) << 16) | ((green & 0xff) << 8)
                  | (blue & 0xff);
         }
      }


      public ColorModel coerceData (WritableRaster raster, boolean isAlphaPremultiplied) {
         if (!isAlphaPremultiplied) {
            return this;
         }
         return super.coerceData(raster, isAlphaPremultiplied);
      }


      public boolean isCompatibleRaster (Raster raster) {
         return isCompatibleSampleModel(raster.getSampleModel());
      }


      public boolean isCompatibleSampleModel (SampleModel sm) {
         return sm.getDataType() == DataBuffer.TYPE_FLOAT;
      }

   }

}
