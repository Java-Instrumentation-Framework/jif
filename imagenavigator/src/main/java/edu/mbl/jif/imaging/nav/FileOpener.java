package edu.mbl.jif.imaging.nav;

import ij.IJ;
import java.io.File;

/**
 * This determines how to handle a given type of file or set of files.
 *
 * @author GBH
 */
public class FileOpener {

   // <editor-fold defaultstate="collapsed" desc=" === Image Opening =============================">
   /*
    * Open Images
    * If Micro-Manager type, using Micro-Manager, if available and open
    * else ImageJ, if available,
    * else ImageIO.
    * 
    * TODO Bundle ImageIO for Tiffs with OneJar...
    * TODO Fall-back image viewer?
    */
   //
   String defaultImageOpener = "edu.mbl.jif.imaging.nav.ImageOpenerIJ";

   public void setDefaultImageOpener(String defaultImageOpener) {
      this.defaultImageOpener = defaultImageOpener;
   }

   public void openFile(String localFilepath) {
      OpenImageThread oit = new OpenImageThread(localFilepath);
      oit.start();
   }

   class OpenImageThread extends Thread {

      private final String file;

      public OpenImageThread(String file) {
         this.file = file;
      }

      public void run() {
         openBasedOnType(file);
      }
   }

   public void openBasedOnType(String localFilepath) {
      File[] files = new File[]{new File(localFilepath)};
      // TODO: extension to application lookup

      // Open based on type...
      if (isMicroManagerType(localFilepath)) {
         try {
            // Open using Micro-Manager
            System.out.println("Is a Micro-Manager dataset.");
            openImagesUsing(files, "edu.mbl.jif.imaging.nav.mmgr.ImageOpenerMmgr");
         } catch (Exception ex) {
            System.out.println(".mmgr.ImageOpenerMmgr threw exception");
            try {
               openImagesUsing(files, defaultImageOpener);
               //openWithImageIO(files);
            } catch (Exception ex1) {
               System.err.println("Default opener failed (" + defaultImageOpener + ")");
            }
         }
      } else {
         // Open using default
         try {
            openImagesUsing(files, defaultImageOpener);
         } catch (Exception ex) {
            openWithImageIO(files);
         }
      }
   }

   public void openWithImageIO(final File[] files) {
      //ImageIO.createImageInputStream(files)
      //openImagesUsing(files, "edu.mbl.jif.imaging.nav.mmgr.ImageOpenerMmgr");
   }

   // Image Types...
   public static boolean isMicroManagerType(String localFilepath) {
      // Determine type of file/dataset
      File f = new File(localFilepath);
      boolean isMmgr = false;
      if (f.isFile()) {
         if (f.getName().contains("ome")) {
            return true;
         }
         File[] files = f.getParentFile().listFiles();
         for (File file : files) {
            if (file.getName().contains("metadata.txt")) {
               return true;
            }
         }
         return false;
      }
      if (f.isDirectory()) {

         File[] files = f.listFiles();
         for (File file : files) {
            if (file.getName().contains("metadata.txt")) {
               return true;
            }
         }
         return false;
      }
      return false;
   }

   void openImagesUsing(final File[] files, String procClassName)
           throws Exception {
      try {
         DatasetOpener proc = (DatasetOpener) Class.forName(procClassName).newInstance();
         if (proc != null) {
            proc.openDataset(files);
         }
      } catch (Exception exception) {
         throw exception;
      }
   }

   private String pluginToRun = "Orientation_Indicators";

   public void setPluginToRun(String pluginToRun) {
      this.pluginToRun = pluginToRun;
   }

   private void runPlugin() {
      if (pluginToRun != null) {
         IJ.run(pluginToRun);
      }
   }

   // TODO
   public void openInExternalViewer() {
      
   }

// </editor-fold>
}
