package edu.mbl.jif.io.zip;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
//----------------------------------------------------------------------------
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.ArrayList;


public class ZipFileTools
{
   public ZipFileTools () {
   }


//
// Creating a ZIP File-----------------------------------------------------------
// These are the files to include in the ZIP file
   public void test () {
      String zipFilename = "outfile.zip";
      String[] filenames = new String[] {"filename1", "filename2"};
      createZIP(zipFilename, filenames);
   }


   public void createZIP (String zipFilename, String[] filenames) {
      // Create a buffer for reading the files
      byte[] buf = new byte[1024];
      try {
         // Create the ZIP file
         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
         // Compress the files
         for (int i = 0; i < filenames.length; i++) {
            FileInputStream in = new FileInputStream(filenames[i]);
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(filenames[i]));
            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
               out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
            in.close();
         }
         out.close(); // Complete the ZIP file
      }
      catch (IOException e) {}
   }


//
// Listing the Contents of a ZIP File------------------------------------------
   public ArrayList listZIP (String zipFilename) {
      ArrayList files = new ArrayList();
      try {
         // Open the ZIP file
         ZipFile zf = new ZipFile("filename.zip");
         // Enumerate each entry
         for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
            // Get the entry name
            files.add(((ZipEntry) entries.nextElement()).getName());
         }
      }
      catch (IOException e) {
      }
      return files;
   }


// Extract from Zip ------------------------------------------------------------
// ???
//  try {
//    // Open the ZIP file
//    String inFilename = "infile.zip";
//    ZipInputStream in = new ZipInputStream(new FileInputStream(inFilename));
//    // Get the first entry
//    ZipEntry entry = in.getNextEntry();
//    // Open the output file
//    String outFilename = "o";
//    OutputStream out = new FileOutputStream(outFilename);
//    // Transfer bytes from the ZIP file to the output file
//    byte[] buf = new byte[1024];
//    int len;
//    while ( (len = in.read(buf)) > 0) {
//      out.write(buf, 0, len);
//    }
//    // Close the streams
//    out.close();
//    in.close();
//  }
//  catch (IOException e) {
//  }


   // specify buffer size for extraction
   static final int BUFFER = 2048;

   public static void decompress (String fileToUnZip, String destinationDirectory) {
      try {
         // fileToUnZip:  file to decompress
         // destinationDirectory destination where file will be unzipped
         File sourceZipFile = new File(fileToUnZip);
         File unzipDestinationDirectory = new File(destinationDirectory);
         // Open Zip file for reading
         ZipFile zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
         // Create an enumeration of the entries in the zip file
         Enumeration zipFileEntries = zipFile.entries();
         // Process each entry
         while (zipFileEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            System.out.println("Extracting: " + entry);
            File destFile = new File(unzipDestinationDirectory, currentEntry);
            // grab file's parent directory structure
            File destinationParent = destFile.getParentFile();
            // create the parent directory structure if needed
            destinationParent.mkdirs();
            // extract file if not a directory
            if (!entry.isDirectory()) {
               BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(
                     entry));
               int currentByte;
               // establish buffer for writing file
               byte data[] = new byte[BUFFER];
               // write the current file to disk
               FileOutputStream fos = new FileOutputStream(destFile);
               BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
               // read and write until last byte is encountered
               while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                  dest.write(data, 0, currentByte);
               }
               dest.flush();
               dest.close();
               is.close();
            }
         }
         zipFile.close();
      }
      catch (IOException ioe) {
         ioe.printStackTrace();
      }
   }


//
//---------------------------------------------------------------------------
   public static void main (String[] args) {
      System.out.println("Example of ZIP file creation.");

      // Specify files to be zipped
      String[] filesToZip = new String[3];
      filesToZip[0] = "firstfile.txt";
      filesToZip[1] = "secondfile.txt";
      filesToZip[2] = "temp\thirdfile.txt";

      byte[] buffer = new byte[18024];

      // Specify zip file name
      String zipFileName = "c:\\example.zip";

      try {

         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));

         // Set the compression ratio
         out.setLevel(Deflater.DEFAULT_COMPRESSION);

         // iterate through the array of files, adding each to the zip file
         for (int i = 0; i < filesToZip.length; i++) {
            System.out.println(i);
            // Associate a file input stream for the current file
            FileInputStream in = new FileInputStream(filesToZip[i]);

            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(filesToZip[i]));

            // Transfer bytes from the current file to the ZIP file
            //out.write(buffer, 0, in.read(buffer));

            int len;
            while ((len = in.read(buffer)) > 0) {
               out.write(buffer, 0, len);
            }

            // Close the current entry
            out.closeEntry();

            // Close the current file input stream
            in.close();

         }
         // Close the ZipOutPutStream
         out.close();
      }
      catch (IllegalArgumentException iae) {
         iae.printStackTrace();
      }
      catch (FileNotFoundException fnfe) {
         fnfe.printStackTrace();
      }
      catch (IOException ioe) {
         ioe.printStackTrace();
      }
   }

}
