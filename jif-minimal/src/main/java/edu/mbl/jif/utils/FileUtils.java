/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.utils;

import edu.mbl.jif.gui.DialogBox;
import java.io.File;

/**
 *
 * @author GBH
 */
public class FileUtils {
   
      public static boolean checkExists(String file, String msg) {
      if ((new File(file)).exists()) {
         return true;
      } else { 
         DialogBox.boxError("Error", msg + " doesn't exist\n" + file);
         return false;
      }
   }

   public static boolean checkFileExists(String fileStr, String msg) {
      File file = new File(fileStr);
      if (file.exists() && file.isFile()) {
         return true;
      } else {
         DialogBox.boxError("Error", msg + " file doesn't exist\n" + file);
         return false;
      }
   }
}
