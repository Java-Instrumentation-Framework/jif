package edu.mbl.jif.imaging.nav;

import java.io.File;
import java.io.IOException;
import org.dart.imagej.IJClient;
import org.dart.imagej.IJClientFactory;

/**
 *
 * @author GBH
 */
public class ImageOpenerIJ implements DatasetOpener {

   @Override
   public void openDataset(File[] files) {
      IJClient ijClient = IJClientFactory.getIJClient(false);
      for (File file : files) {
         try {
            ijClient.openImage(file);
         } catch (IOException ex) {
            
         }
      //IJRunner.runImageJ(files, null);
      }
      
   }
   
   
}
