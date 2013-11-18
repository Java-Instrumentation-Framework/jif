package edu.mbl.jif.imaging.tiff;

//import java.io.*;
//import javax.media.jai.*;
//import javax.media.jai.widget.*;
//
//import java.awt.*;
//import java.awt.image.*;
//import java.awt.image.renderable.*;
//
//import com.sun.media.jai.codec.*;

public class Tiff2Pixels
{
//    public static void main (String[] args) {
//        if (args.length != 1) {
//            System.out.println("Usage: java Tiff2Pixels TIFF_image_filename");
//            System.exit( -1);
//        }
//        FileSeekableStream stream = null;
//        try {
//            stream = new FileSeekableStream(args[0]);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            System.exit(0);
//        }
//        ParameterBlock params = new ParameterBlock();
//        params.add(stream);
//        TIFFDecodeParam decodeParam = new TIFFDecodeParam();
//        decodeParam.setDecodePaletteAsShorts(true);
//        RenderedOp image1 = JAI.create("tiff", params);
//        int dataType = image1.getSampleModel().getDataType();
//        if (dataType == DataBuffer.TYPE_BYTE) {
//            System.out.println("TIFF image is type byte.");
//        }
//        else {
//            System.out.println("this data type will be processed :" + dataType);
//            System.exit(0);
//        }
//        int width = image1.getWidth();
//        int height = image1.getHeight();
//        ScrollingImagePanel panel = new ScrollingImagePanel(image1, width,
//                height);
//        Frame window = new Frame("save pixels");
//        window.add(panel);
//        window.pack();
//        window.setVisible(true);
//        encode(image1);
//    }
//
//
//    public static void encode (RenderedImage im) {
//        int width = im.getWidth();
//        int height = im.getHeight();
//        int size = width * height;
//        FileOutputStream fos = null;
//        BufferedOutputStream bos = null;
//        DataOutputStream dos = null;
//        try {
//            System.out.println("Saving Image");
//            fos = new FileOutputStream("image.save");
//            bos = new BufferedOutputStream(fos);
//            dos = new DataOutputStream(bos);
//            dos.writeInt(width);
//            dos.writeInt(height);
//        }
//        catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//        SampleModel sampleModel = im.getSampleModel();
//        int dataType = sampleModel.getTransferType();
//        if (dataType != DataBuffer.TYPE_BYTE) {
//            System.out.println("its not  byte");
//            System.exit( -1);
//        }
//        int numElements = sampleModel.getNumDataElements();
//        System.out.println("datatype:" + dataType + " numElements:" +
//                           numElements);
//        int[] sampleSize = sampleModel.getSampleSize();
//        int numBands = sampleModel.getNumBands();
//        if (numBands != 1) {
//            System.out.println("num bands must be one");
//            System.exit( -1);
//        }
//        int[] pixels = new int[size];
//        try {
//            Raster src = im.getData(new Rectangle(0, 0, width, height));
//            src.getPixels(0, 0, width, height, pixels);
////??      pixels = im.getData().getPixels(0, 0, width, h1,pixels);
//
//            for (int i = 0; i < size; i++) {
//                dos.writeByte((byte) pixels[i]);
//            }
//            System.out.println("total byte written:" + size);
//        }
//        catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//        try {
//            if (dos != null) {
//                dos.close();
//            }
//            if (bos != null) {
//                bos.close();
//            }
//            if (fos != null) {
//                fos.close();
//            }
//        }
//        catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//        System.out.println("Done Saving Image");
//    }
}
