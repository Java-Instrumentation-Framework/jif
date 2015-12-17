/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.nav;

import java.io.File;

/**
 *
 * @author GBH
 */
public class Test {

   public  void test() {

      boolean plugin=false;
      String topPath= null;
      boolean allowNavUp= true;
      FileChoosenListener fileListener = new TestFileChoosenListener();
      ImageNavigator nav = new ImageNavigator(plugin, topPath, allowNavUp,  fileListener);

   }

   
   
   public static void main(String[] args) {
      new Test().test();
   }
   
   
   
   class TestFileChoosenListener implements FileChoosenListener {

      public void fileChoosen(File file) {
         // A file was choosen... do something with it.
         System.out.println("FileChoosen: " + file);
      }

   }
}
