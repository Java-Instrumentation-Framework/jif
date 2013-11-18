package net.trevize.galatee;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.Vector;

/**
 * GItem is the POJO object for an item in a Galatee panel.
 *
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]] GItem.java - May
 * 12, 2009
 */
public class GItem {

   private URI uri;
   private String filename;
   private String local_filepath;
   private BufferedImage image;
   private String text;
   private boolean chosen;
   private Vector<Object> data;

   public GItem(URI uri) {
      this.uri = uri;

      //get the filename.
      String uri_string = uri.toString();
      filename = uri_string.substring(uri_string.lastIndexOf("/") + 1,
              uri_string.length());

      this.image = null;

      if (uri.getScheme().equals("file")) {
         local_filepath = uri.getPath();
      }

      if (uri.getScheme().equals("tar")) {
         local_filepath = uri.toString();
      }
   }

   public String getLocalFilepath() {
      return local_filepath;
   }

   public InputStream getInputStream() {
      InputStream is = null;
      try {
         is = new FileInputStream(getLocalFilepath());
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      return is;
   }

   public String getFilename() {
      return filename;
   }

   public void setFilename(String filename) {
      this.filename = filename;
   }

   public BufferedImage getImage() {
      return image;
   }

   public void setImage(BufferedImage image) {
      this.image = image;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public Vector<Object> getData() {
      return data;
   }

   public boolean isChosen() {
      return chosen;
   }

   public void setChosen(boolean chosen) {
      this.chosen = chosen;
   }

   public void setData(Vector<Object> data) {
      this.data = data;
   }

   public URI getUri() {
      return uri;
   }

   public void setUri(URI uri) {
      this.uri = uri;
   }
}
