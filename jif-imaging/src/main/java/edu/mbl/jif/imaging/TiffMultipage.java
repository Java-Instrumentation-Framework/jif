package edu.mbl.jif.imaging;

import com.sun.media.jai.codec.*;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;

import javax.media.jai.JAI;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.io.*;
import edu.mbl.jif.utils.DialogBoxI;

// This is based, in part, on Multipagetiff.java 1.0 2001/10/4
// Copyright (c) 2001 Larry Rodrigues

//import  com.sun.media.jai.codec.SeekableStream;
//import  com.sun.media.jai.codec.FileSeekableStream;
//import  com.sun.media.jai.codec.TIFFDecodeParam;
//import  com.sun.media.jai.codec.ImageDecoder;
//import  com.sun.media.jai.codec.ImageCodec;
//
public class TiffMultipage
{
   //////////////////////////////////////////////////////////////////
   // ??
   //
   public static PlanarImage readAsPlanarImage (String filename) {
      return JAI.create("fileload", filename);
   }


   //////////////////////////////////////
   // Save one (1) image in a TIFF file
   //
   public static void saveAsTIFF (RenderedImage image,
                                  String file) throws java.io.IOException {
      String filename = file;
      if (!filename.endsWith(MultipageTiffFile.TIFF_EXT)) {
         filename = new String(file + "." + MultipageTiffFile.TIFF_EXT);
      }
      OutputStream out = new FileOutputStream(filename);
      TIFFEncodeParam param = new TIFFEncodeParam();
      ImageEncoder encoder =
            ImageCodec.createImageEncoder("TIFF", out, param);
      encoder.encode(image);
      out.close();
   }


   /////////////////////////////////////////////////////////////////////
   // saveAsMultipageTIFF - Save multiple images in a TIFF file
   //
   public static void saveAsMultipageTIFF (RenderedImage[] image,
         String file) throws java.io.IOException {
      OutputStream out = null;
      String filename = file;
      if (!filename.endsWith(MultipageTiffFile.TIFF_EXT)) {
         filename = new String(file + "." + MultipageTiffFile.TIFF_EXT);
      }
      try {
         out = new FileOutputStream(filename);
         TIFFEncodeParam param = new TIFFEncodeParam();
         ImageEncoder encoder =
               ImageCodec.createImageEncoder("TIFF", out, param);
         Vector vector = new Vector();
         for (int i = 1; i < image.length; i++) {
            vector.add(image[i]);
         }
         param.setExtraImages(vector.iterator());
         encoder.encode(image[0]);
      }
      catch (Exception ex) {
         DialogBoxI.boxError("Error in saveAsMultipageTIFF",
                             "Exception: " + ex.getMessage());
         ex.printStackTrace();
      }
      finally {
         if (out != null) {
            out.close();
            out = null;
         }
      }
   }


