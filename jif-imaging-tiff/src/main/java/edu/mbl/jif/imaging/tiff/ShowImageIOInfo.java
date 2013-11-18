package edu.mbl.jif.imaging.tiff;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;

public class ShowImageIOInfo {

   static public void main(String args[])
           throws Exception {
      IIORegistry registry = IIORegistry.getDefaultInstance();
      registry.registerServiceProvider(
              new com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi());
      registry.registerServiceProvider(
              new com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi());
      ImageIO.scanForPlugins();
      String names[] = ImageIO.getReaderFormatNames();
      for (int i = 0; i < names.length; ++i) {
         System.out.println("reader " + names[i]);
      }

      names = ImageIO.getWriterFormatNames();
      for (int i = 0; i < names.length; ++i) {
         System.out.println("writer " + names[i]);
      }
   }
}
