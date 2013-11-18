/*
 * SubSampleAverage.java
 *
 * Created on October 11, 2006, 1:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.imaging.jai;

import com.sun.media.jai.codec.SeekableStream;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.OpImage;
import javax.media.jai.RenderedOp;

/**
 *
 * @author GBH
 */
public class SubSampleAverage {
    
    /** Creates a new instance of SubSampleAverage */
    public SubSampleAverage() {}
    public static void main(String[] args) {
        
        float newWidth = 600;
        
        String sourceFilename = "D:\\testdata\\lightfield\\Source0.tif";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilename);
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        // read in the original image from an input stream
        SeekableStream s = SeekableStream.wrapInputStream(inputStream, true);
        RenderedOp image = JAI.create("stream", s);
        ((OpImage)image.getRendering()).setTileCache(null);
        // now resize the image
        float scale = newWidth / image.getWidth();
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image); // The source image
        pb.add(scale);          // The xScale
        pb.add(scale);          // The yScale
        pb.add(0.0F);           // The x translation
        pb.add(0.0F);           // The y translation
        // pb.add(new InterpolationNearest()); // The interpolation
        pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC));
        //pb.add(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
        RenderedOp resizedImage = JAI.create("scale", pb, null);
        BufferedImage resizedBuffImg = resizedImage.getAsBufferedImage();
        
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream("D:\\testdata\\lightfield\\Source30percent.tif");
            JAI.create("encode", resizedImage, outputStream, "TIFF", null);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
// -- OR --
//        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_RENDERING,
//                RenderingHints.VALUE_RENDER_QUALITY);
//        resizedImage = JAI.create("SubsampleAverage",
//                image, scale, scale, qualityHints);
    }
    public RenderedOp formatImageSubsampleAverage(RenderedImage source,
            int width, int height, RenderingHints hints) {
        double hRatio = ((double) height) / ((double) source.getHeight());
        double wRatio = ((double) width) / ((double) source.getWidth());
        double scale = Math.min(hRatio, wRatio);
        
        ParameterBlock scale_pb = new ParameterBlock();
        scale_pb.addSource(source).add(scale).add(scale);
        
        return JAI.create("SubsampleAverage", scale_pb, hints);
    }
}