   /////////////////////////////////////////////////////////////////////
   // createMultipageTiff - Creates a MultipageTiff from a set of image files
   //
   public void createMultipageTiff (String[] filenames) {
      RenderedImage[] image = new PlanarImage[filenames.length];
      for (int i = 0; i < filenames.length; i++) {
         image[i] = readAsPlanarImage(filenames[i]);
      }
      try {
         saveAsMultipageTIFF(image, "multipagetiff");
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   /////////////////////////////////////////////////////////////////////
   // readMultiPageTiff - Reads a multipage Tiff file into an array
   // of RenderedImages
   //
   public static RenderedImage[] readMultiPageTiff (String filename) throws
         IOException {
      File file;
      SeekableStream ss = null;
      TIFFDecodeParam decodeParam;
      ImageDecoder decoder;
      int numPages;
      RenderedImage[] rImage = null;
      try {
         file = new File(filename);
         ss = new FileSeekableStream(file);
         //
         decodeParam = new TIFFDecodeParam();
         decodeParam.setDecodePaletteAsShorts(true);
         //
         decoder = ImageCodec.createImageDecoder("tiff", ss, null);
         numPages = decoder.getNumPages();
         rImage = new RenderedImage[numPages];
         for (int i = 0; i < numPages; i++) {
            rImage[i] =
                  new NullOpImage(decoder.decodeAsRenderedImage(i), null,
                                  OpImage.OP_IO_BOUND, null);
         }
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
      finally {
//      if(ss != null)
//        ss.close();
//      ss = null;
//      decodeParam = null;
//      decoder = null;
      }
      return rImage;
   }


   /////////////////////////////////////////////////////////////////////
   // Reads the first image from a multipage Tiff file into a RenderedImage
   //
   public static RenderedImage readFirstPageOfTiff (String filename) throws
         IOException {
      File file = new File(filename);
      SeekableStream ss = new FileSeekableStream(file);
      ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
      int numPages = decoder.getNumPages();
      RenderedImage rImage = decoder.decodeAsRenderedImage(0);
      ss.close();
      ss = null;
      decoder = null;
      rImage = null;
      return rImage;
   }


   /////////////////////////////////////////////////////////////////////////
   // getImageObject - creates an ImageObject containing the first image
   // from a multipage Tiff file and the number of pages/slices in it.
   //
   public static ImageObject getImageObject (String path, String filename,
         float thumbScale) throws IOException {
      ImageObject _imgObj = null;
      try {
         _imgObj = getImageObject(path + filename, thumbScale);
      }
      catch (IOException ex) {
         throw ex;
      }
      return _imgObj;
   }


   public static ImageObject getImageObject (String path, float thumbScale) throws
         IOException {
      if (path.indexOf(".tif") < 0) {
         path = new String(path + "." + MultipageTiffFile.TIFF_EXT);
      }
      File file = new File(path);
      SeekableStream ss = new FileSeekableStream(file);
      ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
      int numPages = decoder.getNumPages();
      RenderedImage rImage = decoder.decodeAsRenderedImage(0);
      /* Or...
           RenderedImage op =
           new NullOpImage(dec.decodeAsRenderedImage(imageToLoad),
                      null,OpImage.OP_IO_BOUND, null);
       */
      PlanarImage pImage = PlanarImage.wrapRenderedImage(rImage);
      BufferedImage bImage = pImage.getAsBufferedImage();

      // limit size of thumbnail images
      if (bImage.getHeight() > 600) {
         thumbScale = (thumbScale * 600) / bImage.getHeight();
      }
      ImageObject imageObj = null;
//        new ImageObject(file.getName(), bImage, numPages, thumbScale);
      file = null;
      ss.close();
      ss = null;
      decoder = null;
      rImage = null;
      pImage = null;
      bImage = null;
      System.gc();
      return imageObj;
   }


   //////////////////////////////////////////////////////////////////
   // Read Images from Multipage TIFF on-demand...
   //
   static File _file;
   static SeekableStream _ss;
   static TIFFDecodeParam _decodeParam;
   static ImageDecoder _decoder;
   static int _numPages;
   //----------------------------------------------------------------
   public static int openFile (String filename) {
      try {
         _file = new File(filename);
         _ss = new FileSeekableStream(_file);
         _decodeParam = new TIFFDecodeParam();
         _decodeParam.setDecodePaletteAsShorts(true);
         _decoder = ImageCodec.createImageDecoder("tiff", _ss, null);
         _numPages = _decoder.getNumPages();
      }
      catch (Exception ex) {
         ex.printStackTrace();
         return -1; // error
      }
      return _numPages; // number of images in TIFF
   }


   //----------------------------------------------------------------
   public static RenderedImage readImage (int page) throws Exception {
      RenderedImage rImage = null;
      if (page < _numPages) {
         rImage =
               new NullOpImage(_decoder.decodeAsRenderedImage(page), null,
                               OpImage.OP_IO_BOUND, null);
      }
      return rImage;
   }


   //----------------------------------------------------------------
   public static void closeFile () {
      try {
         _file = null;
         _ss.close();
         _ss = null;
         _decoder = null;
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
      finally {
         System.gc();
      }
   }


   ////////////////////////////////////////////////////////////////////////////
   // readBufferedImageArrayFromTiff - Reads a multipage Tiff file into ImageStackBuffer
   //
   public static BufferedImage[] readBufferedImageArrayFromTiff (String filename) throws
         IOException {
      PlanarImage pImage = null;
      File file = new File(filename);
      SeekableStream ss = new FileSeekableStream(file);
      //
      TIFFDecodeParam decodeParam = new TIFFDecodeParam();
      decodeParam.setDecodePaletteAsShorts(true);
      //
      ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
      int numPages = decoder.getNumPages();
      RenderedImage[] rImage = new RenderedImage[numPages];
      for (int i = 0; i < numPages; i++) {
         rImage[i] =
               new NullOpImage(decoder.decodeAsRenderedImage(i), null,
                               OpImage.OP_IO_BOUND, null);
      }
      BufferedImage[] bImage = new BufferedImage[numPages];
      for (int i = 0; i < numPages; i++) {
         pImage = PlanarImage.wrapRenderedImage(rImage[i]);
         bImage[i] = pImage.getAsBufferedImage();
         //System.out.println("readBufferedImageArrayFromTiff.bimage= " + bImage);
      }
      file = null;
      ss.close();
      ss = null;
      decoder = null;
      rImage = null;
      pImage = null;
      System.gc();
      return bImage;
   }


   /////////////////////////////////////////////////////////////////////////
   // getFirstBufferedImage -
   //
   public static BufferedImage getFirstBufferedImage (String filePath) throws
         IOException {
      BufferedImage bImage;
      File file = new File(filePath);
      SeekableStream ss = new FileSeekableStream(file);
      ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
      int numPages = decoder.getNumPages();
      RenderedImage rImage = decoder.decodeAsRenderedImage(0);
      PlanarImage pImage = PlanarImage.wrapRenderedImage(rImage);
      bImage = pImage.getAsBufferedImage();
      file = null;
      ss.close();
      ss = null;
      decoder = null;
      rImage = null;
      pImage = null;
      //    System.gc();
      return bImage;
   }

   // Regarding memory management:
   // "You can try setting planarImage, pb, bufferedImage, and mediaTracker
   // to null then call System.gc();"
   // bis.close();
   // bis=null;
   //  System.gc();
   //    try {
   //    Thread.sleep(10);
   //    } catch (InterruptedException e) {
   //    }
   /////////////////////////////////////////////////////////////////////////////
   // TiffMultipage

   /*  public void saveImageToFile(Image img, String filename) {
     // Define the source and destination file names.
     String outputFile = filename;
     // Load the input image.
     RenderedOp src = JAI.create("fileload", inputFile);
     // Encode the file as a BMP image.
     FileOutputStream stream =
     new FileOutputStream(outputFile);
     JAI.create("encode", src, stream, BMP, null);
     // Store the image in the BMP format.
     JAI.create("filestore", src, outputFile, BMP, null);
      }
    */
   /*
      Compression when saving/reading
      param.setCompression(TIFFEncodeParam.COMPRESSION_PACKBITS);
      param.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE);
      param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP3_1D);
      param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP3_2D);
      param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
      param.setCompression(TIFFEncodeParam.COMPRESSION_JPEG_TTN2);
      param.setCompression(TIFFEncodeParam.COMPRESSION_LZW);
      param.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
    */

   //byte[] array = ((DataBufferByte)raster.getDataBuffer()).getData();

//----------------------------------------------------------------
// > > > >   T A G S
//
   /*
      Iterator it = ImageIO.getImageReadersByFormatName("tif");
      if (it.hasNext()) {
      ImageReader ir = ImageReader it.next();
      ImageInputStream iis = ImageIO.createImageInputStream(new File(<your tiff file>));
      ir.setInput(iis, true);
      TIFFImageMetadata iiom = (TIFFImageMetadata) ir.getImageMetadata(0);
      System.out.println("resolution= " + iiom.getTIFFField(<your tag number. For example 256 or 296>).getAsInt(0));
      }
    */

   //...to combine two image with particulars tags
   /*
         TIFFEncodeParam encodeParam2=new TIFFEncodeParam();
    encodeParam.setTileSize(image[0].getTileWidth(), image[0].getTileHeight());
    encodeParam.setExtraFields(getExtraFields(TD[0]));
    encodeParam.setLittleEndian(true);
    encodeParam2.setTileSize(image[1].getTileWidth(), image[1].getTileHeight());
    encodeParam2.setExtraFields(getExtraFields(TD[1]));
    encodeParam2.setLittleEndian(true);
    Vector vector = new Vector();
    Object[] obj=new Object[2];
    obj[0]=image[1];
    obj[1]=encodeParam2;
    vector.add(obj);
    encodeParam.setExtraImages(vector.iterator());
    */

   /*
       /*
     * Copyright (c) 2001 Sun Microsystems, Inc. All Rights Reserved.
     *
     * Redistribution and use in source and binary forms, with or without
     * modification, are permitted provided that the following conditions are met:
     *
     * -Redistributions of source code must retain the above copyright notice, this
     * list of conditions and the following disclaimer.
     *
     * -Redistribution in binary form must reproduce the above copyright notice,
     * this list of conditions and the following disclaimer in the documentation
     * and/or other materials provided with the distribution.
     *
     * Neither the name of Sun Microsystems, Inc. or the names of contributors may
     * be used to endorse or promote products derived from this software without
     * specific prior written permission.
     *
     * This software is provided "AS IS," without a warranty of any kind. ALL
     * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
     * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
     * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
     * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
     * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
     * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
     * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
     * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
     * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
     * POSSIBILITY OF SUCH DAMAGES.
     *
     * You acknowledge that Software is not designed,licensed or intended for use
     * in the design, construction, operation or maintenance of any nuclear
     * facility.
     */
    /*
           import java.awt.*;
     import java.awt.image.*;
     import java.awt.image.renderable.*;
     import java.io.*;
     import java.util.*;
     import javax.media.jai.*;
     import javax.media.jai.widget.*;
     import com.sun.media.jai.codec.*;
     import com.sun.media.jai.codecimpl.*;
//Reads all specified files and stores them into a single multi-page TIFF
//file called "multipage.tif" which is them read and displayed.
//Usage: java TIFFIOTest file1 [file2 [file3 ...]]
     public class TIFFIOTEST extends Frame {
     public static void main(String[] args) {
     new TIFFIOTEST(args);
     }
     public TIFFIOTEST(String[] fileNames) {
     RenderedImage[] srcs = new RenderedImage[fileNames.length];
     ParameterBlock pb = (new ParameterBlock());
     pb.add(fileNames[0]);
     RenderedImage src0 = JAI.create("fileload", pb);
     ArrayList list = new ArrayList(srcs.length - 1);
     for(int i = 1; i < srcs.length; i++) {
     pb = (new ParameterBlock());
     pb.add(fileNames);
     list.add(JAI.create("fileload", pb));
     }
     TIFFEncodeParam param = new TIFFEncodeParam();
     param.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE);
     param.setExtraImages(list.iterator());
     pb = (new ParameterBlock());
     pb.addSource(src0);
     pb.add("multipage.tif").add("tiff").add(param);
     JAI.create("filestore", pb);
     int rowcol = (int)(Math.sqrt(srcs.length) + 0.5);
     setLayout(new GridLayout(rowcol, rowcol));
     SeekableStream stream = null;
     try {
     stream = new FileSeekableStream("multipage.tif");
     } catch(Exception e) {
     }
     pb = (new ParameterBlock());
     pb.add(stream).add(new TIFFDecodeParam()).add(new Integer(0));
     for(int i = 0; i < srcs.length; i++) {
     pb.set(new Integer(i), 2);
     RenderedImage src = JAI.create("tiff", pb);
     add(new ScrollingImagePanel(src, src.getWidth(), src.getHeight()));
     }
     pack();
     setVisible(true);
     }
     }
     */

    /*
     import java.util.*;
     import java.io.*;
     import java.awt.image.RenderedImage;
     import java.awt.image.renderable.ParameterBlock;
     import javax.media.jai.*;
     import javax.media.jai.widget.*;
     import com.sun.media.jai.codec.*;
     public class MultiPageSave
     {
     public static List getImageList(String a_filename) throws IOException
     {
     File file = new File(a_filename);
     SeekableStream s = new FileSeekableStream(file);
     System.out.println("Number of images in the TIFF named=" + a_filename + ":" +TIFFDirectory.getNumDirectories(s));
     ParameterBlock pb = new ParameterBlock();
     pb.add(s);
     TIFFDecodeParam param = new TIFFDecodeParam();
     pb.add(param);
     List imageList = new ArrayList();
     long nextOffset = 0;
     do
     {
     RenderedOp op = JAI.create("tiff", pb);
     imageList.add(op);
     TIFFDirectory dir = (TIFFDirectory)op.getProperty("tiff_directory");
     nextOffset = dir.getNextIFDOffset();
     if(nextOffset != 0)
     {
     param.setIFDOffset(nextOffset);
     }
     }
     while(nextOffset != 0);
     return imageList;
     }
     public static List getImageList(List a_inputFileList) throws IOException
     {
     List imageList = new ArrayList();
     Iterator inputIterator = a_inputFileList.iterator();
     while (inputIterator.hasNext())
     {
     String filename = (String) inputIterator.next();
     imageList.addAll(getImageList(filename));
     }
     return imageList;
     }
     public static void save(List a_imageList, String a_filename)
     {
     if (!a_imageList.isEmpty())
     {
     RenderedImage image = (RenderedImage) a_imageList.get(0);
     TIFFEncodeParam encodeParam = new TIFFEncodeParam();
     encodeParam.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
     if (a_imageList.size() > 1)
     {
     encodeParam.setExtraImages(a_imageList.listIterator(1));
     }
     String filetype = "TIFF";
     RenderedOp op = JAI.create("filestore", image, a_filename,
     filetype, encodeParam);
     op.dispose();
     }
     }
     public static void main(String [] args)
     {
     if (args.length > 0)
     {
     try
     {
     MultiPageSave.save(getImageList(Arrays.asList(args)), "multi.tif");
     }
     catch (IOException ex)
     {
     ex.printStackTrace();
     }
     }
     else
     {
         System.out.println("MultiPageSave <filename> [ <filename2> <filename3>... ]");
     }
     }
     }
     */


    /*
           public static void save(RenderedImage[] rImage,
           String file) throws java.io.IOException {
         OutputStream out = null;
         String filename = file;
         if (!filename.endsWith(PSj.tiffExt)) {
           filename = new String(file + "." + PSj.tiffExt);
         }

         try {
           long time = edu.mbl.jif.utils.time.TimerHR.currentTimeMillis();
           //String ext = filename.substring(filename.lastIndexOf('.') + 1);
           String ext = "tif";
           Iterator writers = ImageIO.getImageWritersByFormatName(ext);
           ImageWriter writer = (ImageWriter) writers.next();
           ImageOutputStream ios =
               ImageIO.createImageOutputStream(new File(filename));
           writer.setOutput(ios);
           if (writer.canWriteSequence()) { //	i.e tiff, sff(fax)
             writer.prepareWriteSequence(null);
             for (int i = 0; i < rImage.length; i++) {
               IIOImage iioimg = new IIOImage(rImage[i], null, null);
               writer.writeToSequence(iioimg, null);
             }
             writer.endWriteSequence();
           }
           time = edu.mbl.jif.utils.time.TimerHR.currentTimeMillis() - time;
           System.err.println("Saved : " + filename);
           System.err.println("Time used to save images : " + time);
         } catch (Exception e) {
           System.out.println("Image Save Error : " + e.getMessage());
           e.printStackTrace();
         }
       }
     */


}
