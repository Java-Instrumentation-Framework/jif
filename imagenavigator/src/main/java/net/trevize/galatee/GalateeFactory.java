package net.trevize.galatee;


import edu.mbl.jif.imaging.nav.util.FilePathUtils;
//import edu.mbl.jif.imaging.nav.util.PathUtils;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Vector;
import org.apache.commons.io.comparator.NameFileComparator;

/**
 *
 *
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]] GalateeFactory.java
 * - May 17, 2010
 */
public class GalateeFactory {
   
   static String topDir = "";

   public static Galatee loadDatasetFromDirectory(String dir_path,
           boolean recursive, boolean firstOnly, int numColumns, boolean showDescription, boolean equalizeHisto) {
      Vector<URI> v_uri = new Vector<URI>();
      Vector<Vector<Object>> v_object = new Vector<Vector<Object>>();
      File dir = new File(dir_path);
      if (!hasSubDirectories(dir)) {
         recursive = false;
      }
      if (!recursive) {
         firstOnly = false;
      }
      topDir = dir_path;
      getFilesRec(dir, v_uri, v_object, recursive, firstOnly);

      Galatee g = new Galatee(v_uri, v_object,
              new Dimension(GalateeProperties.getImage_width(),
              GalateeProperties.getImage_height()),
              GalateeProperties.getDescription_width(),
              numColumns, showDescription, equalizeHisto);

      return g;
   }
   
   public static Galatee loadDatasetFromDirectory(String dir_path,
           boolean recursive, boolean firstOnly) {
      Vector<URI> v_uri = new Vector<URI>();
      Vector<Vector<Object>> v_object = new Vector<Vector<Object>>();
      File dir = new File(dir_path);
      if (!hasSubDirectories(dir)) {
         recursive = false;
      }
      if (!recursive) {
         firstOnly = false;
      }
      getFilesRec(dir, v_uri, v_object, recursive, firstOnly);

      Galatee g = new Galatee(v_uri, v_object,
              new Dimension(GalateeProperties.getImage_width(),
              GalateeProperties.getImage_height()),
              GalateeProperties.getDescription_width(),
              GalateeProperties.getNumber_of_column());
      return g;
   }

   private static void getFilesRec(File dir, Vector<URI> v_uri,
           Vector<Vector<Object>> v_object, boolean recurse, boolean firstOnly) {
      File[] children = dir.listFiles();
      Arrays.sort(children, NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
      boolean didFirst = false;
      for (int i = 0; i < children.length; ++i) {
         File child = children[i];
         if (child.isDirectory()) {
            if (recurse) {
               getFilesRec(child, v_uri, v_object, recurse, firstOnly);
            }
         } else { // is a file.
            if (!didFirst) {
               String filename = children[i].getName();
               int ext_index = filename.lastIndexOf(".");
               if (ext_index != -1) {
                  // Check type
                  String extension = filename.substring(
                          filename.lastIndexOf(".") + 1, filename.length()).toLowerCase();
                  if (GalateeProperties.getAuthorized_filename_extensions().contains(extension)) {
                     v_uri.add(children[i].toURI());
                     Vector<Object> v = new Vector<Object>();
                     //
                     // Set the description... <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                     
                     v.add(FilePathUtils.getRelativePath(topDir,children[i].getParentFile().getAbsolutePath()) +
                             "/\n" + children[i].getName());
                     
                     // v.add(children[i].getParentFile().getName() + "/\n" + children[i].getName());
                     //v.add(children[i].getParentFile().getAbsolutePath() + "/\n" + children[i].getName());
                     // v.add(children[i].getName());
                     //
                     v_object.add(v);
                     if (firstOnly) {
                        didFirst = true;
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean hasSubDirectories(File dir) {
      File[] children = dir.listFiles();
      for (int i = 0; i < children.length; ++i) {
         File child = children[i];
         if (child.isDirectory()) {
            return true;
         }
      }
      return false;
   }

   // ======================================================================
   // From here on could be elliminated....
   public static Galatee loadLocalDatasetFromFile(String filepath,
           String prefix) {
      //loading files and metadata.
      Vector<URI> v_uri = new Vector<URI>();
      Vector<Vector<Object>> v_object = new Vector<Vector<Object>>();
      try {
         FileReader fr = new FileReader(filepath);
         BufferedReader br = new BufferedReader(fr);

         String line;
         while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            String image_uri_value = fields[0];
            URI uri = null;
            if (prefix != null) {
               uri = new File(prefix + File.separator + image_uri_value)
                       .toURI();
            } else {
               uri = new File(image_uri_value).toURI();
            }
            v_uri.add(uri);
            Vector<Object> v = new Vector<Object>();
            v_object.add(v);
            //a StringBuffer for containing the textual description.
            StringBuffer sb = new StringBuffer();
            //get the image filename.
            String image_filename = image_uri_value.substring(
                    image_uri_value.lastIndexOf(File.separator) + 1,
                    image_uri_value.length());
            sb.append(image_filename + "\n");
            //add textual annotation if there is.
            if (fields.length > 1) {
               for (int i = 1; i < fields.length; ++i) {
                  sb.append(fields[i] + "\n");
               }
            }
            v.add(sb.toString());
         }
         br.close();
         fr.close();
      } catch (FileNotFoundException e1) {
         e1.printStackTrace();
      } catch (IOException e1) {
         e1.printStackTrace();
      }
      Galatee g = new Galatee(v_uri, v_object, new Dimension(
              GalateeProperties.getImage_width(), GalateeProperties
              .getImage_height()), GalateeProperties
              .getDescription_width(), GalateeProperties
              .getNumber_of_column());

      return g;
   }

   public static Galatee loadHTTPDatasetFromFile(String filepath, String prefix) {
      //loading files and metadata.
      Vector<URI> v_uri = new Vector<URI>();
      Vector<Vector<Object>> v_object = new Vector<Vector<Object>>();

      try {
         FileReader fr = new FileReader(filepath);
         BufferedReader br = new BufferedReader(fr);

         String line;
         while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            String image_uri_value = fields[0];

            URI uri = null;
            if (prefix != null) {
               uri = new URI(prefix + "/" + image_uri_value);
            } else {
               uri = new URI(image_uri_value);
            }
            v_uri.add(uri);
            Vector<Object> v = new Vector<Object>();
            v_object.add(v);
            //a StringBuffer for containing the textual description.
            StringBuffer sb = new StringBuffer();
            //get the image filename.
            String image_filename = image_uri_value.substring(
                    image_uri_value.lastIndexOf("/") + 1, image_uri_value
                    .length());
            sb.append(image_filename + "\n");
            //add textual annotation if there is.
            if (fields.length > 1) {
               for (int i = 1; i < fields.length; ++i) {
                  sb.append(fields[i] + "\n");
               }
            }
            v.add(sb.toString());
         }

         br.close();
         fr.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (URISyntaxException e) {
         e.printStackTrace();
      }

      Galatee g = new Galatee(v_uri, v_object, new Dimension(
              GalateeProperties.getImage_width(), GalateeProperties
              .getImage_height()), GalateeProperties
              .getDescription_width(), GalateeProperties
              .getNumber_of_column());

      return g;
   }
}
