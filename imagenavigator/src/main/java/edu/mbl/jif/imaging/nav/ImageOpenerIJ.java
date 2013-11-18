/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.nav;

import java.io.File;
import org.dart.imagej.IJRunner;

/**
 *
 * @author GBH
 */
public class ImageOpenerIJ implements DatasetOpener {

   @Override
   public void openDataset(File[] files) {
      IJRunner.runImageJ(files, null);
   }
   
   
}
