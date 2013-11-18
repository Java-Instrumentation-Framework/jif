    /*
 * ImagingUtils.java
 *
 * Created on March 9, 2006, 3:34 PM
 * Grant B. Harris
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.util;


import edu.mbl.jif.imaging.ImageObject;
import edu.mbl.jif.imaging.tiff.MultipageTiffFile;
import edu.mbl.jif.utils.DialogBox;
import edu.mbl.jif.utils.FileUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferUShort;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import javax.media.jai.ColorCube;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.RegistryMode;
import javax.media.jai.RenderedImageAdapter;
import javax.media.jai.TiledImage;


/**
 * -- PSj.ImageUtils added to this on Oct 11, 2006
 * PSj continues to use psj.ImageUtils
 *
 * @author GBH
 */
public class ImagingUtils {
    /** Creates a new instance of ImagingUtils */
    public ImagingUtils() {
    }

    public static void listReadersWriters() {
        System.out.println("Readers: ----------------- ");
        String[] r = ImageIO.getReaderFormatNames();
        for (int i = 0; i < r.length; i++) {
            System.out.println("    " + r[i]);
        }
        String[] rM = ImageIO.getReaderMIMETypes();
        for (int i = 0; i < rM.length; i++) {
            System.out.println("    " + rM[i]);
        }
        System.out.println("Writers: ----------------- ");
        String[] w = ImageIO.getWriterFormatNames();
        for (int i = 0; i < w.length; i++) {
            System.out.println("    " + w[i]);
        }
        String[] wM = ImageIO.getWriterMIMETypes();
        for (int i = 0; i < wM.length; i++) {
            System.out.println("    " + wM[i]);
        }
    }

    private static OperationRegistry or;

    public static void listJAIRegistry() {
        or = JAI.getDefaultInstance().getOperationRegistry();
        String[] modeNames = RegistryMode.getModeNames();
        String[] descriptorNames;

        for (int i = 0; i < modeNames.length; i++) {
            System.out.println("For registry mode: " + modeNames[i]);

            descriptorNames = or.getDescriptorNames(modeNames[i]);
            for (int j = 0; j < descriptorNames.length; j++) {
                System.out.print("\tRegistered Operator: ");
                System.out.println(descriptorNames[j]);
            }
        }
    }

    static byte[] avgImg = null;

    ////////////////////////////////////////////////////////////////////////////
    // Thumbnail / Scale
    //
    //-----------------------------------------------------------
    // makeThumbnail from a Planar Image
    //
    public static BufferedImage makeThumbnail(PlanarImage _image, float scale) {
        Interpolation interp = null;
        RenderingHints renderHints = null;
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(_image);
        pb.add(scale);
        pb.add(scale);
        pb.add(0.0F);
        pb.add(0.0F);
        if (interp == null) {
            interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        }
        pb.add(interp);
        return JAI.create("scale", pb, renderHints).getAsBufferedImage();
    }

    //---------------------------------------------------------------------
    // makeThumbnail from a Buffered Image
    public static BufferedImage makeThumbnail(BufferedImage _image, float scale) {
        Interpolation interp = null;
        RenderingHints renderHints = null;
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(_image);
        pb.add(scale);
        pb.add(scale);
        pb.add(0.0F);
        pb.add(0.0F);
        if (interp == null) {
            interp = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        }
        pb.add(interp);
        return JAI.create("scale", pb, renderHints).getAsBufferedImage();
    }

    ////////////////////////////////////////////////////////////////////////
    // Image <--> Array
    //
    //
    //---------------------------------------------------------------------
    // Creates a RenderedImage from byte[] array:   byte[] --> RenderedImage
    //
    public static RenderedImage byteArrayToRenderedImage(byte[] imgArray, int width, int height) {
        byte[] image_data;
        BufferedImage image = null;
        int size = width * height;
        image_data = new byte[size];
        image_data = imgArray;
        DataBuffer db = new DataBufferByte(image_data, image_data.length);
        try {
            WritableRaster raster = Raster.createPackedRaster(db, width, height, 8, null);
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            image.setData(raster);
        } catch (Exception e) {
        }
        return (RenderedImage) image;
        // If you need a planar image
        // PlanarImage pi = PlanarImage.wrapRenderedImage(ri)
    }

