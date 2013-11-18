/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.imaging.nav.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author GBH
 */
public class FileMgt {

   // Directories
//   private static final File current = new File(".");
   private static final File javaDir = new File("src/main/java");
   private static final File orgDir = new File(javaDir, "org");
   private static final File apacheDir = new File(orgDir, "apache");
   private static final File commonsDir = new File(apacheDir, "commons");
   private static final File ioDir = new File(commonsDir, "io");
   private static final File outputDir = new File(ioDir, "output");
   private static final File[] dirs = new File[]{orgDir, apacheDir, commonsDir, ioDir, outputDir};
//
//   // Files
   private static final File filenameUtils = new File(ioDir, "FilenameUtils.java");
   private static final File ioUtils = new File(ioDir, "IOUtils.java");
   private static final File proxyWriter = new File(outputDir, "ProxyWriter.java");
   private static final File nullStream = new File(outputDir, "NullOutputStream.java");
   private static final File[] ioFiles = new File[]{filenameUtils, ioUtils};
   private static final File[] outputFiles = new File[]{proxyWriter, nullStream};
//
//   // Filters

   // We want to be able to filter for 
   //   folders like ../SM_*/*.tif
   //    or any file like SM_*
   //
   protected File[] wildcardResolution(File f) {
      File dir = f.getParentFile();
      FileFilter fileFilter = new WildcardFileFilter(f.getName());
      return dir.listFiles(fileFilter);
   }

   public void method() {
      Iterator iterateFiles = FileUtils.iterateFiles(
              new File("."), new WildcardFileFilter("*.xml"), TrueFileFilter.INSTANCE);
      while (iterateFiles.hasNext()) {
         System.out.println(iterateFiles.next());
      }
   }

   /**
    * Test Filtering and limit to depth 5
    */
   public void testFilterAndLimitD() {
      String topPath = "";
      File topDir = new File(topPath);
      File[] dirs = new File[]{topDir};
      IOFileFilter dirsFilter = createNameFilter(dirs);
      IOFileFilter iofilesFilter = createNameFilter(ioFiles);
      IOFileFilter outputFilesFilter = createNameFilter(outputFiles);
      IOFileFilter ioDirAndFilesFilter = new OrFileFilter(dirsFilter, iofilesFilter);
      IOFileFilter dirsAndFilesFilter = new OrFileFilter(ioDirAndFilesFilter, outputFilesFilter);
      //IOFileFilter outputFilesFilter = createNameFilter(outputFiles);
      List<File> results = new TestFileFinder(dirsAndFilesFilter, 5).find(javaDir);
      List<File> resultFiles = filesOnly(results);
      List<File> resultDirs = directoriesOnly(results);
//        assertEquals("[D] Result Size", 1 + dirs.length + ioFiles.length, results.size());
      for (File f : resultFiles) {
         //System.out.println(f.getAbsolutePath() + " : " + f.getParent() + " " + f.getName());
         System.out.println(f.getParent() + "\\" + f.getName());
      }
      for (File f : resultDirs) {
         //System.out.println(f.getAbsolutePath() + " : " + f.getParent() + " " + f.getName());
         System.out.println(f.getParent() + "\\" + f.getName());
      }
//        assertTrue("[D] Start Dir", results.contains(javaDir));
//        checkContainsFiles("[D] Dir", dirs, results);
//        checkContainsFiles("[D] File", ioFiles, results);
   }

   /**
    * Test separate dir and file filters
    */
   public void testFilterDirAndFile1() {
      String topPath = "";
      File topDir = new File(topPath);
      File[] dirs = new File[]{topDir};
      IOFileFilter dirsFilter = createNameFilter(dirs);

      File[] files = new File[]{filenameUtils, ioUtils};
      IOFileFilter filesFilter = createNameFilter(files);

      //WildcardFileFilter wildFilter = new WildcardFileFilter(null);
      List<File> results = new TestFileFinder(dirsFilter, filesFilter, -1).find(javaDir);
//        assertEquals("[DirAndFile1] Result Size", 1 + dirs.length + ioFiles.length, results.size());
//        assertTrue("[DirAndFile1] Start Dir", results.contains(javaDir));
//        checkContainsFiles("[DirAndFile1] Dir", dirs, results);
//        checkContainsFiles("[DirAndFile1] File", ioFiles, results);
   }

   /**
    * Extract the directories.
    */
   private List<File> directoriesOnly(Collection<File> results) {
      List<File> list = new ArrayList<File>(results.size());
      for (File file : results) {
         if (file.isDirectory()) {
            list.add(file);
         }
      }
      return list;
   }

   /**
    * Extract the files.
    */
   private List<File> filesOnly(Collection<File> results) {
      List<File> list = new ArrayList<File>(results.size());
      for (File file : results) {
         if (file.isFile()) {
            list.add(file);
         }
      }
      return list;
   }

   /**
    * Create an name filter containg the names of the files in the array.
    */
   private static IOFileFilter createNameFilter(File[] files) {
      String[] names = new String[files.length];
      for (int i = 0; i < files.length; i++) {
         names[i] = files[i].getName();
      }
      return new NameFileFilter(names);
   }

   //===============================
   /**
    * Test DirectoryWalker implementation that finds files in a directory hierarchy applying a file
    * filter.
    */
   private static class TestFileFinder extends DirectoryWalker<File> {

      protected TestFileFinder(FileFilter filter, int depthLimit) {
         super(filter, depthLimit);
      }

      protected TestFileFinder(IOFileFilter dirFilter, IOFileFilter fileFilter, int depthLimit) {
         super(dirFilter, fileFilter, depthLimit);
      }

      /**
       * find files.
       */
      protected List<File> find(File startDirectory) {
         List<File> results = new ArrayList<File>();
         try {
            walk(startDirectory, results);
         } catch (IOException ex) {
            //Assert.fail(ex.toString());
         }
         return results;
      }

      /**
       * Handles a directory end by adding the File to the result set.
       */
      @Override
      protected void handleDirectoryEnd(File directory, int depth, Collection<File> results) {
         results.add(directory);
      }

      /**
       * Handles a file by adding the File to the result set.
       */
      @Override
      protected void handleFile(File file, int depth, Collection<File> results) {
         results.add(file);
      }
   }

   public static void main(String[] args) {
   }
}
