package edu.mbl.jif.utils.diag;

import edu.mbl.jif.utils.diag.JWhich;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class IconUtils {

   public IconUtils() {
   }

   public static ImageIcon loadImageIcon(String imageFile, String path) {
      ResourceManager rm = new ResourceManager(path);
      return rm.getImageIcon(imageFile);
   }

   public static ImageIcon loadImageIcon(String gifFile, Class clazz) {
      ImageIcon img = null;
      URL url = null;
      try {
         url = JWhich.findClass(clazz.getName());
         String urlS = url.toString();
         int lastSlash = urlS.lastIndexOf("/");
         String path = urlS.substring(0, lastSlash) + "/icons/" + gifFile;
         System.out.println(path);
         img = new ImageIcon(path);
//            url = clazz.getResource("/icons/" + gifFile);
//            img = new ImageIcon(url);
      } catch (Exception e) {
         System.out.println("Exception loading: " + url);
      }
      if (img == null) {
         System.out.println("Could Not Load Image: " + gifFile + " for class: " + clazz.getName() + " from URL: " + url);
      }
      return img;
   }


   public static final class ResourceManager {

      String baseURL;

      public ResourceManager(String url) {
         baseURL = url + "/";
      }

      public Image getImage(String imageName) {
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         InputStream ins;
         BufferedImage bi;

         ins = cl.getResourceAsStream(baseURL + imageName);
         try {
            bi = ImageIO.read(ins);
         } catch (Exception e) {
            bi = null;
         }
         return bi;
      }

      public ImageIcon getImageIcon(String imageName) {
         Image i = getImage(imageName);
         if (i == null) {
            return null;
         } else {
            return new ImageIcon(getImage(imageName));
         }
      }
   }
}
