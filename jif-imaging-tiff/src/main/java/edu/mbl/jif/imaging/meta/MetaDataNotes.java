package edu.mbl.jif.imaging.meta;

//import javax.imageio.*;
//import javax.imageio.metadata.*;
//import javax.media.jai.PlanarImage;
////import org.dom4j.Node;
//import java.awt.image.RenderedImage;
//import java.awt.image.BufferedImage;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MetaDataNotes
{
   public MetaDataNotes () {
      
/*
 IIOMetadata

fieldNode.asciisNode
    //Create the little tree corresponding to the Tiff Taf "ImageDescription".
    
    IIOMetadataNode fieldNode = new IIOMetadataNode("TIFFField");
    
    fieldNode.setAttribute("number", "270");
    fieldNode.setAttribute("name", "ImageDescription");
    
    IIOMetadataNode asciisNode = new IIOMetadataNode("TIFFAsciis");
    
    fieldNode.appendChild(asciisNode);
    IIOMetadataNode asciiNode = new IIOMetadataNode("TIFFAscii");
    String descStr = "PS___\n"
        + "zeroIntensity=10\n"
        + "this=that\n"
        + "swing=0.01\n"
        + "etc. etc. \n"
        + " \n";
    asciiNode.setAttribute("value", descStr);
    asciisNode.appendChild(asciiNode);

    //Add this to the image metadata tree, down one level.
    Node tagSetNode = iNode.getFirstChild();
    tagSetNode.appendChild(fieldNode);
*/

 /*
  // reading 1 --> N input images -------------------------------------
  getImageReader
  ImageReader imageReaderN = null; //.... (bmp, jpg, tiff, etc)


  IIOMetadata iioStreamMetadataN = imageReaderN.getStreamMetadata();
  IIOMetadata iioImageMetadataN = imageReaderN.getImageMetadata(0);
  PlanarImage planarImageN =
  PlanarImage.wrapRenderedImage(imageReaderN.readAsRenderedImage(0, null));

  // preparing output image (multi-page tiff) -------------------------
  ImageWriter imageWriter = null; //....
  ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
  boolean canWriteSequence = imageWriter.canWriteSequence();
  imageWriteParam = imageWriter.getDefaultWriteParam();

  // preparing StreamMetadata -----------------------------------------
  IIOMetadata iioStreamMetadata =
              iioStreamMetadata1 == null
                      ? imageWriter.getDefaultStreamMetadata(imageWriteParam)
                      : imageWriter.convertStreamMetadata(iioStreamMetadata1, imageWriteParam);
  if ( canWriteSequence )
      imageWriter.prepareWriteSequence(iioStreamMetadata);

  // writing every single page ----------------------------------------
  ImageTypeSpecifier imageTypeSpecifierN = new ImageTypeSpecifier(planarImageN);
  IIOMetadata iioImageMetadata =
     iioImageMetadataN == null
     ? imageWriter.getDefaultImageMetadata(imageTypeSpecifierN, imageWriteParam)
     : imageWriter.convertImageMetadata(iioImageMetadataN, imageTypeSpecifierN, imageWriteParam);
    IIOImage iioImage = new IIOImage(planarImageN, null, iioImageMetadata);
  if ( canWriteSequence )
      imageWriter.writeToSequence(iioImage, imageWriteParam);
  else
      imageWriter.write(iioStreamMetadata, iioImage, imageWriteParam);
  // end writing ------------------------------------------------------
  if ( canWriteSequence  )
      imageWriter.endWriteSequence();
*/

//===========================================================================================
// Test image

/*
 RenderedImage image = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

  // Get the default metadata for this kind of image
  ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("TIFF").next();
  ImageWriteParam writeParam = imageWriter.getDefaultWriteParam();
  IIOMetadata metadata = 
     imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image), writeParam);

   // The root metadata node is...
   //Node metadataRoot = metadata.getAsTree(metadata.getNativeMetadataFormatName());
*/
      
}
}
/*
The XSD Schema is misleading, it describes the acceptable type of data for an element,
not necessarily that the element contains that data type as children.
I prefer to use DTDs; for example, this one is invalid for IIO Metadata objects because text is a valid child of the element, like this:

<!ELEMENT some-metadata-tag (PCDATA)>

So you could write XML like this; for which the IIOMetadataFormat object has no
description:

<some-metadata-tag>Text,Text,Text</some-metadata-tag>

This is also invalid for the same reason:

<!ELEMENT some-metadata-tag (PCDATA,child-tag)>

Conforming XML might look like this:

<some-metadata-tag>
  Text,Text,Text
  <child-tag/>
</some-metadata-tag>

However, this DTD will work for IIO Metadata objects:

<!ELEMENT some-metadata-tag (child-tag)>
  <!ATTLIST some-metadata-tag value CDATA #REQUIRED>


<some-metadata-tag value="Text,Text,Text">
  <child-tag/>
</some-metadata-tag>
*/
