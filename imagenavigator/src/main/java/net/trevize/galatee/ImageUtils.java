package net.trevize.galatee;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;


//import net.sourceforge.jiu.codecs.CodecMode;
//import net.sourceforge.jiu.codecs.PNMCodec;
//import net.sourceforge.jiu.codecs.UnsupportedCodecModeException;
//import net.sourceforge.jiu.data.MemoryRGB24Image;
//import net.sourceforge.jiu.data.PixelImage;
//import net.sourceforge.jiu.ops.MissingParameterException;
//import net.sourceforge.jiu.ops.OperationFailedException;
/**
 *
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]] ImageUtils.java -
 * May 26, 2009
 */
public class ImageUtils {

   public static final int RGB = 0x00FFFFFF;
   public static final int R = 0x00FF0000;
   public static final int G = 0x0000FF00;
   public static final int B = 0x000000FF;
   public static final int ALPHA = 0xFF000000;

   public static int getR(Integer r) {
      return (r & R) >> 16;
   }

   public static int setR(int pix, Integer r) {
      return (pix & ~R) | (r << 16 & R);
   }

   public static int getG(Integer g) {
      return (g & G) >> 8;
   }

   public static int setG(int pix, Integer g) {
      return (pix & ~G) | (g << 8 & G);
   }

   public static int getB(Integer b) {
      return (b & B);
   }

   public static int setB(int pix, Integer b) {
      return (pix & ~B) | (b & B);
   }

   public static int getAlphaChannel(Integer a) {
      return (a & ALPHA);
   }

   public static String rgb2String(Integer i) {
      return "(" + getR(i) + "," + getG(i) + "," + getB(i) + ")";
   }

   public static int[][] toGreyLevel(BufferedImage bi) {
      int[][] img = new int[bi.getWidth()][bi.getHeight()];
      for (int i = 0; i < bi.getWidth(); ++i) {
         for (int j = 0; j < bi.getHeight(); ++j) {
            //using CIE recommendation 601.
            img[i][j] = (int) (ImageUtils.getR(bi.getRGB(i, j)) * 0.299
                    + ImageUtils.getG(bi.getRGB(i, j)) * 0.587 + ImageUtils
                    .getB(bi.getRGB(i, j)) * 0.114);
            if (img[i][j] > 255) {
               img[i][j] = 255;
            } else if (img[i][j] < 0) {
               img[i][j] = 0;
            }
         }
      }
      return img;
   }

   /**
    * A static method for creating a BufferedImage object using the ImageIO.read(...) static method.
    * If the ImageIO.read(...) method throws an exception, the image is rewrite using ImageMagick
    * convert through a SystemCommandHandler2 object in the temporary directory of the Operating
    * System, when the method try to reload the image.
    *
    * @param path to the image to load
    * @return BufferedImage the created BufferedImage object
    */
   public static BufferedImage loadImage(String path) {
      BufferedImage bimg = null;
      try {
         bimg = ImageIO.read(new File(path));
      } catch (Exception e) {
         e.printStackTrace();

         File tmp_image_file = null;
         try {
            tmp_image_file = File.createTempFile(""
                    + Calendar.getInstance().getTimeInMillis(), ".tmp");
         } catch (IOException e2) {
            e2.printStackTrace();
         }

         SystemCommandHandler2 sch = new SystemCommandHandler2();

         try {
            String command = "convert "
                    + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
                    + path
                    + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
                    + " "
                    + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
                    + tmp_image_file.getCanonicalPath()
                    + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER;
            String[] commands = new String[1];
            commands[0] = command;
            sch.exec(commands);
         } catch (IOException e1) {
            e1.printStackTrace();
         }

         try {
            bimg = ImageIO.read(tmp_image_file);
         } catch (IOException e1) {
            e1.printStackTrace();
            System.err.println("Unable to load the image " + path);
         }
      }
      return bimg;
   }

