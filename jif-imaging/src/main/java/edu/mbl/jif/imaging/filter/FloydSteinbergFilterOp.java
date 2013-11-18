package edu.mbl.jif.imaging.filter;

import java.awt.image.*;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
/**
 * This implements the classic (unidirectional) Floyd-Steinberg
 * error diffusion filter as an ImageFilterOp
 *
 * @author Pete Cockerell
 */
public class FloydSteinbergFilterOp implements BufferedImageOp {

  /**
   * Do the filter operation
   *
   * @param src The source BufferedImage. Can be any type.
   * @param dst The destination image. If not null, must be of type TYPE_BYTE_BINARY
   * @return A dithered version of src in a BufferedImage of type TYPE_BYTE_BINARY
   */
  public BufferedImage filter(BufferedImage src, BufferedImage dst) {

    // If there's no dest. create one
    if (dst == null)  {
      dst = createCompatibleDestImage(src, null);
    }
    // Otherwise check that the provided dest is a 1bpp image
    else if (dst.getType() != BufferedImage.TYPE_BYTE_BINARY) {
      throw new IllegalArgumentException("Buffer type");
    }

    DataBufferByte dstBuffer =
      (DataBufferByte) dst.getRaster().getDataBuffer();
    byte bitData[] = dstBuffer.getData();

    // NB This code makes many assumptions about the SampleModel of the
    // source (gray) and dst image buffers. If you use this in production
    // code, you should at least make some assertions that these assumptions
    // are correct, using a series of tests such as:
    MultiPixelPackedSampleModel dstSampleModel =
      (MultiPixelPackedSampleModel) dst.getSampleModel();
    if (dstSampleModel.getDataBitOffset() > 0) {
      //(Though you should use a more suitable exception than this...)
      throw new IllegalArgumentException("Bitmap offset > 0");
    }
    // Other things to test are pixel bit strides, scanline stride and transfer type
    // Same goes for the gray image

    int width = src.getWidth();
    int height = src.getHeight();

    // Copy the src into a gray-scale image. This simplifies both
    // calculating the error terms and propagating them to the
    // adjacent pixels
    BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    Graphics2D grayGraphics = grayImage.createGraphics();
    grayGraphics.drawImage(src, 0, 0, null);

    // We read the gray image at the data buffer level for efficiency
    DataBufferByte grayBuffer = (DataBufferByte)grayImage.getRaster().getDataBuffer();
    byte grayData[] = grayBuffer.getData();

    // This is the offset into the buffer of the current source pixel
    int grayDataIndex = 0;

    // This is the bit mask for the current dst pixel
    int bitMask = 0x80;
    // This is he offset into the buffer of the current dest pixel
    int bitDataIndex = 0;
    // This accumulates one byte's worth of bit data before writing to the buffer
    int bitBits = 0;

    // Loop through each pixel
    for (int y = 0; y < height; y++) {
      // If there's bit data from the previous row, flush it to the buffer
      // and restart the bitmask from the next byte
      if (bitBits != 0) {
          bitData[bitDataIndex] = (byte)bitBits;
          bitBits = 0;
          bitMask = 0x80;
          bitDataIndex++;
      }

      for (int x = 0; x < width; x++) {
        // Get the current pixel's gray level
        int data = grayData[grayDataIndex] & 0xff;

        // Convert it to white or black gray level and bitmap sample
        int col;
        if (data >= 128) {
          col = 0xff;
          bitBits |= bitMask;
        } else {
          col = 0;
        }

        // Shift to the next bitmap pixel. If we're at the end of the
        // byte, flush the accumulated samples to the data buffer
        bitMask >>>= 1;
        if (bitMask == 0) {
          bitData[bitDataIndex] = (byte)bitBits;
          bitBits = 0;
          bitMask = 0x80;
          bitDataIndex++;
        }

        // Find the error between the actual gray and the black/white we wrote
        int error = data - col;

        // Calculate the fractions of the error to propagate to adjacent
        // pixels:
        //             |  **  | 7/16 |
        //      | 3/16 | 5/16 | 1/16 |
        //
        // NB Using >>4 instead of /16 would gain a bit of speed
        // on some machines, even though this sometimes is off by one when
        // error is < 0 (because -1..-15 >> 4 == -1 whereas -1..-15 / 16 0= 0)
        // This reduces the quality of the dithering slightly
        int error_x1y0 = error*7/16;
        int error_x_1y1 = error*3/16;
        int error_x0y1 = error*5/16;
        // Note we get the last term based on the others to
        // ensure the sum is equal to error.
        int error_x1y1 = error - error_x1y0 - error_x_1y1 - error_x0y1;

        // Add the error terms to the appropriate pixel's values, being
        // careful not to exceed the image bounds, and also clipping the
        // adjusted values to [0..255]
        int grayDataIndexPlus1 = grayDataIndex + 1;
        if (x < width - 1) {
          // [x+1][y] case
          data = grayData[grayDataIndexPlus1] & 0xff;
          data += error_x1y0;
          if (data < 0) data = 0;
          else if (data > 255) data = 255;
          grayData[grayDataIndexPlus1] = (byte)data;
        }

        if (y < height - 1) {
          int grayDataIndexPlusWidth = grayDataIndex + width;
          // [x-1][y+1] case
          if (x > 0) {
            data = grayData[grayDataIndexPlusWidth-1] & 0xff;
            data += error_x_1y1;
            if (data < 0) data = 0;
            else if (data > 255) data = 255;
            grayData[grayDataIndexPlusWidth-1] = (byte)data;
          }

          // [x][y+1] case
          data = grayData[grayDataIndexPlusWidth] & 0xff;
          data += error_x0y1;
          if (data < 0) data = 0;
          else if (data > 255) data = 255;
          grayData[grayDataIndexPlusWidth] = (byte)data;

          // [x+1][y+1] case
          if (x < width - 1) {
            data = grayData[grayDataIndexPlus1+width] & 0xff;
            data += error_x1y1;
            if (data < 0) data = 0;
            else if (data > 255) data = 255;
            grayData[grayDataIndexPlus1+width] = (byte)data;
          }
        }
      // Move on to the next pixel offset
      grayDataIndex = grayDataIndexPlus1;
      }
    }
    // If there's bit data from the last byte, flush it to the buffer
    if (bitBits != 0) {
      bitData[bitDataIndex] = (byte)bitBits;
    }
    return dst;
  }

  // This always returns a binary image
  public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
    return new BufferedImage(
      src.getWidth(),
      src.getHeight(),
      BufferedImage.TYPE_BYTE_BINARY
    );
  }

  // There are no rendering hints
  public RenderingHints getRenderingHints() { return null; }

  // No transformation, so return the source point
  public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
    if (dstPt == null) dstPt = new Point2D.Float();
    dstPt.setLocation(srcPt.getX(), srcPt.getY());
    return dstPt;
  }

  // No transformation, so return the source bounds
  public Rectangle2D getBounds2D(BufferedImage src) {
    return src.getRaster().getBounds();
  }
}
