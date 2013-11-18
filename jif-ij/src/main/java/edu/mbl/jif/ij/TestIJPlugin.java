
package edu.mbl.jif.ij;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.io.OpenDialog;

/**
 *
 * @author GBH
 */
public class TestIJPlugin implements ij.plugin.PlugIn {

   public void run(String arg) {
      OpenDialog od = new OpenDialog("Open Micro-Manager Dataset...", arg);
      String directory = od.getDirectory();
      String fileName = od.getFileName();
      if (fileName == null) {
         return;
      }
   }
// ====================================================================================
   // Testing as an ImageJ Plugin... 
   //
   public static void testAsImageJPlugin(Class<?> clazz, String testImagePath) {
      String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
      String pluginsDir = url.substring(5, url.length() - clazz.getName().length() - 6);
      System.setProperty("plugins.dir", pluginsDir);
      // start ImageJ
      ImageJ imagej = new ImageJ();

      ImagePlus img = IJ.openImage(testImagePath);
      img.show();

      // run the plugin
      IJ.runPlugIn(clazz.getName(), "");
      
   }



   /**
    * Test as ImageJ Plugin
    *
    * For debugging, it is convenient to have a method that starts ImageJ, loads an image and calls
    * the plugin, e.g. after setting breakpoints.
    *
    */
   public static void main(String[] args) {
      // set the plugins.dir property to make the plugin appear in the Plugins menu
      //Class<?> clazz = edu.mbl.cdp.ps.plugins.Orientation_Indicators.class;
      Class<?> clazz = TestIJPlugin.class;
      String testImagePath = "C:/_Dev/_Dev_Data/TestImages/testData/PS_Aster/PS_03_0825_1753_24.tif";
      testAsImageJPlugin(clazz, testImagePath);
   }   
}
