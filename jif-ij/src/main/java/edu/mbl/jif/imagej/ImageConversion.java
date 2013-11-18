package edu.mbl.jif.imagej;


import ij.ImagePlus;
import ij.io.FileSaver;
import ij.io.Opener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

// Don't know where I got this... 
// See readme below.

public class ImageConversion {
    // 8-bit unsigned integer (0-255).
    public static final int GRAY8 = 0;

    // 16-bit signed integer (-32768-32767). Imported signed images are converted to unsigned by adding 32768.
    public static final int GRAY16_SIGNED = 1;

    // 16-bit unsigned integer (0-65535).
    public static final int GRAY16_UNSIGNED = 2;

    // 32-bit signed integer. Imported 32-bit integer images are converted to floating-point.
    public static final int GRAY32_INT = 3;

    // 32-bit floating-point.
    public static final int GRAY32_FLOAT = 4;

    // 8-bit unsigned integer with color lookup table.
    public static final int COLOR8 = 5;

    // 24-bit interleaved RGB. Import/export only.
    public static final int RGB = 6;

    // 24-bit planer RGB. Import only.
    public static final int RGB_PLANAR = 7;

    // 1-bit black and white. Import only.
    public static final int BITMAP = 8;

    // 32-bit interleaved ARGB. Import only.
    public static final int ARGB = 9;

    // 24-bit interleaved BGR. Import only.
    public static final int BGR = 10;

    // 32-bit unsigned integer. Imported 32-bit integer images are converted to floating-point.
    public static final int GRAY32_UNSIGNED = 11;

    public void batchConvert(String srcDirectory, String outDirectory, String outFormat) {
        String[] list = new File(srcDirectory).list();
        boolean isSuccess = false;
        if (list == null) return;
        for (int i = 0; i < list.length; i++) {
            String filename = list[i];
            File f = new File(srcDirectory + "\\" + filename);
            if (!f.isDirectory()) {
                String outFileName = filename.substring(0, filename.lastIndexOf(".")) + "." + outFormat;
                //File outFile = new File(outDirectory + "\\" + outFileName);
                isSuccess = convertImage(filename, outFileName, outFormat);
                System.out.println("isSuccess: " + isSuccess);
            }
        }
    }

    public boolean convertImage(String inFileName, String outFileName, String outFormat) {
        boolean isSuccess = false;
        BufferedImage bi = null;
        File outputFile = new File(outFileName);
        // Get extension from a File object
        String ext = inFileName.substring(inFileName.lastIndexOf('.') + 1);
        if (canReadExtension(ext)) {
            try {
                //System.out.println("Image IO can read the File:");
                bi = ImageIO.read(new File(inFileName));
                bi.flush();
            } catch (java.io.IOException e) {
            }
        } else {
            try {
                Opener o = new Opener();
                ImagePlus imp = o.openImage(inFileName);
                //System.out.println("Info: " + imp.getProperty("Info"));
                bi = new BufferedImage(imp.getWidth(), imp.getHeight(), getType(imp));
                Graphics g = bi.createGraphics();
                g.drawImage(imp.getImage(), 0, 0, null);
                g.dispose();
                imp.flush();
                bi.flush();
            } catch (NullPointerException e) {
                isSuccess = false;
                e.printStackTrace();
                return isSuccess;
            }
        }
        if (canWriteFormat(outFormat)) {
            try {
                isSuccess = ImageIO.write(bi, outFormat, outputFile);
            } catch (java.io.IOException e) {
            } finally {
            }
        } else {
            FileSaver fs = new FileSaver(new ImagePlus("title", bi));
            if (outFormat.equalsIgnoreCase("jpeg")) fs.saveAsJpeg(outputFile.toString());
            if (outFormat.equalsIgnoreCase("tiff")) fs.saveAsTiff(outputFile.toString());
            isSuccess = false;
        }
        System.out.println("isSuccess: " + isSuccess);
        return isSuccess;
    }

    //Returns true if the specified file extension can be read
    public static boolean canReadExtension(String fileExt) {
        Iterator iter = ImageIO.getImageReadersBySuffix(fileExt);
        return iter.hasNext();
    }

    // Returns true if the specified format name can be written
    public static boolean canWriteFormat(String formatName) {
        Iterator iter = ImageIO.getImageWritersByFormatName(formatName);
        return iter.hasNext();
    }

    public static void printReadFormats() {
        String[] formatNames = ImageIO.getReaderFormatNames();
        formatNames = unique(formatNames);
        for (int i = 0; i < formatNames.length; i++)
            System.out.println(formatNames[i]);
    }