   /**
    * A static method for creating a BufferedImage object using the JAI library. static method. If
    * the JAI.create(...) method throws an exception, the image is rewrite using ImageMagick convert
    * through a SystemCommandHandler2 object in the temporary directory of the Operating System,
    * when the method try to reload the image.
    *
    * @param path to the image to load
    * @return BufferedImage the created BufferedImage object
    */
   public static BufferedImage JAI_loadImage(String path) {
      BufferedImage res = null;
      try {
         PlanarImage img = (PlanarImage) JAI.create("fileload", path);
         res = img.getAsBufferedImage();
         //free memory.
         img.dispose();
      } catch (Exception e) {
         System.out.println("error when loading image " + path);
         e.printStackTrace();

         /*
          * Maybe the exception is due to: 
          * Corrupt JPEG data: premature end of data segment
          * 
          * So, a solution is to use a tmp file that is the rewritten
          * image using the convert tool of ImageMagick.
          */

         //create the temporary file.
         File tmp_image_file = null;
         try {
            tmp_image_file = File.createTempFile(""
                    + Calendar.getInstance().getTimeInMillis(), ".tmp");
         } catch (IOException e1) {
            e1.printStackTrace();
         }

         SystemCommandHandler2 sch = new SystemCommandHandler2();

         try {
            String command = "convert "
                    + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
                    + path
                    + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
                    + " "
                    + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
                    + tmp_image_file.getCanonicalPath()
                    + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER;
            String[] commands = new String[1];
            commands[0] = command;
            sch.exec(commands);
         } catch (IOException e1) {
            e1.printStackTrace();
         }

         //try again to load the image.
         try {
            PlanarImage img = (PlanarImage) JAI.create("fileload",
                    tmp_image_file.getPath());
            res = img.getAsBufferedImage();
            //free memory.
            img.dispose();
         } catch (Exception e1) {
            System.out.println("Unable to load the image " + path);
            e1.printStackTrace();
         }
      }

      return res;
   }

