package edu.mbl.jif.imaging.series;

import java.io.File;

import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import edu.mbl.jif.utils.*;


public class Stackificator
{
   String directoryName = ".";
   int slices = 0;
   MultipageTiffFile[] tifFile;

// Assumes Raw PolStacks of N slices

   public Stackificator (String directoryName, int slices) {
      this.directoryName = directoryName;
      this.slices = slices;
      tifFile = new MultipageTiffFile[slices];
   }


   public void DeStackificate () {
      // For all the files in a directory,
      // all having the same number of slices,
      // generates [slices] files named stackSet_0.tif, stackSet_1.tif, ...
      // each containing a stack with [files] images.

      File directory = new File(directoryName);
      if (directory.isDirectory() == false) {
         if (directory.exists() == false) {
            System.out.println("There is no such directory!");
         } else {
            System.out.println("That file is not a directory.");
         }
      } else {
         for (int i = 0; i < tifFile.length; i++) {
            tifFile[i] = new MultipageTiffFile("stackSet_" + i);
         }
         File[] files = directory.listFiles();
         System.out.println("Files in directory \"" + directory + "\":");
         for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getAbsolutePath());
            MultipageTiffFile tifSection = new MultipageTiffFile(
                  files[i].getAbsolutePath());
            for (int j = 0; j < tifFile.length; j++) {
               tifFile[j].appendImage(tifSection.getImage(j));
            }
         }
      }
      for (int j = 0; j < tifFile.length; j++) {
         tifFile[j].close();
      }
   }


   public void ReStackificate () {
      File directory = new File(directoryName);
      if (directory.isDirectory() == false) {
         if (directory.exists() == false) {
            System.out.println("There is no such directory!");
         } else {
            System.out.println("That file is not a directory.");
         }
      } else {
         for (int i = 0; i < tifFile.length; i++) {
            tifFile[i] = new MultipageTiffFile("stackSet_" + i);
         }
         File[] files = directory.listFiles();
         for (int i = 0; i < files.length; i++) {
            String base = edu.mbl.jif.utils.FileUtil.getBaseName(files[i].
                  getAbsolutePath());
            String path = edu.mbl.jif.utils.FileUtil.getJustPath(files[i].
                  getAbsolutePath());
            base = base + "_X";
            String newFile = path + "\\" + base + ".tif";
            System.out.println(newFile);
            MultipageTiffFile tifSection = new MultipageTiffFile(newFile);
            for (int j = 0; j < tifFile.length; j++) {
               tifSection.appendImage(tifFile[j].getImage(i));
            }
         }
         for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getAbsolutePath());
            MultipageTiffFile tifSection = new MultipageTiffFile(
                  files[i].getAbsolutePath());
            for (int j = 0; j < tifFile.length; j++) {
               tifFile[j].appendImage(tifSection.getImage(j));
            }
         }
      }
      for (int j = 0; j < tifFile.length; j++) {
         tifFile[j].close();
      }
   }


   public void clearTempFiles () {
      for (int i = 0; i < slices; i++) {
         edu.mbl.jif.utils.FileUtil.deleteFile("stackSet_" + i);
      }
   }


   public static void main (String[] args) {
      String seriesDir =
            "C:\\PSjData\\project1\\TestSeries\\series\\PS_05_0726_1515_15";
      int numberOfSlices = 5;
      Stackificator s = new Stackificator(seriesDir, numberOfSlices);
      s.DeStackificate();

      // Do deconvolution...

      s.ReStackificate();
      // s.clearTempFiles()
   }
}