    public static void printWriteFormats() {
        String[] formatNames = ImageIO.getWriterFormatNames();
        formatNames = unique(formatNames);
        for (int i = 0; i < formatNames.length; i++)
            System.out.println(formatNames[i]);
    }

    public static String[] unique(String[] strings) {
        Set set = new HashSet();
        for (int i = 0; i < strings.length; i++) {
            String name = strings[i].toLowerCase();
            set.add(name);
        }
        return (String[]) set.toArray(new String[0]);
    }

    // Returns an integer corresponding to the TYPE of BufferedImage
    private int getType(ImagePlus imp) {
        if (imp == null) return -1;
//        try {
//            switch (imp.getFileInfo().getFileType()) {
//                case GRAY8:
//                    return BufferedImage.TYPE_BYTE_GRAY;
//                case GRAY16_SIGNED:
//                    return BufferedImage.TYPE_USHORT_GRAY;
//                case GRAY16_UNSIGNED:
//                    return BufferedImage.TYPE_USHORT_GRAY;
//                case GRAY32_INT:
//                    return BufferedImage.TYPE_INT_RGB;
//                case GRAY32_UNSIGNED:
//                    return BufferedImage.TYPE_INT_RGB;
//                case GRAY32_FLOAT:
//                    return BufferedImage.TYPE_INT_RGB;
//                case COLOR8:
//                    return BufferedImage.TYPE_INT_RGB;
//                case RGB:
//                    return BufferedImage.TYPE_INT_RGB;
//                case RGB_PLANAR:
//                    return BufferedImage.TYPE_INT_RGB;
//                case BITMAP:
//                    return BufferedImage.TYPE_INT_RGB;
//                case ARGB:
//                    return BufferedImage.TYPE_INT_ARGB;
//                case BGR:
//                    return BufferedImage.TYPE_INT_BGR;
//                default:
//                    return 1;
//            }
//        } catch (Exception e) {
//            System.out.println("Exception caught::DicomConverter::getType");
//        }
        return -1;
    }

    // Creates the Medatadata hashtable using the keystring provided
    protected Hashtable getMetaData(ImagePlus imp, String keys) {

        if (imp == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(keys, "|");
        ArrayList hashtableKeys = new ArrayList();
        ArrayList dicomKeys = new ArrayList();
        while (st.hasMoreTokens()) {
            hashtableKeys.add(st.nextToken());
            if (!st.hasMoreTokens())
                dicomKeys.add(null);
            else
                dicomKeys.add(st.nextToken());
        }
        // Gets the metadata as a string by invoking method on ImagePlus object.
        String s = (String) imp.getProperty("Info");
        System.out.println("Info: " + imp.getProperty("Info"));
        Hashtable ImageMetaData = new Hashtable();
        for (int i = 0; i < hashtableKeys.size(); i++) {
            try {
                String value = s.substring(s.indexOf(dicomKeys.get(i).toString()));
                ImageMetaData.put(hashtableKeys.get(i).toString(), value.substring(value.indexOf(": ") + 2, value.indexOf("\n")).trim());
            } catch (StringIndexOutOfBoundsException e) {
                if (hashtableKeys.get(i) != null) ImageMetaData.put(hashtableKeys.get(i).toString(), "");
            }
        }
        return ImageMetaData;
    }

    public static void main(String[] args) {
        //printReadFormats();
        //printWriteFormats();
        ImageConversion ic = new ImageConversion();
        //ic.convertImage("C:\\programs\\Projects\\jdjImage\\input\\image1.dcm", "C:\\programs\\Projects\\jdjImage\\output\\image1.jpg", "jpeg-lossless");
        ic.convertImage(args[0],args[1],args[2]);
    }

}
/*
1. Unzip all files in a directory say C:\temp\jdj
2. Download JAI Image I/O Tools from http://java.sun.com/products/java-media/jai/downloads/download-iio.html.
3. Extract all jar files and dll files in the same directory (say C:\temp\jdj)
4. Only following jar/dll files are needed to be present in the same directory:
   i)   clibwrapper_jiio.jar
   ii)  jai_imageio.jar
   iii) mlibwrapper_jai.jar
   iv)  clib_jiio.dll
    V)  ij.jar
2. Run run.bat, you need jdk1.4 or higher
3. To change the input files and output format edit run.bat's following command:
   java -Dlibrary=. ImageConversion image1.dcm image2.jpg jpeg2000
   to 
   java -Dlibrary=. ImageConversion yourInputfileName yourOutputFileName yourFormat

   The available output formats are:
   tif,tiff,jfif,wbmp,jpeg-lossless,jpeg2000,raw,bmp,jpeg,jpeg 2000,jpeg-ls,pnm,png,jpg
 */