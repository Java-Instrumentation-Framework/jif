package edu.mbl.jif.imaging;

// ^^^^^ GBH:  This uses JAI, not ImageIO...

import edu.mbl.jif.imaging.meta.XML2JTree;
import com.sun.media.imageio.plugins.tiff.*;

import org.w3c.dom.*;

import java.awt.*;
import java.awt.image.renderable.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import javax.media.jai.*;


// displays metadata in edu.mbl.jif.xml.XML2JTree


public class TiffMetadataTreeTest {
   public TiffMetadataTreeTest() {}
//   static String tiffFileIn = "C:\\_dev\\_TestImages\\ImagesTest\\tiff\\new_zealand.tif";
   static String tiffFileout = "C:\\_dev\\_TestImages\\testMeta.tif";
   //
   //---------------------------------------------------------------------
   public static void writeMeta(String filename) {
      String nameOfOutputFile = tiffFileout; //will set to args[0]
      
      String errorMessage = null;
      
      //check arguments, set nameOfOutputFile
      //    if (args.length != 1) {
      //      errorMessage = "I take one argument, name of output tiff.";
      //    } else if ((new File(nameOfOutputFile = args[0])).exists()) {
      //      errorMessage = "Won't clobber" + nameOfOutputFile;
      //    }
      //    if (errorMessage != null) {
      //      System.out.println(errorMessage);
      //      System.exit(0);
      //    }
      //Make a blue rectangle
      Byte[] bandValues = {new Byte((byte) 100), new Byte((byte) 100),
      new Byte((byte) 200)
      };
      RenderedOp constantImage = JAI.create("constant",
            (new ParameterBlock()).add(new Float(300)).add(new Float(500)).add(bandValues));
      
      //Now we set about saving the blue image as a tiff, with the
      //Tiff tag "ImageDescription"  set to "Hello World.  The tiff will be
      // untiled so the tiff can be opened by Photoshop.
      //In Photoshop, this tag is File >> File Info >> General >>  Caption.
      //I'll use an "imageWrite" RenderedOp (com.sun.media.jai.operator.ImageWriteDescriptor).
      ParameterBlock writePB = new ParameterBlock().addSource(constantImage);
      
      //Set 'Output' and 'Format'.
      writePB.add(nameOfOutputFile).add("tiff");
      
      //Set 'UseProperties', 'Transcode', 'VerifyOutput' and
      // 'AllowPixelReplacement' to default.
      writePB.add(new Boolean(true)).add(new Boolean(true)).add(new Boolean(true)).add(new
            Boolean(false));
      
      //Set 'TileSize' so as to make one big tile (for an untiled tiff).
      Dimension tileDim = new Dimension(constantImage.getWidth(), constantImage.getHeight());
      writePB.add(tileDim);
      
      //Set 'streamMetadata' to default.
      writePB.add(null);
      
      //Use first available ImageWriter that can handle a tiff.
      Iterator tiffWriters = ImageIO.getImageWritersBySuffix("tiff");
      ImageWriter tiffWriter = (ImageWriter) tiffWriters.next();
      System.out.print("Using this tiff writer: ");
      System.out.println(tiffWriter.toString());
      
      //Use a generic parameters for the imagewriter.
      ImageWriteParam writeParam = tiffWriter.getDefaultWriteParam();
      
      //Get default Metadata for an image in a tiff.
      ImageTypeSpecifier thisType = new ImageTypeSpecifier(constantImage.getColorModel(),
            constantImage.getSampleModel());
      IIOMetadata imageData = tiffWriter.getDefaultImageMetadata(thisType, writeParam);
      
      //Verify we're stuck with native tiff metadata format.
      //Not as expected, c.f. the second table in
      //http://java.sun.com/products/java-media/jai/forDevelopers/jai-imageio-1_0-rc-docs/index.html
      System.out.print("Standard metadata format is available:  ");
      
      System.out.println(String.valueOf(imageData.isStandardMetadataFormatSupported()));
      
      //Get the name for the native format.
      String imageDataFormat = imageData.getNativeMetadataFormatName();
      
      //Turn the metadata object into a tree.
      Node iNode = imageData.getAsTree(imageDataFormat);
      
      
      // addTIFFAsciiField(String tagNumber, String Name, String value){
      
      //Create the little tree corresponding to the Tiff Tag "ImageDescription".
      IIOMetadataNode fieldNode = new IIOMetadataNode("TIFFField");
      fieldNode.setAttribute("number", "270");
      fieldNode.setAttribute("name", "ImageDescription");
      //fieldNode.setAttribute("number", "269");
      //fieldNode.setAttribute("name", "DocumentName");
      IIOMetadataNode asciisNode = new IIOMetadataNode("TIFFAsciis");
      fieldNode.appendChild(asciisNode);
      IIOMetadataNode asciiNode = new IIOMetadataNode("TIFFAscii");
      String s = "PSj:type=polstack;frames=5;exposure=3000;";
      asciiNode.setAttribute("value", s);
      asciisNode.appendChild(asciiNode);
      
      //Add this to the image metadata tree, down one level.
      Node tagSetNode = iNode.getFirstChild();
      tagSetNode.appendChild(fieldNode);
      
      //Convert the tree back to metadata object
      try {
         imageData.setFromTree(imageDataFormat, iNode);
      } catch (IIOInvalidTreeException e) {
         System.out.println(e.getOffendingNode().toString());
         System.exit(0);
      }
      
      //Set 'ImageMetadata' to resulting metadata object.
      writePB.add(imageData);
      
      //Set 'Thumbnails', 'Listeners' and 'Locale' to default.
      writePB.add(null).add(null).add(null);
      
      //Set 'WriteParam' and 'Writer'.
      writePB.add(writeParam).add(tiffWriter);
      
      //At last, we save the file.
      JAI.create("imageWrite", writePB);
   }
   
   
   //
   //---------------------------------------------------------------------
   public static void viewMeta(String filename) {
      String nameOfInputFile = filename; //tiffFileIn; //will set to args[0]
      String errorMessage = null; //check arguments, set nameOfInputFile
      
      try {
         //make input stream.
         File inFile = new File(nameOfInputFile);
         ImageInputStream inStream = ImageIO.createImageInputStream(inFile);
         
         //Fetch first available tiff reader.
         Iterator readerList = ImageIO.getImageReaders(inStream);
         ImageReader firstReader = (ImageReader) readerList.next();
         System.out.println(firstReader.toString());
         //attach reader and stream.
         firstReader.setInput(inStream);
         
         // get Stream Metadata
         IIOMetadata mdataS = null;
         Node mNodeS = null;
         try {
            mdataS = firstReader.getStreamMetadata();
            mNodeS = mdataS.getAsTree(mdataS.getNativeMetadataFormatName());
            edu.mbl.jif.gui.test.FrameForTest f =
                  new edu.mbl.jif.gui.test.FrameForTest();
            f.setTitle("Stream Metadata");
            f.getContentPane().add(
                  new XML2JTree( mNodeS, true ),BorderLayout.CENTER);
            f.pack();
            f.setVisible(true);
            
            displayMetadata(mNodeS);
         } catch (Exception e) {
            e.printStackTrace();
         }
         
         //This should read read the metadata attached to first image,
         // w/o reading that image.
         IIOMetadata mdata = firstReader.getImageMetadata(0);
         //Convert from metadata object to a tree
         Node mNode = mdata.getAsTree(mdata.getNativeMetadataFormatName());
         edu.mbl.jif.gui.test.FrameForTest fi =
               new edu.mbl.jif.gui.test.FrameForTest();
         fi.setTitle("Image Metadata");
         fi.getContentPane().add(
               new XML2JTree( mNode, true ),BorderLayout.CENTER);
         fi.pack();
         fi.setVisible(true);
         //print out the metadata.
         displayMetadata(mNode);
      } catch (Exception e) {
         System.out.println("Caught: " + e.toString());
         System.exit(0);
      }
   }
   
   
   static void displayMetadata(Node root) {
      displayMetadata(root, 0);
   }
   
   
   //---------------------------------------------------------------------
   static void displayMetadata(Node node, int level) {
      indent(level); // emit open tag
      System.out.print("<" + node.getNodeName());
      NamedNodeMap map = node.getAttributes();
      if (map != null) { // print attribute values
         int length = map.getLength();
         for (int i = 0; i < length; i++) {
            Node attr = map.item(i);
            System.out.print(" " + attr.getNodeName() + "=\"" + attr.getNodeValue()
            + "\"");
         }
      }
      Node child = node.getFirstChild();
      if (child != null) {
         System.out.println(">"); // close current tag
         while (child != null) { // emit child tags recursively
            displayMetadata(child, level + 1);
            child = child.getNextSibling();
         }
         indent(level); // emit close tag
         System.out.println("</" + node.getNodeName() + ">");
      } else {
         System.out.println("/>");
      }
   }
   
   
   //---------------------------------------------------------------------
   static void indent(int level) {
      for (int i = 0; i < level; i++) {
         System.out.print("   ");
      }
   }
   public static void main(String[] args) {
      // String imageFile = edu.mbl.jif.Constants.testDataPath + "meta/ome.tif";
			String imageFile = "C:/_Dev/_Dev_Data/TestImages/testData/meta/OME-Tiff.tif";
      viewMeta(imageFile);
   }
}
