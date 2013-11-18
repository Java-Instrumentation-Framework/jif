/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.nav.util;

import edu.mbl.jif.imaging.nav.ZoomWindow;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A Histogram represents the histogram of a BufferedImage object. The histogram of an image shows
 * the amount of pixels of each possible value, which is 0-255 for an BufferedImage.
 *
 * Besides calculating and drawing the histogram of an image, an Histogram object may also be used
 * to make a histogram equalized version of the image. Histogram equalization increases the contrast
 * of the image.
 *
 */
public class Histogram {

   private BufferedImage image;
   public HPanel histpanel;

   int displayRange16bit = 65535;
   /**
    * Constructs a Histogram for an image.
    */
   public Histogram(BufferedImage image) {
      setImage(image);
      histpanel = new HPanel();
   }

   /**
    * Constructs a Histogram for an array of pixel data. The array must contain intensity values
    * between 0 and 255. data[y][x] is the intensity at (x, y) in the image.
    */
   public Histogram(int[][] data) {
      BufferedImage image = CreateImagefromIntArray(data);
      setImage(image);
   }

   /**
    * Returns the image this Histogram represents.
    */
   public BufferedImage getImage() {
      return image;
   }

   /**
    * Sets the image this Histogram represents.
    */
   public void setImage(BufferedImage image) {
      this.image = image;
   }

   public int[][] getPixels(BufferedImage image) {
      int iw = image.getWidth(null);
      int ih = image.getHeight(null);
      int[][] pixels = new int[iw][ih];

      /*DataBufferByte db = (DataBufferByte)image.getRaster().getDataBuffer();
       byte[] pixelarray = db.getData();
        
       for (int x = 0; x < pixels.length; x++){
       for (int y = 0; y < pixels[0].length; y++){
       pixels[x][y] = (int) pixelarray[y + x * pixels.length] &0xFF;
       }
       } */
      if (image.getType() == 10) {
         DataBufferByte db = (DataBufferByte) image.getRaster().getDataBuffer();
         byte[] pixelarray = db.getData();
         //System.out.println(pix[0].length * pix.length);
         //System.out.println(pixelarray.length);
         for (int x = 0; x < iw; x++) {
            for (int y = 0; y < ih; y++) {
               pixels[x][y] = pixelarray[x + y * iw] & 0xFF;
            }
         }
      } else if (image.getType() == 11) {
         DataBufferUShort db = (DataBufferUShort) image.getRaster().getDataBuffer();
         short[] pixelarray = db.getData();
         for (int x = 0; x < iw; x++) {
            for (int y = 0; y < ih; y++) {
               pixels[x][y] = pixelarray[x + y * iw] & 0xFFFF;
            }
         }
      } else {
         System.err.println("Image Type incorrect.");
      }
      return pixels;
   }

