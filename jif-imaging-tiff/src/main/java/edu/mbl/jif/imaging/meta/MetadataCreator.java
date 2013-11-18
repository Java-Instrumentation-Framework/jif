package edu.mbl.jif.imaging.meta;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;


public class MetadataCreator
{
   public MetadataCreator () {}

   public static void main (String[] args) {
      try {
         // Create an image in memory and save it as Jpeg with metadata text.
         BufferedImage buffImg = new BufferedImage(50, 50, BufferedImage.TYPE_BYTE_GRAY);
         Graphics2D gc = buffImg.createGraphics();
         gc.drawRect(20, 20, 10, 10);

         // create ImageWriter
         File outputFile = new File("out.jpg");
         ImageWriter imgWriter = getImageWriterForMetadata();
         ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile);
         imgWriter.setOutput(ios);
         
         // Get the default metadata
         ImageTypeSpecifier imgTypeSpec = new ImageTypeSpecifier(buffImg);
         ImageWriteParam param = imgWriter.getDefaultWriteParam();
         IIOMetadata meta = imgWriter.getDefaultImageMetadata(imgTypeSpec, param);
         
         IIOMetadataDisplay.displayIIOMetadata(meta);
         modifyMetadata(meta);  // test
         System.out.println("Modified");
         IIOMetadataDisplay.displayIIOMetadata(meta);
         
         // save it to file
         IIOImage image = new IIOImage(buffImg, null, meta);
         imgWriter.write(image);
         ios.close();
         imgWriter.dispose();

         
         // Now open the new image that we just saved
         ImageInputStream iis2 = ImageIO.createImageInputStream(outputFile);
         ImageReader imgReader2 = getImageReaderForMetadata();
         imgReader2.setInput(iis2);

         // The following statement throws an IOException:  JFIF APP0 must be
         // first marker after SOI
         IIOMetadata meta2 = imgReader2.getImageMetadata(0);
      }
      catch (IOException exc) {
         System.err.println("IOException: " + exc.getMessage());
      }
   }


   static ImageReader getImageReaderForMetadata () {
      // Get a specific reader (we need one that supports the standard
      // metadata format).
      ImageReader imgReader = null;
      String useThisImageReader = "com.sun.imageio.plugins.jpeg.JPEGImageReader";
      Iterator it = ImageIO.getImageReadersByFormatName("JPEG");
      while (it.hasNext()) {
         imgReader = (ImageReader) it.next();
         if (imgReader.getClass().getName().compareTo(useThisImageReader) == 0) {
            break;
         } else {
            imgReader = null;
         }
      }
      return (imgReader);
   }


   static ImageWriter getImageWriterForMetadata () {
      // Get a specific writer (we need one that supports the standard
      // metadata format).
      ImageWriter imgWriter = null;
      String useThisImageWriter = "com.sun.imageio.plugins.jpeg.JPEGImageWriter";
      Iterator it = ImageIO.getImageWritersByFormatName("JPEG");
      while (it.hasNext()) {
         imgWriter = (ImageWriter) it.next();
         if (imgWriter.getClass().getName().compareTo(useThisImageWriter) == 0) {
            break;
         } else {
            imgWriter = null;
         }
      }
      return (imgWriter);
   }


   static void modifyMetadata (IIOMetadata meta) {
      if (meta != null) {
        
         IIOMetadataNode metadataRootNode = 
               (IIOMetadataNode) meta.getAsTree("javax_imageio_1.0");
         IIOMetadataNode textNode = new IIOMetadataNode("Text");
         IIOMetadataNode textEntryNode = new IIOMetadataNode("TextEntry");
         textEntryNode.setAttribute("value", "This is a test string");
         textNode.appendChild(textEntryNode);
         metadataRootNode.appendChild(textNode);
         try {
            meta.setFromTree("javax_imageio_1.0", metadataRootNode);
         }
         catch (IIOInvalidTreeException exc) {
            System.out.println("Invalid tree exception");
            exc.printStackTrace();
         }
      }
   }

}
