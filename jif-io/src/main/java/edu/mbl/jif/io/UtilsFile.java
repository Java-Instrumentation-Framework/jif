package edu.mbl.jif.io;

import java.io.File;
import java.io.IOException;
import java.util.Stack;


public class UtilsFile
{

   public UtilsFile () {}


   public static void showCurrentDirectories () {
      File dir1 = new File(".");
      File dir2 = new File("..");
      try {
         System.out.println(". (current):    " + dir1.getCanonicalPath());
         System.out.println(".. (parent):    " + dir2.getCanonicalPath());
         System.out.println("user.dir:       " + System.getProperty("user.dir"));
         System.out.println("user.home:      " + System.getProperty("user.home"));
         System.out.println("java.io.tmpdir: " + System.getProperty("java.io.tmpdir"));
      }
      catch (Throwable t) {
         t.printStackTrace();
      }
   }


   public static void dirStack () {
      // @version 1.10 1999-07-07, author Cay Horstmann
      Stack directoryStack = new Stack();
      File root = new File(File.separator + ".");
      directoryStack.push(root);
      while (directoryStack.size() > 0) {
         File currentDirectory = (File) directoryStack.pop();
         System.out.println(currentDirectory);
         String[] subdirectories = currentDirectory.list();
         if (subdirectories != null) {
            for (int i = 0; i < subdirectories.length; i++) {
               try {
                  String fname = currentDirectory.getCanonicalPath()
                                 + File.separator + subdirectories[i];
                  File f = new File(fname);

                  if (f.isDirectory()) {
                     directoryStack.push(f);
                  }
               }
               catch (IOException e) {
                  System.out.println(e);
               }
            }
         }
      }
   }


   public static void main (String[] args) {
      showCurrentDirectories();
      dirStack();
   }
}
