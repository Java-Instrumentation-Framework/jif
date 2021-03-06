package edu.mbl.jif.imagej;

//import edu.mbl.jif.imaging.ImgInfoDumper;

import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.Calibration;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.ShortProcessor;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import org.dart.imagej.IJClient;
import org.dart.imagej.IJClientFactory;




public class IJTools {
    public static void main(String[] args) {
        //        //copyFiles();
        //        setWorkingDir("");
        //        
        //        invokeImageJ();
    }

    private static void setWorkingDir(String dir) {
        String workDir = System.getProperty("user.dir");
        System.out.println("WorkDir=" + workDir);
        //System.setProperty("user.dir", dir);        
    }

    public static void copyFiles() {
        String from = "";
        String to = "";
        try {
            copyFile(from, to, true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // === Open ImageJ =========================================================
    public static void invokeImageJ() {
        dispatchToEDT(new Runnable() {
                public void run() {
                    openImageJ();
                }
            });
    }

    public static void openImageJ() {
          IJClient ijClient = IJClientFactory.getIJClient(false);
//        ImageJ ij = IJ.getInstance();
//        if ((ij == null) || ((ij != null) && !ij.isShowing())) {
//            if (IJ.isMacOSX()) {
//                System.setProperty("com.apple.mrj.application.growbox.intrudes", "true");
//                ij = new ImageJ(null);
//                System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
//            } else {
//                ij = new ImageJ(null);
//            }
//        }
//        if (ij != null) {
//            ij.setVisible(true);
//        }
    }

    public static void dispatchToEDT(Runnable runnable) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            runnable.run();
        }
    }

    // Make ImageJ stack from array of BufferedImages
    public static ImagePlus makeStack(String name, BufferedImage[] rImage) {
        ImagePlus imp = null;
        if (name == null) {
            name = "None";
        }
        if (rImage.length >= 1) {
            ImageStack stack = new ImageStack(rImage[0].getWidth(), rImage[0].getHeight());
            for (int i = 0; i < (rImage.length); i++) {
                DataBuffer dBuff = rImage[i].getData().getDataBuffer();
                ColorModel cm = rImage[i].getColorModel();
                try {
                    ImageProcessor ip = createProcessor(rImage[i].getWidth(),
                            rImage[i].getHeight(), dBuff, cm);
                    stack.addSlice(String.valueOf(i - 1), ip);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //stack.addSlice();
            }
            if (stack == null) {
                return null;
            }
            if (stack.getSize() == 0) {
                return null;
            }
            imp = new ImagePlus(name, stack);
        } else { // single image
            imp = new ImagePlus(name, (Image) rImage[0]);
        }
        imp.show();
        // setResolution(fi, imp);
        return imp;
    }

    // Make ImageJ stack from array of RenderedImages
    public static ImagePlus makeStack(String name, RenderedImage[] rImage) {
        Object pixels = null;
        ImagePlus imp = null;
        if (name == null) {
            name = "None";
        }
        if (rImage.length > 1) {
            ImageStack stack = new ImageStack(rImage[0].getWidth(), rImage[0].getHeight());
            for (int i = 0; i < (rImage.length); i++) {
                DataBuffer dBuff = rImage[i].getData().getDataBuffer();
                ColorModel cm = rImage[i].getColorModel();
                try {
                    ImageProcessor ip = createProcessor(rImage[i].getWidth(),
                            rImage[i].getHeight(), dBuff, cm);
                    stack.addSlice(String.valueOf(i - 1), ip);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //stack.addSlice();
            }
            if (stack == null) {
                return null;
            }
            if (stack.getSize() == 0) {
                return null;
            }
            imp = new ImagePlus(name, stack);
        } else { // single image
            imp = new ImagePlus(name, (Image) rImage[0]);
        }
        imp.show();
        // setResolution(fi, imp);
        return imp;
    }

    //
    //---------------------------------------------------------------------
    public static ImagePlus makeImagePlus(String name, java.awt.Image awtImage) {
        ImagePlus iPlus = new ImagePlus(name, awtImage);
        ByteProcessor ip = (ByteProcessor) iPlus.getProcessor();
        return iPlus;
        // ImagePlus createBtyeImage(title, w, h, slices, fill)
    }

    //
    //---------------------------------------------------------------------------
    public static double[] getLinePixels(String name, java.awt.Image awtImage, Rectangle roi) {
        ImagePlus iPlus = new ImagePlus(name, awtImage);
        ByteProcessor ip = (ByteProcessor) iPlus.getProcessor();
        return ip.getLine((double) roi.getX(), (double) (roi.getX() + roi.getWidth()),
            (double) roi.getY(), (double) (roi.getY() + roi.getHeight()));
    }

    //===========================================================================
    /**
     *  Create an ImageProcessor object from a DataBuffer.
     *
     * @param  w              Image width.
     * @param  h              Image height.
     * @param  buffer         Data buffer.
     * @param  cm             Color model.
     * @return                Image processor object.
     * @exception  Exception  If data buffer is in unknown format.
     * From ijImageIO
     */
    public static ImageProcessor createProcessor(int w, int h, DataBuffer buffer, ColorModel cm)
        throws Exception {
        if (buffer.getOffset() != 0) {
            throw new Exception("Expecting BufferData with no offset.");
        }
        switch (buffer.getDataType()) {
        case DataBuffer.TYPE_BYTE:
            return new ByteProcessor(w, h, ((DataBufferByte) buffer).getData(), cm);
        case DataBuffer.TYPE_USHORT:
            return new ShortProcessor(w, h, ((DataBufferUShort) buffer).getData(), cm);
        case DataBuffer.TYPE_SHORT:
            short[] pixels = ((DataBufferShort) buffer).getData();
            for (int i = 0; i < pixels.length; ++i) {
                pixels[i] = (short) (pixels[i] + 32768);
            }
            return new ShortProcessor(w, h, pixels, cm);
        case DataBuffer.TYPE_INT:
            return new FloatProcessor(w, h, ((DataBufferInt) buffer).getData());
        case DataBuffer.TYPE_FLOAT: {
            DataBufferFloat dbFloat = (DataBufferFloat) buffer;
            return new FloatProcessor(w, h, dbFloat.getData(), cm);
        }
        case DataBuffer.TYPE_DOUBLE:
            return new FloatProcessor(w, h, ((DataBufferDouble) buffer).getData());
        case DataBuffer.TYPE_UNDEFINED:
            throw new Exception("Pixel type is undefined.");
        default:
            throw new Exception("Unrecognized DataBuffer data type");
        }
    }

    //===========================================================================
    public static ImagePlus[] makeImagePlusArray(BufferedImage[] imageArray, String title)
        throws Exception {
        // Get number of sub images
        int nbPages = imageArray.length;
        if (nbPages < 1) {
            throw new Exception("Image decoding problem. " +
                "Image file has less then 1 page. Nothing to decode.");
        }

        // Iterate through pages
        ArrayList imageList = new ArrayList();
        for (int i = 0; i < nbPages; ++i) {
            imageList.add(create(imageArray[i].getRaster(), null, "Something"));
        }
        ImagePlus[] images = (ImagePlus[]) imageList.toArray(new ImagePlus[imageList.size()]);
        if (nbPages == 1) {
            // Do not use page numbers in image name
            images[0].setTitle(title);
        } else {
            // Attempt to combine images into a single stack.
            ImagePlus im = combineImages(images);
            if (im != null) {
                im.setTitle(title);
                images = new ImagePlus[1];
                images[0] = im;
            }
        }
        return images;
    }

    /**
     *  Attempt to combine images into a single stack. Images can be combined into
     *  a stack if all of them are single slice images of the same type and
     *  dimensions.
     * @param  images  Array of images.
     * @return         Input images combined into a stack. Return null if images
     *      cannot be combined.
     */
    public static ImagePlus combineImages(ImagePlus[] images) {
        if ((images == null) || (images.length <= 1)) {
            return null;
        }
        if (images[0].getStackSize() != 1) {
            return null;
        }
        int fileType = images[0].getFileInfo().fileType;
        int w = images[0].getWidth();
        int h = images[0].getHeight();
        ImageStack stack = images[0].getStack();
        for (int i = 1; i < images.length; ++i) {
            ImagePlus im = images[i];
            if (im.getStackSize() != 1) {
                return null;
            }
            if ((fileType == im.getFileInfo().fileType) && (w == im.getWidth()) &&
                    (h == im.getHeight())) {
                stack.addSlice(null, im.getProcessor().getPixels());
            } else {
                return null;
            }
        }
        images[0].setStack(images[0].getTitle(), stack);
        return images[0];
    }

    /**
     *  Create instance of ImagePlus from WritableRaster r and ColorModel cm.
     *
     * @param  r              Raster containing pixel data.
     * @param  cm             Image color model (can be null).
     * @return                ImagePlus object created from WritableRaster r and
     *      ColorModel cm
     * @exception  Exception  when enable to create ImagePlus.
     */
    public static ImagePlus create(WritableRaster r, ColorModel cm, String title)
        throws Exception {
        DataBuffer db = r.getDataBuffer();
        int numBanks = db.getNumBanks();
        if ((numBanks > 1) && (cm == null)) {
            throw new Exception("Don't know what to do with image with no " +
                "color model and multiple banks.");
        }
        SampleModel sm = r.getSampleModel();
        int dbType = db.getDataType();
        if ((numBanks > 1) || (sm.getNumBands() > 1)) {
            // If image has multiple banks or multiple color components, assume that it
            // is a color image and relay on AWT for proper decoding.
            BufferedImage bi = new BufferedImage(cm, r, false, null);
            return new ImagePlus(null, new ColorProcessor((Image) bi));
        } else if (sm.getSampleSize(0) < 8) {
            // Temporary fix for less then 8 bit images
            BufferedImage bi = new BufferedImage(cm, r, false, null);
            return new ImagePlus(null, new ByteProcessor((Image) bi));
        } else {
            if (!(cm instanceof IndexColorModel)) {
                // Image/J (as of version 1.26r) can not properly deal with non color
                // images and ColorModel that is not an instance of IndexedColorModel.
                cm = null;
            }
            ImageProcessor ip = createProcessor(r.getWidth(), r.getHeight(), r.getDataBuffer(), cm);
            ImagePlus im = new ImagePlus(title, ip);

            // Add calibration function for 'short' pixels
            if (db.getDataType() == DataBuffer.TYPE_SHORT) {
                Calibration cal = new Calibration(im);
                double[] coeff = new double[2];
                coeff[0] = -32768.0;
                coeff[1] = 1.0;
                cal.setFunction(Calibration.STRAIGHT_LINE, coeff, "gray value");
                im.setCalibration(cal);
            } else if (cm == null) {
                Calibration cal = im.getCalibration();
                im.setCalibration(null);
                ImageStatistics stats = im.getStatistics();
                im.setCalibration(cal);
                ip.setMinAndMax(stats.min, stats.max);
                im.updateImage();
            }
            return im;
        }
    }

    public static void test() {
        /** @todo test with short and float */
//        BufferedImage bi = edu.mbl.jif.imaging.ImageFactoryGrayScale.testImageByte();
//        System.out.println(ImgInfoDumper.dump(bi));
//        ImagePlus iPlus = new ImagePlus("NewImagePlus", bi); // makes an RGB
//        iPlus.show();
//        try {
//            ImagePlus iPlus2 = create(bi.getRaster(), bi.getColorModel(), "create");
//            iPlus2.show();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        int n = 6;
//        BufferedImage[] biArray = new BufferedImage[n];
//        for (int i = 0; i < n; i++) {
//            biArray[i] = edu.mbl.jif.imaging.ImageFactoryGrayScale.testImageByte();
//        }
//        ImagePlus[] iPlusArray = new ImagePlus[n];
//        for (int i = 0; i < n; i++) {
//            try {
//                iPlusArray[i] = create(biArray[i].getRaster(), biArray[i].getColorModel(),
//                        "slice " + String.valueOf(i));
//                iPlusArray[i].show();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        // Combine the images into a stack
//        ImagePlus iPlusStack = combineImages(iPlusArray);
//        iPlusStack.show();
    }

    // --------------------------- Copy File -------------------------------------
    /**
     * Copies a file to another location/file
     * Returns true if file copied or
     * false if not (possibly the file existed and 'overwrite' was not set).
     */
    public static boolean copyFile(String fromName, String toName, boolean overwrite)
        throws IOException {
        if (fromName == null) {
            throw new IOException("source filename is null!");
        }
        if (toName == null) {
            throw new IOException("destination filename is null!");
        }
        File fromFile = new File(fromName);
        File toFile = new File(toName);
        return (copyFile(fromFile, toFile, overwrite));
    }

    /**
     * Copies a file to another location/file
     * Returns true if file copied or
     * false if not (possibly the file existed and 'overwrite' was not set).
     */
    public static boolean copyFile(File fromFile, File toFile, boolean overwrite)
        throws IOException {
        final int IO_BUF_SIZE = 1024 * 32;
        if (fromFile == null) {
            throw new IOException("source file is null!");
        }
        if (toFile == null) {
            throw new IOException("destination file is null!");
        }

        // make sure that source file exists
        if (!fromFile.exists()) {
            throw new IOException("no such source file: " + fromFile.getAbsoluteFile());
        }
        if (!fromFile.isFile()) {
            throw new IOException("can't copy directory: " + fromFile.getAbsoluteFile());
        }
        if (!fromFile.canRead()) {
            throw new IOException("source file is unreadable: " + fromFile.getAbsoluteFile());
        }
        if (toFile.isDirectory()) {
            toFile = new File(toFile, fromFile.getName());
        }
        if (toFile.exists()) {
            if (!toFile.canWrite()) {
                throw new IOException("destination file is unwriteable: " +
                    toFile.getAbsoluteFile());
            }

            // check if we should overwrite it
            if (!overwrite) {
                return (false);
            }
        } else {
            // if the file dosn't exist, check if the directory exists and is
            // writeable. If getParent() returns null, then the directory is the
            // current dir. so look up the user.dir system property to find out
            // what that is.
            String parent = toFile.getParent(); // the destination dir
            if (parent == null) { // if none use the current dir
                parent = System.getProperty("user.dir");
            }
            File dir = new File(parent); // convert it to a file
            if (!dir.exists()) {
                throw new IOException("destination directory doesn't exist: " + parent);
            }
            if (dir.isFile()) {
                throw new IOException("destination is not a directory: " + parent);
            }
            if (!dir.canWrite()) {
                throw new IOException("destination directory is unwritable: " + parent);
            }
        }

        // if we've gotten this far then everything is ok
        // so we copy the file one buffer of bytes at a time
        FileInputStream from = null; // Stream to read from source
        FileOutputStream to = null; // Stream to write to destination
        try {
            from = new FileInputStream(fromFile); // Create input stream
            to = new FileOutputStream(toFile); // Create output stream
            byte[] buffer = new byte[IO_BUF_SIZE]; // to hold file data
            int bytesRead;

            // read a chunk of bytes into the buffer then write them out
            // looping until we reach the EOF (when read() returns -1)
            // Note the combination of assignment and comparison in this
            // while loop. This is a common I/O programming idiom.
            while ((bytesRead = from.read(buffer)) != -1) { // read until EOF
                to.write(buffer, 0, bytesRead); // write
            }
        } catch (IOException e) {
            throw (e);
        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException ignored) {
                }
            }
            if (to != null) {
                try {
                    to.close();
                } catch (IOException ignored) {
                }
            }
        }
        return (true);
    }
}
