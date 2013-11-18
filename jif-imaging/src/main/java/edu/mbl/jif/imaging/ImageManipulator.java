package edu.mbl.jif.imaging;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class ImageManipulator {
   public BufferedImage bImage;
   public byte[]        byteArray;

   public Rectangle     roi;
   public int           lineX1;
   public int           lineY1;
   public int           lineX2;
   public int           lineY2;

   public ImageManipulator(BufferedImage _bImage, byte[] _byteArray,
      Rectangle _roi, int _lineX1, int _lineY1, int _lineX2, int _lineY2) {

      bImage = _bImage;
      byteArray = _byteArray;
      roi = _roi;
      lineX1 = _lineX1;
      lineY1 = _lineY1;
      lineX2 = _lineX2;
      lineY2 = _lineY2;
   }
}
