package edu.mbl.jif.io.zip;

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

/*
import de.schlichtherle.io.*;
import java.io.PrintStream;


public class Zippy
{

   // Creates a text file contained in two ZIP files in the current directory.
   public Zippy () throws ZipControllerException {}


   public static void main (String[] args) throws Exception {
      try {
         // Do any file IO here as if you were importing java.io.*
         // No ZIP file needs to exist unless FileOutputStream.strict is set.
         FileOutputStream fos = new FileOutputStream("outer.zip/inner.zip/hello.txt");
         PrintStream ps = new PrintStream(fos);
         ps.println("Creating nested ZIP files is really a no-brainer!");
         ps.close();
      }
      finally {
         // Always update all modified ZIP compatible files before
         // terminating!
         // Do a quick update here which does not release memory resources
         // and does not support concurrent modifications of the used ZIP
         // compatible files by other processes.
         // In a long termed loop, use File.umount() instead.
         File.update(); // grant priority for any exceptions thrown here
      }
   }
}
 */
