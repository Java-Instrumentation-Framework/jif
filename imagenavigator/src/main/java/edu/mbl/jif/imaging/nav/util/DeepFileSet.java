/*
 * DeepFileSet.java
 *
 * Created on May 24, 2007, 2:28 PM
 */
package edu.mbl.jif.imaging.nav.util;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

/**
 * This class is part of the <A HREF=http://www.mpi-inf.mpg.de/~suchanek/downloads/javatools
 * target=_blank> Java Tools
 * </A> by <A HREF=http://www.mpi-inf.mpg.de/~suchanek target=_blank> Fabian M. Suchanek</A> You may
 * use this class if (1) it is not for commercial purposes, (2) you give credit to the author and
 * (3) you use the class at your own risk. If you use the class for scientific purposes, please cite
 * our paper "Combining Linguistic and Statistical Analysis to Extract Relations from Web Documents"
 * (<A HREF=http://www.mpi-inf.mpg.de/~suchanek/publications/kdd2006.pdf target=_blank>pdf</A>, <A
 * HREF=http://www.mpi-inf.mpg.de/~suchanek/publications/kdd2006.bib target=_blank>bib</A>, <A
 * HREF=http://www.mpi-inf.mpg.de/~suchanek/publications/kdd2006.ppt target=_blank>ppt</A> ). If you
 * would like to use the class for commercial purposes, please contact <A
 * HREF=mailto:f.m.suchanek@web.de>Fabian M. Suchanek</A><P>
 *
 * This class represents a set of files as given by a wildcard string. It can also recurse into
 * subfolders. It does not return folders and is not case-sensitive. The class can be used as an
 * Iterator or Iterable (e.g. in a for-each-loop).<BR> Example:
 * <PRE>
 * for(File f : new DeepFileSet("c:\\myfiles\\*.jaVa"))
 * System.out.println(f);
 * -->
 * c:\myfiles\FileSet.java
 * c:\myfiles\HTMLReader.java
 * c:\myfiles\mysubfolder\OtherFile.java
 * ...
 * </PRE>
 *
 */
public class DeepFileSet implements Iterable<File>, Iterator<File> {

   private File[] fileList;
   private final Stack<File> paths = new Stack<File>();
   private final Pattern wildcard;
   protected boolean subFolders;
   private Iterator<File> currentIterator;

   /**
    * Constructs a DeepFileSet from a wildcard string (including path)
    */
   public DeepFileSet(File f, boolean recurseSubfolders) {
      File path = f.getParentFile() == null ? new File(".") : f.getParentFile();
      paths.push(path);
      String regex = "";
      String fname = f.getName();
      for (int i = 0; i < fname.length(); i++) {
         switch (fname.charAt(i)) {
            case '.':
               regex += "\\.";
               break;
            case '?':
               regex += '.';
               break;
            case '*':
               regex += ".*";
               break;
            default:
               regex += "" + '[' + Character.toLowerCase(fname.charAt(i)) + 
                       Character.toUpperCase(fname.charAt(i)) + ']';
         }
      }
      wildcard = Pattern.compile(regex);
      subFolders = recurseSubfolders;
      fillUp();
   }

   /**
    * Constructs a DeepFileSet from a wildcard string (including path)
    */
   public DeepFileSet(String f, boolean recurseSubfolders) {
      this(new File(f), recurseSubfolders);
   }

   /**
    * Constructs a DeepFileSet from a wildcard string (including path). Recurses subfolders if the
    * string contains wildcards.
    */
   public DeepFileSet(String s) {
      this(s, true);
      subFolders = s.indexOf('*') != -1 || s.indexOf('?') != -1;
   }

   /**
    * Constructs a DeepFileSet from a wildcard string (including path). Recurses subfolders if the
    * string contains wildcards.
    */
   public DeepFileSet(File s) {
      this(s, true);
      subFolders = s.getName().indexOf('*') != -1 || s.getName().indexOf('?') != -1;
   }

   /**
    * Fills up fileList
    */
   private void fillUp() {
      if (paths.empty()) {
         fileList = null;
         currentIterator = new Iterator<File>() {

            public boolean hasNext() {
               return (false);
            }

            public void remove() {
            }

            public File next() {
               return (null);
            }

         };
         return;
      }
      File folder = paths.pop();
      fileList = folder.listFiles(new FileFilter() {

         public boolean accept(File pathname) {
            if (subFolders && pathname.isDirectory()) {
               paths.push(pathname);
            }
            return (wildcard.matcher(pathname.getName()).matches());
         }

      });
      currentIterator = new Iterator<File>() {

         int i = 0;

         public boolean hasNext() {
            return (i < fileList.length);
         }

         public File next() {
            return (fileList[i++]);
         }

         public void remove() {
         }

      };
   }

   /**
    * Does nothing
    */
   public void remove() {
   }

   /**
    * Returns an iterator on the FileSet
    */
   public Iterator<File> iterator() {
      return (this);
   }

   /**
    * Tells whether there are more files
    */
   public boolean hasNext() {
      while (!currentIterator.hasNext() && !paths.empty()) {
         fillUp();
      }
      return (currentIterator.hasNext());
   }

   /**
    * Returns next file
    */
   public File next() {
      if (!hasNext()) {
         return (null);
      }
      return (currentIterator.next());
   }

   /**
    * Returns the current state of this DeepFileSet
    */
   public String toString() {
      return ("DeepFileSet at " + paths);
   }

   /**
    * Test routine
    */
   public static void main(String argv[]) {
      String path = //"C:\\_Dev\\_Dev_Data\\TestImages\\*.tif";
              "C:/MicroManagerData/project/testdata/SM_*";
      for (File f : new DeepFileSet(path)) {
         //System.out.println(f.getAbsolutePath() + " : " + f.getParent() + " " + f.getName());
         System.out.println(f.getParent() + "\\" + f.getName());
      }
//      for (File f : new DeepFileSet(path, false)) {
//         //System.out.println(f.getAbsolutePath() + " : " + f.getParent() + " " + f.getName());
//         System.out.println(f.getParent() + "\\" + f.getName());
//      }
      
      
//        UserIO.p("Enter a filename with wildcards and hit ENTER. Press CTRL+C to abort");
//        while (true) {
//            for (File f : new DeepFileSet(UserIO.r())) {
//                //UserIO.p(f);
//                System.out.println(f.getAbsolutePath() + " : " + f.getParent() + " " + f.getName());
//            }
//
//        }
   }

}