    //---------------------------------------------------------------------
    // Creates a RenderedImage from short[] array:   short[] --> RenderedImage
    //
    public static RenderedImage shortArrayToRenderedImage(short[] imgArray, int width, int height) {
        byte[] image_data;
        BufferedImage image = null;
        int size = width * height;
        DataBuffer db = new DataBufferUShort(imgArray, imgArray.length);
        try {
            WritableRaster raster = Raster.createPackedRaster(db, width, height, 8, null);
            image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
            image.setData(raster);
        } catch (Exception e) {
        }
        return (RenderedImage) image;
        // If you need a planar image
        // PlanarImage pi = PlanarImage.wrapRenderedImage(ri)
    }

    //----------------------------------------------------------------
    // createImage - create a BufferedImage fron short[]
    public static BufferedImage createImage(int imageWidth, int imageHeight, int dataType,
        Object data) {
        if (dataType == DataBuffer.TYPE_BYTE) {
            return createImage(imageWidth, imageHeight, (byte[]) data);
        } else if (dataType == DataBuffer.TYPE_USHORT) {
            return createImage(imageWidth, imageHeight, (short[]) data);
        } else {
            return null;
        }
    }

    // createImage - create a BufferedImage fron short[]
    public static BufferedImage createImage(int imageWidth, int imageHeight, short[] data) {
        ComponentColorModel ccm = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_GRAY),
                    new int[] { 16 },
                    false,
                    false,
                    Transparency.OPAQUE,
                    DataBuffer.TYPE_USHORT);
        ComponentSampleModel csm = new ComponentSampleModel(
                    DataBuffer.TYPE_USHORT,
                    imageWidth,
                    imageHeight,
                    1,
                    imageWidth,
                    new int[] { 0 });
        DataBuffer dataBuf = new DataBufferUShort((short[]) data, imageWidth);
        WritableRaster wr = Raster.createWritableRaster(csm, dataBuf, new Point(0, 0));
        return new BufferedImage(ccm, wr, true, null);
    }

    // From an ImageJ ShortProcessor...
    /*    BufferedImage bufferedImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
          Raster raster = bufferedImage.getData();
          DataBufferUShort dataBuffer = (DataBufferUShort) raster.getDataBuffer();
          System.arraycopy(src.getPixels(), 0, dataBuffer.getData(), 0,
               dataBuffer.getData().length);
          bufferedImage.setData(raster);
    */

    // createImage - create a BufferedImage fron byte[]
    public static BufferedImage createImage(int imageWidth, int imageHeight, byte[] data) {
        ComponentColorModel ccm = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_GRAY),
                    new int[] { 8 },
                    false,
                    false,
                    Transparency.OPAQUE,
                    DataBuffer.TYPE_BYTE);
        ComponentSampleModel csm = new ComponentSampleModel(
                    DataBuffer.TYPE_BYTE,
                    imageWidth,
                    imageHeight,
                    1,
                    imageWidth,
                    new int[] { 0 });
        DataBuffer dataBuf = new DataBufferByte((byte[]) data, imageWidth);
        WritableRaster wr = Raster.createWritableRaster(csm, dataBuf, new Point(0, 0));
        return new BufferedImage(ccm, wr, true, null);
    }

    //
    //---------------------------------------------------------------------
    public static BufferedImage byteArrayToBufferedImage(byte[] imgArray, int width, int height) {
        int size = width * height;
        BufferedImage image = null;
        byte[] image_data = new byte[size];
        //image_data = imgArray;

        DataBuffer db = new DataBufferByte(image_data, image_data.length);
        try {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            WritableRaster wr = image.getRaster();
            wr.setDataElements(0, 0, width, height, imgArray);
        } catch (Exception e) {
        }
        return image;
        // If you need a planar image
        // PlanarImage pi = PlanarImage.wrapRenderedImage(ri)
    }

    //
    //---------------------------------------------------------------------
    // Feb 2006: This don't work for shorts??'
    public static BufferedImage shortArrayToBufferedImage(short[] imgArray, int width, int height) {
        int size = width * height;
        BufferedImage image = null;
        DataBuffer db = new DataBufferUShort(imgArray, imgArray.length);
        try {
            image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
            WritableRaster wr = image.getRaster();
            wr.setDataElements(0, 0, width, height, db);
        } catch (Exception e) {
        }
        return image;
        // If you need a planar image
        // PlanarImage pi = PlanarImage.wrapRenderedImage(ri)
    }

    //---------------------------------------------------------------------
    // Creates a RenderedImage from short[] array:   short[] --> RenderedImage
    //
    //   public static BufferedImage shortArrayToBufferedImage
    //         (short[] imgArray, int width, int height) {
    //      BufferedImage image = null;
    //      int size = width * height;
    //      DataBuffer db = new DataBufferUShort(imgArray, imgArray.length);
    //      try {
    //         WritableRaster raster = Raster.createPackedRaster(db, width, height, 8, null);
    //         image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
    //         image.setData(raster);
    //      } catch (Exception e) { }
    //      return image;
    //   }

    //---------------------------------------------------------------------
    // RenderedImageToByteArray -   RenderedImage --> byte[]
    public static byte[] renderedImageToByteArray(RenderedImage image) {
        int imageSize = image.getWidth() * image.getHeight();
        PixelGrabber pixGrab;
        RenderedImageAdapter ria = new RenderedImageAdapter(image);
        BufferedImage bi = ria.getAsBufferedImage();
        byte[] pix = new byte[imageSize];
        pix = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        return pix;
    }

    //---------------------------------------------------------------------
    // Create a RenderedImage from a 2D array of Float values
    // float[][] --> RenderedImage
    public static RenderedImage createRenderedImage(float[][] theData, int width, int height,
        int numBands) {
        int len = width * height;
        Point origin = new Point(0, 0);

        // create a float sample model
        SampleModel sampleModel = RasterFactory.createBandedSampleModel(
                    DataBuffer.TYPE_FLOAT,
                    width,
                    height,
                    numBands);

        // create a compatible ColorModel
        ColorModel colourModel = PlanarImage.createColorModel(sampleModel);

        // create a TiledImage using the float SampleModel
        TiledImage tiledImage = new TiledImage(origin, sampleModel, width, height);

        // create a DataBuffer from the float[][] array
        DataBufferFloat dataBuffer = new DataBufferFloat(theData, len);

        // create a Raster
        Raster raster = RasterFactory.createWritableRaster(sampleModel, dataBuffer, origin);

        // set the TiledImage data to that of the Raster
        tiledImage.setData(raster);

        RenderedImageAdapter img = new RenderedImageAdapter((RenderedImage) tiledImage);

        return img;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Write on images...
    //

    //-----------------------------------------------------------
    // dataStripe - Write a string in a black bar along the bottom of the image
    //
    public static RenderedImage dataStripe(RenderedImage rImage, String data) {
        RenderedImageAdapter ria = new RenderedImageAdapter(rImage);
        BufferedImage bImage = ria.getAsBufferedImage();
        return dataStripe(bImage, data);
    }

    public static BufferedImage dataStripe(BufferedImage bImage, String data) {
        Graphics bG = bImage.getGraphics();
        int w = bImage.getWidth();
        int h = bImage.getHeight();
        // adjust font size to match stripe size
        // ###########
        bG.setFont(new Font("SansSerif", Font.PLAIN, 11));
        bG.setColor(Color.black);
        //bG.fillRect(0, (int) ((h * 31) / 32), w, h - (int) ((h * 31) / 32));
        bG.fillRect(0, h - 14, w, h);
        bG.setColor(Color.white);
        bG.drawString(data, 10, h - 3);
        bG.dispose();
        return bImage;
    }

    //-----------------------------------------------------------
    // dataOnImage - Write a title plus an array of strings onto the image
    //
    public static RenderedImage dataOnImage(RenderedImage rImage, String title, String[] data) {
        RenderedImageAdapter ria = new RenderedImageAdapter(rImage);
        BufferedImage bImage = ria.getAsBufferedImage();
        return dataOnImage(bImage, title, data);
    }

    public static BufferedImage dataOnImage(BufferedImage bImage, String title, String[] data) {
        Graphics bG = bImage.getGraphics();
        int w = bImage.getWidth();
        int h = bImage.getHeight();
        int incr = (int) (h / (data.length + 2));

        // draw the Title
        bG.setColor(Color.white);
        bG.drawString(title, (int) (w / 8), incr);

        // draw the strings
        for (int i = 0; i < data.length; i++) {
            bG.setColor(Color.white);
            bG.drawString(data[i], (int) (w / 8), incr * (i + 2));
        }
        return bImage;
    }

    //
    //-----------------------------------------------------------
    // Write a string on the image
    //
    public static RenderedImage StrOnImage(RenderedImage rImage, String data) {
        RenderedImageAdapter ria = new RenderedImageAdapter(rImage);
        BufferedImage bImage = ria.getAsBufferedImage();
        Graphics bG = bImage.getGraphics();
        int w = bImage.getWidth();
        int h = bImage.getHeight();

        //int incr = (int) (h / (data.length + 2));
        bG.setColor(Color.white);
        bG.drawString(data, (int) (w / 8), (int) (h / 8));
        rImage = bImage;

        return rImage;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Conversions
    //
    //////////////////////////////////////////////////////////////////////////
    /// Convert between Color <--> Gray
    //

    /** produce a 3 band luminance image from a 3 band color image */
    public static PlanarImage convertColorToGray(PlanarImage src, int brightness) {
        PlanarImage dst = null;
        double b = (double) brightness;
        double[][] matrix = {
                { .114D, 0.587D, 0.299D, b },
                { .114D, 0.587D, 0.299D, b },
                { .114D, 0.587D, 0.299D, b }
            };
        if (src != null) {
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(src);
            pb.add(matrix);
            dst = JAI.create("bandcombine", pb, null);
        }
        return dst;
    }

    //------------------------------------------------------------------------
    // Produce a 3 band image from a single band gray scale image
    public static PlanarImage convertGrayToColor(PlanarImage src, int brightness) {
        PlanarImage dst = null;
        double b = (double) brightness;
        double[][] matrix = {
                { 1.0D, b },
                { 1.0D, b },
                { 1.0D, b }
            };
        if (src != null) {
            int nbands = src.getSampleModel().getNumBands();

            // MUST check color model here
            if (nbands == 1) {
                ParameterBlock pb = new ParameterBlock();
                pb.addSource(src);
                pb.add(matrix);
                dst = JAI.create("bandcombine", pb, null);
            } else {
                dst = src;
            }
        }
        return dst;
    }

    //------------------------------------------------------------------------
    //  public static BufferedImage convertToIndexColor(BufferedImage src) {
    //    FloydSteinbergFilterOp fsfo = new FloydSteinbergFilterOp();
    //    return fsfo.filter(src, null);
    //  }
    public static BufferedImage convertToIndexColor(BufferedImage src) {
        BufferedImage dst = new BufferedImage(
                    src.getWidth(),
                    src.getHeight(),
                    BufferedImage.TYPE_BYTE_INDEXED);
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(src);
        pb.add(ColorCube.BYTE_496);
        pb.add(KernelJAI.ERROR_FILTER_FLOYD_STEINBERG);
        // Perform the error diffusion operation.
        try {
            RenderedImage imgR = JAI.create("errordiffusion", pb, null);
            dst = PlanarImage.wrapRenderedImage((PlanarImage) imgR).getAsBufferedImage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dst;
    }

    //
    //---------------------------------------------------------------------
    final private byte clamp(int v) {
        if (v > 255) {
            return (byte) 255;
        } else if (v < 0) {
            return (byte) 0;
        } else {
            return (byte) v;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // 4:1 Virtual Averager - halves the size of the image.
    //
    public static void average4to1(byte[] imageOut, byte[] imageIn, int _width, int _height) {
        int w2 = _width / 2;
        int h2 = _height / 2;
        int offset = 0;
        int x2 = 0;

        //byte[] avgImg = new byte[(_width / 2 * _height) / 2];
        for (int y = 0; y < h2; y++) {
            offset = 2 * y * _width;
            for (int x = 0; x < w2; x++) {
                x2 = x * 2;
                imageOut[(y * w2) + x] = (byte) (((int) (imageIn[offset + x2] & 0xFF) +
                    (int) (imageIn[offset + x2 + 1] & 0xFF) +
                    (int) (imageIn[offset + _width + x2] & 0xFF) +
                    (int) (imageIn[offset + _width + x2 + 1] & 0xFF)) / 4);
            }
        }
        return;
    }

    public static byte[] average4to1(byte[] image, int _width, int _height) {
        int w2 = _width / 2;
        int h2 = _height / 2;
        int offset = 0;
        int x2 = 0;

        //byte[]
        avgImg = new byte[(_width / 2 * _height) / 2];
        for (int y = 0; y < h2; y++) {
            offset = 2 * y * _width;
            for (int x = 0; x < w2; x++) {
                x2 = x * 2;
                avgImg[(y * w2) + x] = (byte) (((int) (image[offset + x2] & 0xFF) +
                    (int) (image[offset + x2 + 1] & 0xFF) +
                    (int) (image[offset + _width + x2] & 0xFF) +
                    (int) (image[offset + _width + x2 + 1] & 0xFF)) / 4);
            }
        }
        return avgImg;
    }

    //-----------------------------------------------------------------------------
    // This method returns an Image object from a buffered image
    public static Image toImage(BufferedImage bufferedImage) {
        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }

    //----------------------------------------------------------------------------
    // Get a image from a file using ImageIO
    //
    public static Image getFromFile(String image_file_name) {
        Image image = null;
        try {
            image = ImageIO.read(new File(image_file_name));
        } catch (Exception e) {
            System.out.println("Exception loading: " + image_file_name);
            e.printStackTrace();
        }
        return image;
    }

    //---------------------------------------------------------------------------
    // loadImageArrayList
    public static ArrayList loadImageArrayList(String tiffPathFile) {
        ArrayList imgs = new ArrayList();
        try {
            imgs = MultipageTiffFile.loadImageArrayList(tiffPathFile);
            if (imgs == null) {
                DialogBox.boxError("Could not open image", "file: " + tiffPathFile);
                System.err.println("Could not loadImageArrayList from: " + tiffPathFile);
            }
        } catch (Exception e) {
            System.err.println("Error loading ImageArray from:\n " + tiffPathFile);
            e.printStackTrace();
            imgs = null;
        }
        return imgs;
    }

    public static void loadIntoTabbedViewerFrame(String filename) {
        ArrayList imgs = new ArrayList();
        imgs = loadImageArrayList(filename);
        if (imgs != null) {
            getImageDataType(imgs);
            int width = ((BufferedImage) imgs.get(0)).getWidth();
            int height = ((BufferedImage) imgs.get(0)).getHeight();
            int size = width * height;
            int numImages = imgs.size();
            new edu.mbl.jif.gui.imaging.FrameImageDisplayTabbed(imgs);
        }
    }

    //---------------------------------------------------------------------
    public static int getImageDataType(ArrayList imgs) {
        return getImageDataType((BufferedImage) imgs.get(0));
    }

    public static int getImageDataType(BufferedImage img) {
        int dataType = img.getData().getDataBuffer().getDataType();
        //if (Globals.isDeBug()) {
        System.out.print("ImageLoaded: type= " + dataType + " ) ");
        switch (dataType) {
        case DataBuffer.TYPE_BYTE:
            System.out.print("BYTE");
            break;

        case DataBuffer.TYPE_USHORT:
            System.out.print("USHORT");
            break;

        case DataBuffer.TYPE_SHORT:
            System.out.print("SHORT");
            break;

        case DataBuffer.TYPE_INT:
            System.out.print("INT");
            break;

        case DataBuffer.TYPE_FLOAT:
            System.out.print("FLOAT");
            break;

        case DataBuffer.TYPE_DOUBLE:
            System.out.print("DOUBLE");
            break;

        default:
            System.out.print("unknown type?");
            //  }
            System.out.println();
        }
        return dataType;
    }

    /////////////////////////////////////////////////////////////////////////
    // getImageObject - creates an ImageObject containing the first image
    // from a multipage Tiff file and the number of pages/slices in it.
    //
    public static ImageObject getImageObject(String path, String filename, float thumbScale)
        throws IOException {
        ImageObject _imgObj = null;
        try {
            _imgObj = getImageObject(path + filename, thumbScale);
        } catch (IOException ex) {
            throw ex;
        }
        return _imgObj;
    }

    public static ImageObject getImageObject(String _filename, float thumbScale)
        throws IOException {
        String f = filenameWithTifExt(_filename);
        MultipageTiffFile mf = new MultipageTiffFile(f);
        int numPages = mf.getNumImages();
        BufferedImage bImage = mf.getImage(0);
        if (bImage == null) {
            mf.close();
            mf = null;
            return null;
        } else {
            int sample = 4;
            if (bImage.getWidth() > 800) {
                sample = 5;
            }
            BufferedImage thumb = mf.getAsThumbnail(0, sample);
            ImageObject imageObj = new ImageObject(
                        FileUtil.getJustFilenameNoExt(f),
                        bImage,
                        numPages,
                        thumb,
                        thumbScale);
            mf.close();
            mf = null;
            return imageObj;
        }
    }

    public static String filenameWithTifExt(String filename) {
        return FileUtil.removeExtension(filename) + "." + MultipageTiffFile.TIFF_EXT;
    }
}