   /**
    * A static method to write a BufferedImage in a PNG file.
    *
    * @param img the BufferedImage object
    * @param path the path of the file to create
    */
   public static void writeBufferedImage(BufferedImage img, String path) {
      File f = new File(path);
      try {
         ImageIO.write(img, "png", f);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

//	public static BufferedImage loadPPM(String path) {
//		// load the image in the PPM format using the JIU library
//		PixelImage pi = null;
//
//		try {
//			PNMCodec codec = new PNMCodec();
//			codec.setFile(path, CodecMode.LOAD);
//			codec.process();
//			codec.close();
//			pi = codec.getImage();
//		} catch (UnsupportedCodecModeException e) {
//			e.printStackTrace();
//		} catch (MissingParameterException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (OperationFailedException e) {
//			e.printStackTrace();
//		}
//
//		MemoryRGB24Image mi = ((MemoryRGB24Image) pi);
//
//		int width = mi.getWidth();
//		int height = mi.getHeight();
//
//		BufferedImage img = new BufferedImage(width, height,
//				BufferedImage.TYPE_INT_ARGB);
//
//		//copy the ppm image in a BufferedImage
//		for (int i = 0; i < width; ++i) {
//			for (int j = 0; j < height; ++j) {
//
//				//retrieve the rgb value in the ppm
//				int r = mi.getSample(0, i, j);
//				r <<= 16;
//				int g = mi.getSample(1, i, j);
//				g <<= 8;
//				int b = mi.getSample(2, i, j);
//				int rgb = ImageUtils.ALPHA | r | g | b;
//
//				//update the BufferedImage pixel value
//				img.setRGB(i, j, rgb);
//			}
//		}
//
//		return img;
//	}
   public static Dimension getImageSize(String path) {
      BufferedImage bi = JAI_loadImage(path);

      /*
       * if there is a problem while loading the image we return null.
       */
      if (bi == null) {
         return null;
      }

      Dimension res = new Dimension(bi.getWidth(), bi.getHeight());
      bi = null;
      return res;
   }

   /**
    * get the size of a scaled image in keeping the with/height ratio and considering a maximum with
    * and a maximum height.
    *
    * @param width
    * @param height
    * @param maxWidth
    * @param maxHeight
    * @return
    */
   public static Dimension getScaledSize(int width, int height, int maxWidth,
           int maxHeight) {
      Dimension imageDim = new Dimension();

      if (width <= maxWidth && height <= maxHeight) {
         imageDim.width = width;
         imageDim.height = height;
      } else {
         float scale_x = (float) maxWidth / (float) width;
         float scale_y = (float) maxHeight / (float) height;

         if (scale_x < scale_y) {
            imageDim.width = (int) (width * maxWidth / width);
            imageDim.height = (int) (height * maxWidth / width);
         } else {
            imageDim.width = (int) (width * maxHeight / height);
            imageDim.height = (int) (height * maxHeight / height);
         }

      }

      return imageDim;
   }

   public static BufferedImage resizeImage(int maxWidth, int maxHeight,
           BufferedImage bi, boolean keepRatio) {
      int width = bi.getWidth();
      int height = bi.getHeight();
      Dimension imageDim;

      if (keepRatio) {
         imageDim = getScaledSize(width, height, maxWidth, maxHeight);
      } else { //don't keep ratio.
         imageDim = new Dimension(maxWidth, maxHeight);
      }

      BufferedImage res = new BufferedImage(imageDim.width, imageDim.height,
              BufferedImage.TYPE_INT_ARGB);

      Graphics g = res.createGraphics();

      g.drawImage(bi.getScaledInstance(imageDim.width, imageDim.height,
              Image.SCALE_SMOOTH), 0, 0, null);

      g.dispose();

      return res;
   }

   public static BufferedImage JAI_loadAndResizeImage(int maxWidth,
           int maxHeight, String path, boolean keepRatio) {
      PlanarImage img = (PlanarImage) JAI.create("fileload", path);

      ParameterBlockJAI pb = new ParameterBlockJAI("scale");
      pb.addSource(img);

      Dimension scaled_size = null;
      try {
         scaled_size = getScaledSize(img.getWidth(), img.getHeight(),
                 maxWidth, maxHeight);
      } catch (Exception e) {
         System.out.println("Unable to load image: " + path);
         System.out.println(e.getMessage());
         e.printStackTrace();
         return null;
      }

      pb.setParameter("xScale", (float) scaled_size.width
              / (float) img.getWidth()); //x Scale Factor
      pb.setParameter("yScale", (float) scaled_size.height
              / (float) img.getHeight()); //y Scale Factor
      pb.setParameter("xTrans", 0.0F); //x Translate amount
      pb.setParameter("yTrans", 0.0F); //y Translate amount
      pb.setParameter("interpolation", new InterpolationNearest());

      PlanarImage resized_img = JAI.create("scale", pb, null);
      BufferedImage res = null;
      try {
         res = resized_img.getAsBufferedImage();
      } catch (Exception e) {
         System.out.println("JAI: Could not find mediaLib accelerator, continuing in pure Java mode.");
      }

      //free memory.
      img.dispose();
      resized_img.dispose();

      return res;
   }

   public static BufferedImage JAI_loadAndResizeImage(int maxWidth,
           int maxHeight, InputStream is, boolean keepRatio) {
      PlanarImage img = (PlanarImage) JAI.create("stream", is);

      ParameterBlockJAI pb = new ParameterBlockJAI("scale");
      pb.addSource(img);

      Dimension scaled_size = null;
      try {
         scaled_size = getScaledSize(img.getWidth(), img.getHeight(),
                 maxWidth, maxHeight);
      } catch (Exception e) {
         System.out.println("Unable to load image from input stream");
         System.out.println(e.getMessage());
         e.printStackTrace();
         return null;
      }
      pb.setParameter("xScale", (float) scaled_size.width / (float) img.getWidth()); //x Scale Factor
      pb.setParameter("yScale", (float) scaled_size.height / (float) img.getHeight()); //y Scale Factor
      pb.setParameter("xTrans", 0.0F); //x Translate amount
      pb.setParameter("yTrans", 0.0F); //y Translate amount
      pb.setParameter("interpolation", new InterpolationNearest());
      PlanarImage resized_img = JAI.create("scale", pb, null);
      BufferedImage res = resized_img.getAsBufferedImage();
      //free memory.
      img.dispose();
      resized_img.dispose();

      return res;
   }

   public static void ImageMagick_resizeImage(String source,
           String destination, int new_width, int new_height) {
      SystemCommandHandler2 sch = new SystemCommandHandler2();
      StringBuffer command = new StringBuffer();
      command.append("convert ");
      command.append("-resize " + new_width + "x" + new_height + " ");
      command
              .append(SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
              + source
              + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER);
      command.append(" ");
      command.append(SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
              + destination
              + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER);
      String[] commands = new String[1];
      commands[0] = command.toString();
      sch.exec(commands);
   }

   public static void ImageMagick_convertImage(String source,
           String destination) {
      SystemCommandHandler2 sch = new SystemCommandHandler2();
      StringBuffer command = new StringBuffer();
      command.append("convert ");
      command
              .append(SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
              + source
              + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER);
      command.append(" ");
      command.append(SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
              + destination
              + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER);
      String[] commands = new String[1];
      commands[0] = command.toString();
      sch.exec(commands);
   }

   public static void ImageMagick_convertImage(String source,
           String destination, String color_space) {
      SystemCommandHandler2 sch = new SystemCommandHandler2();
      StringBuffer command = new StringBuffer();
      command.append("convert ");
      command.append("-colorspace " + color_space + " ");
      command
              .append(SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
              + source
              + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER);
      command.append(" ");
      command.append(SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER
              + destination
              + SystemCommandHandler2.ESCAPED_DOUBLE_QUOTE_CHARACTER);
      String[] commands = new String[1];
      commands[0] = command.toString();
      sch.exec(commands);
   }
}