   /**
    * Calculates and returns the histogram for the image. The histogram is represented by an int
    * array of 256 elements. Each element gives the number of pixels in the image of the value equal
    * to the index of the element.
    *
    */
   public int[] getHistogram() {
      int iw = image.getWidth(null);
      int ih = image.getHeight(null);
          
      int[] histogram;
      if (image.getType() == 10) {
         histogram = new int[256];
      } else {
         histogram = new int[displayRange16bit+1];
      }
      int[][] pixels = getPixels(image);
      //find the histogram
      try {
         for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
               histogram[pixels[x][y]]++;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return histogram;
   }

   /**
    * Calculates and returns the normalized histogram for the image. The normalized histogram is
    * represented by an array of 256 doubles. Each element gives the amount of pixels in the image
    * of the value equal to the index of the element. The amount given is relative to the total
    * number of pixels in the image, so that the sum of the normalized histogram is 1.
    *
    * @return The normalized histogram.
    */
   public double[] getNormHistogram() {
      int[] histogram = getHistogram();
      double[] normHistogram;
      //The sum of the histogram equals the number of pixels in the image
      int sum = image.getHeight() * image.getWidth();
      if (image.getType() == 10) {
         normHistogram = new double[256];
         //Find the normalized histogram by dividing each component of the histogram
         //by sum
         for (int n = 0; n < 256; n++) {
            normHistogram[n] = (double) histogram[n] / sum;
         }
      } else {
         normHistogram = new double[displayRange16bit+1];
         //Find the normalized histogram by dividing each component of the histogram
         //by sum
         for (int n = 0; n < displayRange16bit; n++) {
            normHistogram[n] = (double) histogram[n] / sum;
         }
      }
      return normHistogram;
   }

   /**
    * Draws the histogram of the image.
    *
    * @param g The Graphics object to draw the histogram on.
    * @param x The x coordinate of the histogram.
    * @param y The y coordinate of the histogram.
    * @param height The heigth of the histogram.
    * @param space The space between the bars in the histogram.
    */
   public void draw(Graphics g, int x, int y, int height, int space) {
   }

   /**
    * Does histogram equalization on the image and returns the result as an new image. Histogram
    * equalization increases the contrast of the image.
    */
   public BufferedImage makeHistEqualizedImg() {
      double[] normHistogram = getNormHistogram();

      int[][] data = getPixels(image);
      int[] sum;
      double s = 0;
      if (image.getType() == 11) {
         sum = new int[displayRange16bit+1];
         for (int v = 0; v < displayRange16bit; v++) {
            s += normHistogram[v];
            sum[v] = (int) (s * 255 + 0.5);
         }
      } else {
         sum = new int[256];
         for (int v = 0; v < 256; v++) {
            s += normHistogram[v];
            sum[v] = (int) (s * 255 + 0.5);
         }
      }
      int[][] data2 = data;
      for (int x = 0; x < data.length; x++) {
         for (int y = 0; y < data[0].length; y++) {
            data2[x][y] = sum[data[x][y]];
         }
      }
      BufferedImage image = CreateImagefromIntArray(data2);
      return image;
   }

   /**
    * Does histogram equalization on the image. Histogram equalization increases the contrast of the
    * image.
    */
   public void equalizeHistogram() {
      setImage(makeHistEqualizedImg());
   }

   /**
    * subclass to paint the histogram in a panel
    */
   public class HPanel extends JPanel {

      public HPanel() {
         setPreferredSize(new Dimension(356, 200));
         setBackground(new Color(241, 244, 180));
         setBorder(BorderFactory.createLineBorder(Color.black));

      }

      public void paintComponent(Graphics g) {
         super.paintComponent(g);

         double[] normHistogram = getNormHistogram();
         int x, y, height, space;
         height = this.getHeight() * 3 / 4;
         x = 20;
         y = 20;
         space = 0;
         int len;
         if (image.getType() == 10) {
            len = 255;
         } else {
            len = displayRange16bit;
         }
         //find max value in histogram
         double max = 0;
         for (int j = 0; j < len; j++) {
            if (normHistogram[j] > max) {
               max = normHistogram[j];
            }
         }

         g.setFont(new Font("SansSerif", Font.PLAIN, 11));

         //draw vertical axis
         g.drawString("" + ((double) ((int) (max * 100)) / 10) + "%", x, y + 7);
         x += 30;
         g.drawLine(x - 2, y + height + 2, x - 2, y);
         g.drawLine(x - 2, y, x - 4, y);

         //draw horizontal axis
         g.drawLine(x - 2, y + height, x + (space + 1) * 257 - 1, y + height);
         g.drawLine(x + (space + 1) * 256, y + height + 2, x + (space + 1) * 256, y + height);
         g.drawString(Integer.toString(len), x + (space + 1) * 256 - 8, y + height + 11);
         g.drawLine(x + (space + 1) * 128, y + height + 2, x + (space + 1) * 128, y + height);
         g.drawString(Integer.toString(len / 2), x + (space + 1) * 128 - 8, y + height + 11);

         //draw bars
         Color shade = new Color(10, 10, 180);
         g.setColor(shade);
         int i, j = 0;
         if (len == 255) {
            for (i = 0; i < len; i++) {
               g.drawLine(x + (i + 1) * (space + 1), y + height, x + (i + 1) * (space + 1),
                       y + (int) ((height - (normHistogram[i] / max) * height)));
            }
         } else {
            for (i = 0; i < len; i++) //j = (int) (i/Math.pow(2,8));
            {
               g.drawLine(x + (int) (i * 0.1275 + 1) * (space + 1), y + height, x + (int) (i * 0.1275 + 1) * (space + 1),
                       y + (int) ((height - (normHistogram[i] / max) * height)));
            }

         }
      }
   }

   /**
    * Creates an imnage from a 2D array of integers
    */
   public static BufferedImage CreateImagefromIntArray(int[][] pixels) {

      int i, j;
      i = 0;
      j = 0;

      short[] pixelshortArray = new short[pixels.length * pixels[0].length];
      //int[] pixelintArray = new int[pixels.length * pixels[0].length];
      int min = getMin(pixels);
      int max = getMax(pixels);
      int gray;
      // System.out.println("rescaling output image: ");
      for (int x = 0; x < pixels.length; x++) {
         for (int y = 0; y < pixels[0].length; y++) {
            gray = pixels[x][y];
            if (gray < 0) {
               pixelshortArray[x + y * pixels.length] = 0;
            } else {
               pixelshortArray[x + y * pixels.length] = (short) gray;
            }
         }
      }
      // returns an 8-bit buffered image for display purposes
      BufferedImage img = getGrayBufferedImage(pixelshortArray, pixels.length,
              pixels[0].length, 0, 255);
      return img;
   }

   /**
    * method to find the minimum value in a 1D array of doubles
    */
   public static double getMin(double data[]) {

      double min = data[0];
      for (int i = 0; i < data.length; i++) {
         if (data[i] < min) {
            min = data[i];
         }
      }
      return min;
   }

   /**
    * method to find the maximum value in a 1D array of doubles
    */
   public static double getMax(double data[]) {

      double max = data[0];
      for (int i = 0; i < data.length; i++) {
         if (data[i] > max) {
            max = data[i];
         }
      }
      return max;
   }

   /**
    * method to find the minimum value in a 1D array of int
    */
   public static int getMin(int data[]) {

      int min = data[0];
      for (int i = 0; i < data.length; i++) {
         if (data[i] < min) {
            min = data[i];
         }
      }
      return min;
   }

   /**
    * method to find the maximum value in a 1D array of integers
    */
   public static int getMax(int data[]) {

      int max = data[0];
      for (int i = 0; i < data.length; i++) {
         if (data[i] > max) {
            max = data[i];
         }
      }
      return max;
   }

   /**
    * method to find the minimum value in a 2D array of integers
    */
   public static int getMin(int data[][]) {

      int min = data[0][0];
      for (int i = 0; i < data.length; i++) {
         for (int j = 0; j < data[0].length; j++) {
            if (data[i][j] < min) {
               min = data[i][j];
            }
         }
      }
      return min;
   }

   /**
    * method to find the maximum value in a 2D array of integers
    */
   public static int getMax(int data[][]) {

      int max = data[0][0];
      for (int i = 0; i < data.length; i++) {
         for (int j = 0; j < data[0].length; j++) {
            if (data[i][j] > max) {
               max = data[i][j];
            }
         }
      }
      return max;
   }

   /**
    * method to find the minimum value in a 2D array of doubles
    */
   public static double getMin(double data[][]) {

      double min = data[0][0];
      for (int i = 0; i < data.length; i++) {
         for (int j = 0; j < data[0].length; j++) {
            if (data[i][j] < min) {
               min = data[i][j];
            }
         }//System.out.println(min);
      }
      return min;
   }

   /**
    * method to find the maximum value in a 2D array of doubles
    */
   public static double getMax(double data[][]) {

      double max = data[0][0];
      for (int i = 0; i < data.length; i++) {
         for (int j = 0; j < data[0].length; j++) {
            if (data[i][j] > max) {
               max = data[i][j];
            }
         } //System.out.println(max);
      }
      return max;
   }

   public static BufferedImage getGrayBufferedImage(short[] buf, int width, int height, int imgMin, int imgMax) {
      if (buf.length != width * height) {
         throw new IllegalArgumentException(width + " * " + height + " != " + buf.length);
      }
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
      byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
      toGrayBytes(data, buf, imgMin, imgMax);
      return image;
   }

   public static void toGrayBytes(byte[] grayBytes, short[] shortBuffer, int imgMin, int imgMax) {
      final int displayMin = 0; // Black is zero
      final int displayMax = 255; //(2 << 7) - 1; // For 8 bits per sample (255)
      final float displayRatio = (float) (displayMax - displayMin) / (imgMax - imgMin);

      for (int i = 0; i < shortBuffer.length; ++i) {
         int in = shortBuffer[i];
         int out;
         if (in < imgMin) {
            out = displayMin;
         } else if (in > imgMax) {
            out = displayMax;
         } else {
            out = (int) ((in - imgMin) * displayRatio);
         }
         grayBytes[i] = (byte) out;
      }
   }

   public static void main(String[] args) {
      String imagePath = "C:/MicroManagerData/shalins test data/000010.tif";
      try {
         BufferedImage image = ImageIO.read(new File(imagePath));
         int type = image.getType();
         System.out.println("type = " + type);
         Histogram hist = new Histogram(image);
         
         hist.equalizeHistogram();
         BufferedImage out = hist.getImage();
         ZoomWindow zoomwin = new ZoomWindow("ZoomWindow Example", 3);
         zoomwin.setImage(out);
         zoomwin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         zoomwin.setVisible(true);

      } catch (IOException ex) {
         Logger.getLogger(Histogram.class.getName()).log(Level.SEVERE, null, ex);
      }

   }
}
